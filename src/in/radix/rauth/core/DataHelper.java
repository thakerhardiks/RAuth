package in.radix.rauth.core;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class DataHelper {
	
	/*
	 * RDBMS Connection Pool
	 */
	static BasicDataSource source = new BasicDataSource();
	
	/*
	 * Redis Connection Pool
	 */
	static JedisPool jPool = null;
	static JedisPoolConfig poolConfig = new JedisPoolConfig();
	
	static {
		if(RAuthCore.USE_RAUTH_RDBMS_POOLING) {
			//DBMS Pool Start
			source.setDriverClassName(RAuthCore.DB_DRIVER);
			source.setUsername(RAuthCore.DB_USER);
			source.setPassword(RAuthCore.DB_PASSWORD);
			source.setUrl(RAuthCore.DB_CONN_URL);
			source.setMaxActive(RAuthCore.DB_POOL_MAX_ACTIVE);
			source.setMaxWait(RAuthCore.DB_POOL_MAX_WAIT);
			source.setMaxIdle(RAuthCore.DB_POOL_MAX_IDLE);
			//DBMS Pool End
		}
		//Redis Pool Start
		poolConfig.setMaxTotal(RAuthCore.REDIS_POOL_MAX_TOTAL);
		poolConfig.setMaxWaitMillis(RAuthCore.REDIS_POOL_MAX_WAIT);
		poolConfig.setMaxIdle(RAuthCore.REDIS_POOL_MAX_IDLE);
		poolConfig.setMinIdle(RAuthCore.REDIS_POOL_MIN_IDLE);
		
		if (RAuthCore.REDIS_AUTH != null) {
			jPool = new JedisPool(poolConfig, RAuthCore.REDIS_HOST, RAuthCore.REDIS_PORT, RAuthCore.REDIS_TIMEOUT, RAuthCore.REDIS_AUTH);
		} else {
			jPool = new JedisPool(poolConfig, RAuthCore.REDIS_HOST, RAuthCore.REDIS_PORT, RAuthCore.REDIS_TIMEOUT);
		}
		//Redis Pool End
	}
	
	/**
	 * @return RDBMS Connection Object From ConnectionPool
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return source.getConnection();
	}
	
	/**
	 * Releases RDBMS connection and returns to the connection pool
	 * @param connection
	 * @throws SQLException
	 */
	public static void releaseConnection(Connection connection) throws SQLException {
		connection.close();
	}
	
	/**
	 * @return String Array Of Username Check Columns
	 */
	public static String[] getIdentityColumns() {
		return RAuthCore.DB_TABLE_USERNAME.split(",");
	}
	
	/**
	 * 
	 * @return
	 */
	private static Jedis getRedisConnection() {
		return jPool.getResource();
	}
	
	/**
	 * 
	 * @param jedis
	 */
	private static void releaseRedisConnection(Jedis jedis) {
		jedis.close();
	}
	
	public static void saveJwt(String jwt) {
		Jedis j = null;
		try {
			j = getRedisConnection();
		    j.connect();
		    j.set(jwt, "");
		} finally {
			if(null != j) {
				j.disconnect();
				releaseRedisConnection(j);
			}
		}
	}
	
	public static boolean isJwtExists(String jwt) {
		Jedis j = null;
		boolean isExists = false;
		try {
			j = getRedisConnection();
		    j.connect();
		    isExists = j.exists(jwt);
		} finally {
			if(null != j) {
				j.disconnect();
				releaseRedisConnection(j);
			}
		}
		return isExists; 
	}
	
	public static void setJwtExp(String jwt, int seconds) {
		Jedis j = null;
		try {
			j = getRedisConnection();
		    j.connect();
		    j.expire(jwt, seconds);
		} finally {
			if(null != j) {
				j.disconnect();
				releaseRedisConnection(j);
			}
		}
	}
}
