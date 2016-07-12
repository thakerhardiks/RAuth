package in.radix.rauth.app;

import java.sql.Connection;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.json.JSONException;

import in.radix.rauth.core.Audience;
import in.radix.rauth.core.DBName;
import in.radix.rauth.core.DataHelper;
import in.radix.rauth.core.RAuthCore;
import in.radix.rauth.core.User;
import in.radix.rauth.rdbms.Database;
import in.radix.rauth.rdbms.DatabaseFactory;

public class RAuth {
	private String userName;
	private String userPassword;
	private static DatabaseFactory dF = new DatabaseFactory();
	
	public static User authUser(String userName, String userPassword) throws Exception {
		if(!RAuthCore.USE_RAUTH_RDBMS_POOLING) {
			throw new Exception("RAuth Connection Pooling Should Be Enabled To Use This Method. Use authUser(Connection connection, String userName, String userPassword) method."); 
		}
		
		Database dB = dF.getDb(DBName.MySQL);
		return dB.getUser(userName, userPassword);
	}
	
	public static User authUser(Connection connection, String userName, String userPassword) throws JSONException, JoseException {
		if(RAuthCore.USE_RAUTH_RDBMS_POOLING) {
			System.out.println("RAuth Connection Pooling Is Already Enabled. Use authUser(String userName, String userPassword) for best performance."); 
		}
		Database dB = dF.getDb(DBName.MySQL);
		dB.setConnection(connection);
		return dB.getUser(userName, userPassword);
	}

	public static String issueJwt(Audience audience, String payload) throws JoseException {
		// Create the Claims, which will be the content of the JWT
	    JwtClaims claims = new JwtClaims();
	    claims.setIssuer(RAuthCore.JWT_ISSUER);  // who creates the token and signs it
	    claims.setAudience(audience.name()); // to whom the token is intended to be sent
	    claims.setExpirationTimeMinutesInTheFuture(RAuthCore.REDIS_WEB_EXP); // time when the token will expire (10 minutes from now)
	    claims.setGeneratedJwtId(); // a unique identifier for the token
	    claims.setIssuedAtToNow();  // when the token was issued/created (now)
	    claims.setNotBeforeMinutesInThePast(1); // time before which the token is not yet valid (2 minutes ago)
	    claims.setSubject("RAuthentication"); // the subject/principal is whom the token is about
	    claims.setClaim("RAtuhData", RAuthCore.APP+"/"+audience.name()+"/"+payload); // additional claims/attributes about the subject can be added
	    
	    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
	    // In this example it is a JWS so we create a JsonWebSignature object.
	    JsonWebSignature jws = new JsonWebSignature();

	    // The payload of the JWS is JSON content of the JWT Claims
	    jws.setPayload(claims.toJson());

	    // The JWT is signed using the private key
	    jws.setKey(RAuthCore.RSAKEY.getPrivateKey());

	    // Set the Key ID (kid) header because it's just the polite thing to do.
	    // We only have one key in this example but a using a Key ID helps
	    // facilitate a smooth key rollover process
	    jws.setKeyIdHeaderValue(RAuthCore.RSAKEY.getKeyId());

	    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

	    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
	    // representation, which is a string consisting of three dot ('.') separated
	    // base64url-encoded parts in the form Header.Payload.Signature
	    // If you wanted to encrypt it, you can simply set this jwt as the payload
	    // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
	    String jwt = jws.getCompactSerialization();
	    
	    //Saving Key To Redis
	    DataHelper.saveJwt(jwt);
	    
	    //Set Expiration
	    if(RAuthCore.USE_REDIS_EXPIRE && audience.equals(Audience.WEB))
        	DataHelper.setJwtExp(jwt, RAuthCore.REDIS_WEB_EXP);
	    
	    return jwt;
	}
	
	public static String authJwt(Audience audience, String jwt) {
		if(DataHelper.isJwtExists(jwt)) {
			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime() // the JWT must have an expiration time
				.setRequireSubject() // the JWT must have a subject claim
				.setExpectedIssuer(RAuthCore.JWT_ISSUER) // whom the JWT needs to have been issued by
				.setExpectedAudience(audience.name()) // to whom the JWT is intended for
				.setVerificationKey(RAuthCore.RSAKEY.getKey()) // verify the signature with the public key
				.build(); // create the JwtConsumer instance
			
			try {
		        //  Validate the JWT and process it to the Claims
		        JwtClaims payload = jwtConsumer.processToClaims(jwt);
		        if(RAuthCore.USE_REDIS_EXPIRE && audience.equals(Audience.WEB))
		        	DataHelper.setJwtExp(jwt, RAuthCore.REDIS_WEB_EXP);
		        return payload.toString();
		    } catch (InvalidJwtException e) {
		        // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
		        // Hopefully with meaningful explanations(s) about what went wrong.
		        e.printStackTrace();
		    }
		}
		return null;
	}

	// Getter Setter
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
