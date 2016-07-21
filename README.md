# RAuth - Reliable Robust Radix Authentication
> RAuth token based authentication system build on top of Java and Redis.

The general concept behind a RAuth token-based authentication system is simple. Allow users to enter their username and password in order to obtain a token which allows them to fetch a specific resource - without using their username and password. Once their token has been obtained, the user can offer the token - which offers access to a specific resource for a time period - to the remote site.

RAuth is a java library which provides token-based authentication system for all type of java projects or can run independently to support your existing system.

# Quick Installation

* Add RAuth jar into your project.
* RAuth requires a separate directory containing `app.properties` file. For linux and unix operating systems it should be `/opt/rauth/` and for windows it should be `C:\rauth`.
* RAuth provides a facility to create independent database connection pools as well. If you want to use your connection pooling mechanism you need to pass it to DataHelper class.
* To authenticate a user simply call static method, `RAuth.authUser("USERNAME", "PASSWORD");`. This method will return `User` object.
```
User u = RAuth.authUser("username", "password");								
if(u.getUserStatus().equals(UserStatus.ACTIVATE)) {							
	String jwt = RAuth.issueJwt(Audience, Payload);			
}
```
* Store this token in your client and send it with your request to the server.
* The method `RAuth.authJwt(Audience, jwt)` returns payload from the token. Extract and verify token from its payload.
That's it. Isn't it really simple?



