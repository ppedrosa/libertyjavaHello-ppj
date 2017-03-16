package com.acit.dc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.acit.dc.util.DashDBUtil;

public class DashboardService {

	public int addNewApplication(HttpServletRequest request) throws IOException, ServletException {
		String appName = request.getParameter("appName");
		String optGrp = request.getParameter("optGrp");
		String shortDesc = request.getParameter("shortDesc");
		String challenge = request.getParameter("challenge");
		String solution = request.getParameter("solution");
		String delivery = request.getParameter("delivery");
		String dcmntUrl = request.getParameter("dcmntUrl");
		String videoUrl = request.getParameter("videoUrl");
		String appUrl = request.getParameter("appUrl");
		String technologyArea = request.getParameter("technologyArea");
		String appCredential = request.getParameter("appCredential");
		String appPriority = request.getParameter("appPriority");
		System.out.println("appName::"+appName);
		final Part filePart = request.getPart("image");
		Connection connection = DashDBUtil.connectDashDB();
		PreparedStatement preparedStatement = null;
		int dbPriority = 0;
		int rows = 0;
		if (appPriority.length() > 0) {
			dbPriority = Integer.parseInt(appPriority);
		}
		try {
			/*preparedStatement = connection.prepareStatement("INSERT INTO APP_DETAILS1 (OPERATING_GROUP_NAME, APP_NAME,"
					+ " SHORT_DESCRIPTION, CHALLENGE, SOLUTION, DELIVERY, APP_URL, DOCUMENT_URL, VIDEO_URL,"
					+ " PRIORITY, CREATED_ON, UPDATED_ON, APP_INSTRUCTIONS, IMAGE_TYPE,"
					+ " IS_ACTIVE, BINARY_IMAGE,TECHONOLOGY_AREA) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");*/
			int newAppId=0;
			//try {
				preparedStatement = connection.prepareStatement("select max(app_id) as last_app_id from app_details1");
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()){
					newAppId=rs.getInt("last_app_id");
					System.out.println("Last App ID::"+newAppId);
				}
				newAppId=newAppId+1;
			preparedStatement = connection.prepareStatement("INSERT INTO APP_DETAILS1 (APP_ID,OPERATING_GROUP_NAME, APP_NAME,"
					+ " SHORT_DESCRIPTION, CHALLENGE, SOLUTION, DELIVERY, APP_URL, DOCUMENT_URL, VIDEO_URL,"
					+ " PRIORITY, CREATED_ON, UPDATED_ON, APP_INSTRUCTIONS, IMAGE_TYPE,"
					+ " IS_ACTIVE, BINARY_IMAGE,TECHONOLOGY_AREA) Values("+newAppId+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			Date currentDate = new Date(System.currentTimeMillis());
			preparedStatement.setString(1, optGrp);
			preparedStatement.setString(2, appName);
			preparedStatement.setString(3, shortDesc);
			preparedStatement.setString(4, challenge);
			preparedStatement.setString(5, solution);
			preparedStatement.setString(6, delivery);
			preparedStatement.setString(7, appUrl);
			preparedStatement.setString(8, dcmntUrl);
			preparedStatement.setString(9, videoUrl);
			preparedStatement.setInt(10, dbPriority);
			preparedStatement.setDate(11, currentDate);
			preparedStatement.setDate(12, currentDate);
			preparedStatement.setString(13, appCredential);
			preparedStatement.setString(14, null);
			preparedStatement.setBinaryStream(16, null, 0);
			if (null != filePart && filePart.getSize() > 0) {
				preparedStatement.setString(14, filePart.getContentType());
				preparedStatement.setBinaryStream(16, filePart.getInputStream(), filePart.getInputStream().available());
			} 
			preparedStatement.setInt(15, 1);
			preparedStatement.setString(17, technologyArea); 
			//System.out.println("technologyArea::"+technologyArea);
			rows = preparedStatement.executeUpdate();
			System.out.println("debug::");
		} catch (SQLException e) {
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
		return rows;
	}
	
	public int updateApplication(HttpServletRequest request, String appId) throws IOException, ServletException {
		String appName = request.getParameter("appName");
		String optGrp = request.getParameter("optGrp");
		String shortDesc = request.getParameter("shortDesc");
		String challenge = request.getParameter("challenge");
		String solution = request.getParameter("solution");
		String delivery = request.getParameter("delivery");
		String dcmntUrl = request.getParameter("dcmntUrl");
		String videoUrl = request.getParameter("videoUrl");
		String appUrl = request.getParameter("appUrl");
		String technologyArea = request.getParameter("technologyArea");
		String appCredential = request.getParameter("appCredential");
		String appPriority = request.getParameter("appPriority");
		final Part filePart = request.getPart("image");
		long filePartSize = filePart.getSize();
		String preImg = request.getParameter("preImg");
		Connection connection = DashDBUtil.connectDashDB();
		PreparedStatement preparedStatement = null;
		int dbPriority = 0;
		int rows = 0;
		if (appPriority.length() > 0) {
			dbPriority = Integer.parseInt(appPriority);
		}
		try {
				preparedStatement = connection.prepareStatement("UPDATE APP_DETAILS1 SET OPERATING_GROUP_NAME=?, APP_NAME=?,"
						+ " SHORT_DESCRIPTION=?, CHALLENGE=?, SOLUTION=?, DELIVERY=?, APP_URL=?, DOCUMENT_URL=?, VIDEO_URL=?,"
						+ " PRIORITY=?, UPDATED_ON=?, APP_INSTRUCTIONS=?, IMAGE_TYPE=?,"
						+ " BINARY_IMAGE=?,TECHONOLOGY_AREA=? WHERE APP_ID = ?");
			preparedStatement.setString(1, optGrp);
			preparedStatement.setString(2, appName);
			preparedStatement.setString(3, shortDesc);
			preparedStatement.setString(4, challenge);
			preparedStatement.setString(5, solution);
			preparedStatement.setString(6, delivery);
			preparedStatement.setString(7, appUrl);
			preparedStatement.setString(8, dcmntUrl);
			preparedStatement.setString(9, videoUrl);
			preparedStatement.setInt(10, dbPriority);
			preparedStatement.setDate(11, new Date(System.currentTimeMillis()));
			preparedStatement.setString(12, appCredential);
			preparedStatement.setString(13, null);
			preparedStatement.setBinaryStream(14, null, 0);
			if (null != filePart && filePartSize > 0) {
				preparedStatement.setString(13, filePart.getContentType());
				preparedStatement.setBinaryStream(14, filePart.getInputStream(), filePart.getInputStream().available());
			} else if (filePartSize == 0 && null != preImg) {
				Path preImgPath = Paths.get(request.getServletContext().getRealPath(File.separator) + File.separator + preImg);
				File file = new File(preImgPath.toString());
				InputStream is = new FileInputStream(file);
				preparedStatement.setString(13, Files.probeContentType(preImgPath));
				preparedStatement.setBinaryStream(14, is, is.available());
			}
			preparedStatement.setString(15, technologyArea);
			preparedStatement.setInt(16, Integer.parseInt(appId));
			rows = preparedStatement.executeUpdate();
		} catch (SQLException e) {
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
		return rows;
	}

	public int deleteApp(String appId) {
		Connection connection = DashDBUtil.connectDashDB();
		PreparedStatement preparedStatement = null;
		int delete = 0;
		try {
			preparedStatement = connection.prepareStatement("UPDATE APP_DETAILS1 SET IS_ACTIVE=0 WHERE APP_ID=?");
			preparedStatement.setInt(1, Integer.parseInt(appId));
			delete = preparedStatement.executeUpdate();
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
		return delete;
	}
}
