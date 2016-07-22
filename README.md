# RAuth - Reliable Robust Radix Authentication
> RAuth token based authentication system build on top of Java and Redis.

![Alt text](http://i.imgur.com/2O7q6sq.png "RAuth Hardik")

The general concept behind a RAuth token-based authentication system is simple. Allow users to enter their username and password in order to obtain a token which allows them to fetch a specific resource - without using their username and password. Once their token has been obtained, the user can offer the token - which offers access to a specific resource for a time period - to the remote site.

RAuth is a java library which provides token-based authentication system for all type of java projects or can run independently to support your existing system.

## Prerequisite
* RAuth requires a separate directory containing `app.properties` file. For linux and unix operating systems it should be `/opt/rauth/` and for windows it should be `C:\rauth`. Download property file from here: [app.properties](https://github.com/thakerhardiks/RAuth/blob/master/rauth.properties). I will write detail explaination of it soon.
* Running instance of Redis. Follow [this link](http://redis.io/topics/quickstart) to know how to install redis on your server/machine. (Please remove REDIS_AUTH from property file if you are not using redis authentication)
* Running instance of your RDBMS. Sample SQL : [rauth.db.sql](https://github.com/thakerhardiks/RAuth/blob/master/rauth.db.sql)

## Generating Authentication Tokens

* Add RAuth jar into your project.
* RAuth provides a facility to create independent database connection pools as well. If you want to use your connection pooling mechanism you need to pass it to DataHelper class.
* To authenticate a user simply call static method, `RAuth.authUser("USERNAME", "PASSWORD");`. This method will return `User` object. Payload is a JWT term represents content/information you want to put inside your token. Read more about [JWT](http://jwt.io) here.
```
User u = RAuth.authUser("username", "password");
if(u.getUserStatus().equals(UserStatus.ACTIVATE)) {							
	/* 
	 * Add Your ACL Code Here ! RAuth future release will have it.
	 */
	String rtoken = RAuth.issueJwt(Audience, Payload);			
}
```
* Store this token in your client and send it with your request to the server.

## Authenticating Tokens
* The method `RAuth.authJwt(Audience, jwt)` returns payload from the token. Extract and verify token from its payload.
That's it. Isn't it really simple?

## What's Next?
* Good set of examples explaining single sign on feature, Cross devices authentication.
* Implementing ORM to make it database independent
* Detailed guide of system with [Redis LRU Cache](http://redis.io/topics/lru-cache) example.
* LDAP Integration.

Let's make a scalable authentication system together!

- Hardik Thaker.



