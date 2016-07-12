package in.radix.rauth.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

public class RAuthCore {
	
	public static final String APP = "RAuth";
	public static final String VERSION = "0.1";
	public static final String AUTHOR = "Hardik Thaker";
	public static final String COMPANY = "Radix Analytics Pvt. Ltd.";
	public static final String COMPANY_URL = "http://radixanalytics.com?utm_source=rauth";
	
	/**
	 * RAuth Requirements
	 */
	public static String SYSTEM;
	public static String RAUTH_ROOT;
	
	/**
	 * MySQL Stack Details
	 */
	public static String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static String DB_CONN_URL = "jdbc:mysql://localhost:3306/rauth_db";
	public static String DB_USER = "root";
	public static String DB_PASSWORD = "root";
	
	//UserTable Details
	public static String DB_TABLE = "user";
	public static String DB_TABLE_ID = "user_id";
	public static String DB_TABLE_USERNAME = "user_name,user_email,user_phone";
	public static String DB_TABLE_USERPASSWORD = "user_password";
	public static String DB_TABLE_USERSTATUS = "user_status";
	
	/**
	 * Redis Stack Details
	 */
	public static String REDIS_HOST = "localhost";
	public static int REDIS_PORT = 6379;
	public static String REDIS_AUTH = null;
	public static int REDIS_TIMEOUT = 1000;
	public static int REDIS_WEB_EXP = 1440;
	public static int REDIS_DEVICE_EXP = -1;
	public static boolean USE_REDIS_EXPIRE = true;
	
	/**
	 * Pooling Details
	 */
	public static boolean USE_RAUTH_RDBMS_POOLING = true;
	public static int DB_POOL_MAX_ACTIVE = 100;
	public static int DB_POOL_MAX_WAIT = 10000;
	public static int DB_POOL_MAX_IDLE = 10;
	
	public static int REDIS_POOL_MAX_TOTAL = 100;
	public static int REDIS_POOL_MAX_WAIT = 100000;
	public static int REDIS_POOL_MAX_IDLE = 10;
	public static int REDIS_POOL_MIN_IDLE = 1;
	
	/**
	 * JWT Key
	 */
	public static String JWT_ISSUER = "RAuth";
	public static String AESKEY = "r@d!x@na|yt!c$12"; //Must be 128bits
	public static RsaJsonWebKey RSAKEY;
	
	static {
		try{
			RSAKEY = RsaJwkGenerator.generateJwk(2048);
			RSAKEY.setKeyId("RAuth");
			
			//Reading RAuth Property
			SYSTEM = System.getProperty("os.name");
			
			if(SYSTEM.toLowerCase().contains("windows")) {
				RAUTH_ROOT = "C:\\rauth\\";
			} else {
				RAUTH_ROOT = "/opt/rauth/";
			}
			
			Properties prop = new Properties();
			InputStream input = null;
		
			input = new FileInputStream(RAUTH_ROOT+"app.properties");
			prop.load(input);
			
			if(null != prop.getProperty("DB_DRIVER"))
				DB_DRIVER = prop.getProperty("DB_DRIVER");
			if(null != prop.getProperty("DB_CONN_URL"))
				DB_CONN_URL = prop.getProperty("DB_CONN_URL");
			if(null != prop.getProperty("DB_USER"))
				DB_USER = prop.getProperty("DB_USER");
			if(null != prop.getProperty("DB_PASSWORD"))
				DB_PASSWORD = prop.getProperty("DB_PASSWORD");
			
			if(null != prop.getProperty("DB_TABLE"))
				DB_TABLE = prop.getProperty("DB_TABLE");
			if(null != prop.getProperty("DB_TABLE_ID"))
				DB_TABLE_ID = prop.getProperty("DB_TABLE_ID");
			if(null != prop.getProperty("DB_TABLE_USERNAME"))
				DB_TABLE_USERNAME = prop.getProperty("DB_TABLE_USERNAME");
			if(null != prop.getProperty("DB_TABLE_USERPASSWORD"))
				DB_TABLE_USERPASSWORD = prop.getProperty("DB_TABLE_USERPASSWORD");
			if(null != prop.getProperty("DB_TABLE_USERSTATUS"))
				DB_TABLE_USERSTATUS = prop.getProperty("DB_TABLE_USERSTATUS");
			
			if(null != prop.getProperty("REDIS_HOST"))
				REDIS_HOST = prop.getProperty("REDIS_HOST");
			if(null != prop.getProperty("REDIS_PORT"))
				REDIS_PORT = Integer.parseInt(prop.getProperty("REDIS_PORT"));
			if(null != prop.getProperty("REDIS_AUTH"))
				REDIS_AUTH = prop.getProperty("REDIS_AUTH");
			if(null != prop.getProperty("REDIS_WEB_EXP"))
				REDIS_WEB_EXP = Integer.parseInt(prop.getProperty("REDIS_WEB_EXP"));
			if(null != prop.getProperty("REDIS_DEVICE_EXP"))
				REDIS_DEVICE_EXP = Integer.parseInt(prop.getProperty("REDIS_DEVICE_EXP"));
			
			if(null != prop.getProperty("AESKEY"))
				AESKEY = prop.getProperty("AESKEY");
			
		} catch(IOException | JoseException e) {
			e.printStackTrace();
		}
	}
}
