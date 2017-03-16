package com.acit.dc.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.acit.dc.util.DashDBUtil;
import com.acit.dc.util.DemoCatalogConstants;
import com.acit.dc.util.ImageType;

public class DemoCatalogService {
	
	public JSONArray createSliderJSONArray(ServletContext context) throws SQLException {
		JSONArray sliderJsonArray = new JSONArray();
		String sliderQuery = "SELECT * FROM SLIDER1 ORDER BY CREATED_ON DESC LIMIT 3";
		try (Connection connection = DashDBUtil.connectDashDB();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sliderQuery)) {
			String dirPath = createImageDirectory(context);
			while (resultSet.next()) {
				String imagePath = null;
				if (null != resultSet.getBinaryStream("IMAGE")) {
					String imageExt = ImageType.getImageType(resultSet.getString("IMAGE_TYPE").toLowerCase());
					Path filePath = Paths
							.get(dirPath + File.separator + resultSet.getInt("ID") + DemoCatalogConstants.SLIDER + imageExt);
					if (!Files.exists(filePath)) {
						Files.copy(resultSet.getBinaryStream("IMAGE"), filePath, StandardCopyOption.REPLACE_EXISTING);
					}
					imagePath = DemoCatalogConstants.IMG_DIR_NAME + File.separator + resultSet.getInt("ID")
					+ DemoCatalogConstants.SLIDER + imageExt;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("title", resultSet.getString("TITLE"));
				jsonObject.put("content", resultSet.getString("DESCRIPTION"));
				jsonObject.put("image", imagePath);
				sliderJsonArray.put(jsonObject);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return sliderJsonArray;
	}

	public JSONArray createLandingPageAppsJSONArray(ServletContext context) throws SQLException {
		JSONArray appJsonArray = new JSONArray();
		String landingPageAppQuery = "SELECT * FROM APP_DETAILS1 WHERE OPERATING_GROUP_NAME IN (SELECT DISTINCT OPERATING_GROUP_NAME FROM APP_DETAILS1) AND IS_ACTIVE=1 AND PRIORITY=1 ORDER BY OPERATING_GROUP_NAME ASC";
		try (Connection connection = DashDBUtil.connectDashDB();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(landingPageAppQuery)) {
			String opGrpName = null;
			String dirPath = createImageDirectory(context);
			while (resultSet.next()) {
				String imagePath= null;
				if (null != resultSet.getBinaryStream("BINARY_IMAGE")) {
					String imageExt = ImageType.getImageType(resultSet.getString("IMAGE_TYPE").toLowerCase());
					Path filePath = Paths.get(dirPath + File.separator + resultSet.getInt("APP_ID") + imageExt);
					//if(!Files.exists(filePath)) {
						Files.copy(resultSet.getBinaryStream("BINARY_IMAGE"), filePath, StandardCopyOption.REPLACE_EXISTING);					
					//}
					imagePath = DemoCatalogConstants.IMG_DIR_NAME + File.separator + resultSet.getInt("APP_ID") + imageExt;
				}
				
				JSONObject jsonObject = new JSONObject();
				opGrpName = resultSet.getString("OPERATING_GROUP_NAME");
				jsonObject.put("title", opGrpName);
				jsonObject.put("appName", resultSet.getString("APP_NAME"));
				jsonObject.put("description", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("imgSrc", imagePath);
				jsonObject.put("homeId", getHomeID(opGrpName));
				jsonObject.put("challenge", resultSet.getString("CHALLENGE"));
				jsonObject.put("solution", resultSet.getString("SOLUTION"));
				jsonObject.put("delivery", resultSet.getString("DELIVERY"));
				jsonObject.put("docLink", resultSet.getString("DOCUMENT_URL"));
				jsonObject.put("videoLink", resultSet.getString("VIDEO_URL"));
				jsonObject.put("appLink", resultSet.getString("APP_URL"));
				jsonObject.put("cred", resultSet.getString("APP_INSTRUCTIONS"));
				jsonObject.put("technologyArea", resultSet.getString("TECHONOLOGY_AREA"));
				appJsonArray.put(jsonObject);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return appJsonArray;
	}
	
	public JSONArray getAppDetailsBasedOnOpertingGroup(String operatingGroup, ServletContext context) throws SQLException {
		JSONArray appJsonArray = new JSONArray();
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			connection = DashDBUtil.connectDashDB();
			String landingPageAppQuery = "SELECT * FROM APP_DETAILS1 WHERE IS_ACTIVE=1 AND OPERATING_GROUP_NAME = ? ORDER BY PRIORITY ASC";
			statement = connection.prepareStatement(landingPageAppQuery);
			statement.setString(1, operatingGroup);
			resultSet = statement.executeQuery();
			String dirPath = createImageDirectory(context);
			while (resultSet.next()) {
				String imagePath = null;
				if (null != resultSet.getBinaryStream("BINARY_IMAGE")) {
					String imageExt = ImageType.getImageType(resultSet.getString("IMAGE_TYPE").toLowerCase());
					Path filePath = Paths.get(dirPath + File.separator + resultSet.getInt("APP_ID") + imageExt);
					//if (!Files.exists(filePath)) {
						Files.copy(resultSet.getBinaryStream("BINARY_IMAGE"), filePath, StandardCopyOption.REPLACE_EXISTING);
					//}
					imagePath = DemoCatalogConstants.IMG_DIR_NAME + File.separator + resultSet.getInt("APP_ID") + imageExt;
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("title", resultSet.getString("OPERATING_GROUP_NAME"));
				jsonObject.put("appName", resultSet.getString("APP_NAME"));
				jsonObject.put("challenge", resultSet.getString("CHALLENGE"));
				jsonObject.put("solution", resultSet.getString("SOLUTION"));
				jsonObject.put("delivery", resultSet.getString("DELIVERY"));
				jsonObject.put("docLink", resultSet.getString("DOCUMENT_URL"));
				jsonObject.put("videoLink", resultSet.getString("VIDEO_URL"));
				jsonObject.put("appLink", resultSet.getString("APP_URL"));
				jsonObject.put("description", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("imgSrc", imagePath);
				jsonObject.put("cred", resultSet.getString("APP_INSTRUCTIONS"));
				jsonObject.put("technologyArea", resultSet.getString("TECHONOLOGY_AREA"));
				appJsonArray.put(jsonObject);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		} finally {
			if (null != resultSet) {
				resultSet.close();
			}
			if (null != statement) {
				statement.close();
			}
			if (null != connection) {
				connection.close();
			}
		}
		return appJsonArray;
	}
	
	private String createImageDirectory(ServletContext context) throws IOException {
		Path dirPath = null;
		String contextpath = context.getRealPath(File.separator);
		if (!contextpath.endsWith(File.separator)) {
			contextpath += File.separator;
		}
		dirPath = Paths.get(contextpath + DemoCatalogConstants.IMG_DIR_NAME);
		if (!Files.isDirectory(dirPath)) {
			dirPath = Files.createDirectory(dirPath);
		}
		return dirPath.toString();
	}

	private String getHomeID(String opGrp) {
		String homeID = null;
		switch (opGrp) {
		case DemoCatalogConstants.OP_GRP_FINANCE:
			homeID = "finance";
			break;
		case DemoCatalogConstants.OP_GRP_HEALTH:
			homeID = "health";
			break;
		case DemoCatalogConstants.OP_GRP_PRODUCTS:
			homeID = "products";
			break;
		case DemoCatalogConstants.OP_GRP_COMMUNICATIONS:
			homeID = "communications";
			break;
		case DemoCatalogConstants.OP_GRP_RESOURCES:
			homeID = "resources";
			break;
		case DemoCatalogConstants.OP_GRP_CROSS:
			homeID = "crossIndustry";
			break;
		default:
			break;
		}
		return homeID;
	}

	public JSONArray getApplicationDetails() {
		JSONArray appJsonArray = new JSONArray();
		String landingPageAppQuery = "SELECT APP_ID, APP_NAME, OPERATING_GROUP_NAME,TECHONOLOGY_AREA FROM APP_DETAILS1 WHERE IS_ACTIVE=1 ORDER BY APP_ID ASC";
		try (Connection connection = DashDBUtil.connectDashDB();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(landingPageAppQuery)) {
			while (resultSet.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("appId", resultSet.getInt("APP_ID"));
				jsonObject.put("appName", resultSet.getString("APP_NAME"));
				jsonObject.put("optGrp", resultSet.getString("OPERATING_GROUP_NAME"));
				jsonObject.put("technologyArea", resultSet.getString("TECHONOLOGY_AREA"));
				appJsonArray.put(jsonObject);
			}
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return appJsonArray;
	}
	
	public JSONArray getApplicationDetailsBasedOnID(int appId, ServletContext context) {
		JSONArray appJsonArray = new JSONArray();
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			connection = DashDBUtil.connectDashDB();
			String landingPageAppQuery = "SELECT * FROM APP_DETAILS1 WHERE IS_ACTIVE=1 AND APP_ID = ?";
			statement = connection.prepareStatement(landingPageAppQuery);
			statement.setInt(1, appId);
			resultSet = statement.executeQuery();
			String dirPath = createImageDirectory(context);
			while (resultSet.next()) {
				String imagePath = null;
				if (null != resultSet.getBinaryStream("BINARY_IMAGE")) {
					String imageExt = ImageType.getImageType(resultSet.getString("IMAGE_TYPE").toLowerCase());
					Path filePath = Paths.get(dirPath + File.separator + resultSet.getInt("APP_ID") + imageExt);
					//if (!Files.exists(filePath)) {
						Files.copy(resultSet.getBinaryStream("BINARY_IMAGE"), filePath, StandardCopyOption.REPLACE_EXISTING);
					//}
					imagePath = DemoCatalogConstants.IMG_DIR_NAME + File.separator + resultSet.getInt("APP_ID") + imageExt;
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("description", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("challenge", resultSet.getString("CHALLENGE"));
				jsonObject.put("solution", resultSet.getString("SOLUTION"));
				jsonObject.put("delivery", resultSet.getString("DELIVERY"));
				jsonObject.put("docURL", resultSet.getString("DOCUMENT_URL"));
				jsonObject.put("videoURL", resultSet.getString("VIDEO_URL"));
				jsonObject.put("appURL", resultSet.getString("APP_URL"));
				jsonObject.put("appName", resultSet.getString("APP_NAME"));
				jsonObject.put("technologyArea", resultSet.getString("TECHONOLOGY_AREA"));
				jsonObject.put("image", imagePath);
				jsonObject.put("appCred", resultSet.getString("APP_INSTRUCTIONS"));
				jsonObject.put("shortDesc", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("priority", resultSet.getInt("PRIORITY"));
				jsonObject.put("opGrp", resultSet.getString("OPERATING_GROUP_NAME"));
				appJsonArray.put(jsonObject);
			}
		} catch (JSONException | SQLException | IOException e) {
			e.printStackTrace();
		}
		return appJsonArray;
	}
	
	public JSONArray getAllApplicationDetails( ServletContext context) {
		JSONArray appJsonArray = new JSONArray();
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			connection = DashDBUtil.connectDashDB();
			String landingPageAppQuery = "SELECT * FROM APP_DETAILS1 WHERE IS_ACTIVE=1";
			statement = connection.prepareStatement(landingPageAppQuery);
			//statement.setInt(1, appId);
			resultSet = statement.executeQuery();
			String dirPath = createImageDirectory(context);
			while (resultSet.next()) {
				String imagePath = null;
				if (null != resultSet.getBinaryStream("BINARY_IMAGE")) {
					String imageExt = ImageType.getImageType(resultSet.getString("IMAGE_TYPE").toLowerCase());
					Path filePath = Paths.get(dirPath + File.separator + resultSet.getInt("APP_ID") + imageExt);
					//if (!Files.exists(filePath)) {
						Files.copy(resultSet.getBinaryStream("BINARY_IMAGE"), filePath, StandardCopyOption.REPLACE_EXISTING);
					//}
					imagePath = DemoCatalogConstants.IMG_DIR_NAME + File.separator + resultSet.getInt("APP_ID") + imageExt;
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("description", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("challenge", resultSet.getString("CHALLENGE"));
				jsonObject.put("solution", resultSet.getString("SOLUTION"));
				jsonObject.put("delivery", resultSet.getString("DELIVERY"));
				jsonObject.put("docURL", resultSet.getString("DOCUMENT_URL"));
				jsonObject.put("videoURL", resultSet.getString("VIDEO_URL"));
				jsonObject.put("appURL", resultSet.getString("APP_URL"));
				jsonObject.put("appName", resultSet.getString("APP_NAME"));
				jsonObject.put("technologyArea", resultSet.getString("TECHONOLOGY_AREA"));
				jsonObject.put("image", imagePath);
				jsonObject.put("appCred", resultSet.getString("APP_INSTRUCTIONS"));
				jsonObject.put("shortDesc", resultSet.getString("SHORT_DESCRIPTION"));
				jsonObject.put("priority", resultSet.getInt("PRIORITY"));
				jsonObject.put("opGrp", resultSet.getString("OPERATING_GROUP_NAME"));
				appJsonArray.put(jsonObject);
			}
		} catch (JSONException | SQLException | IOException e) {
			e.printStackTrace();
		}
		return appJsonArray;
	}
}
