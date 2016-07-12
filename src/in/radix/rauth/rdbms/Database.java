package in.radix.rauth.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import in.radix.rauth.core.User;

public interface Database {
	User user = new User();
	Connection connection = null;
	String sqlStatement = "";
	ResultSet rs = null;
	PreparedStatement ps = null;
	
	void setConnection(Connection connection);
	User getUser(String userName, String userPassword);
}
