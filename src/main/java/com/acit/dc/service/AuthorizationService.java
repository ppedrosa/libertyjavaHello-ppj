package com.acit.dc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.acit.dc.util.DashDBUtil;

public class AuthorizationService {
	
	public boolean authorizeUser(String userid) {
		System.out.println("method authorizeUser");
		Connection connection = DashDBUtil.connectDashDB();
		PreparedStatement preparedStatement = null;
		if(userid.contains("@accenture.com")){
			userid=userid.substring(0,userid.lastIndexOf("@"));
		}
		boolean authFlag = false;
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM USER_GROUP WHERE ROLE='ADMIN' and USERID=?");
			preparedStatement.setString(1, userid);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				System.out.println("user is admin ");
				authFlag=true;
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != preparedStatement) {
					preparedStatement.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
		return authFlag;
	}
}
