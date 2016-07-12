package in.radix.rauth.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.radix.rauth.core.DataHelper;
import in.radix.rauth.core.RAuthCore;
import in.radix.rauth.core.User;
import in.radix.rauth.core.UserStatus;

public class MySQL implements Database {
	
	private User user = new User();
	private Connection connection = null;
	private String sqlStatement = "";
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	@Override
	public User getUser(String userName, String userPassword) {
		user.setUserStatus(UserStatus.NOT_FOUND);
		try {
			sqlStatement = "SELECT * FROM "+RAuthCore.DB_TABLE+" WHERE ( ";
			for(String column : DataHelper.getIdentityColumns()) {
				sqlStatement += " "+column+" = ? OR ";
			}
			//Remove trailing OR
			sqlStatement = sqlStatement.substring(0, sqlStatement.length()-3);
			sqlStatement += " ) AND "+RAuthCore.DB_TABLE_USERPASSWORD+" = md5(?)";
			System.out.println("Executing : "+sqlStatement);
			
			if(null == this.connection)
				connection = DataHelper.getConnection();
			
			ps = connection.prepareStatement(sqlStatement);
			int colCount = DataHelper.getIdentityColumns().length;
			for(int i=1;i<=colCount;i++) {
				ps.setString(i, userName);
			}
			ps.setString(colCount+1, userPassword);
			rs = ps.executeQuery();
			if(rs.next()) {
				user.setUserId(rs.getString(RAuthCore.DB_TABLE_ID));
				user.setUserStatus(UserStatus.valueOf(rs.getString(RAuthCore.DB_TABLE_USERSTATUS)));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != rs)
					rs.close();
				if(null != ps)
					ps.close();
				if(null != connection)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
