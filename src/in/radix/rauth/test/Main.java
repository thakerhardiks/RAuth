package in.radix.rauth.test;

import org.jose4j.lang.JoseException;
import org.json.JSONException;

import in.radix.rauth.app.RAuth;
import in.radix.rauth.core.Audience;
import in.radix.rauth.core.RAuthCore;
import in.radix.rauth.core.User;
import in.radix.rauth.core.UserStatus;

public class Main {

	public static void main(String[] args) {
		System.out.println(RAuthCore.SYSTEM);
		try {
			User u = RAuth.authUser("h1", "hardik");
			String jwt = "";
			if(u.getUserStatus().equals(UserStatus.ACTIVATE)) {
				jwt = RAuth.issueJwt(Audience.WEB, u.getUserId());
			}
			
			System.out.println(RAuth.authJwt(Audience.WEB, jwt));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JoseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
