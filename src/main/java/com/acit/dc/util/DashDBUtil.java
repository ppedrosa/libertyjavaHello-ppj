package com.acit.dc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DashDBUtil {


	private DashDBUtil() {
	}

	/**
	 * This method establishes a connection with dashDB Service and returns the
	 * database.
	 * 
	 * @return DATABASE
	 */
	public static Connection connectDashDB() {
		Connection dbConnection = null;;
			String VCAP_SERVICES = System.getenv(DemoCatalogConstants.VCAP_SERVICES);
			if (VCAP_SERVICES != null) {
				try {
					JSONObject vcap = new JSONObject(VCAP_SERVICES);
					JSONArray arrVcap = (JSONArray) vcap.get("dashDB");
					JSONObject dashDBInstance = (JSONObject) arrVcap.get(0);
					JSONObject dashDBCredentials = (JSONObject) dashDBInstance.get("credentials");
					String dbURL = dashDBCredentials.getString("jdbcurl");
					String username = dashDBCredentials.getString("username");
					String password = dashDBCredentials.getString("password");
					Class.forName(DemoCatalogConstants.DASH_DB_DRIVER);
					dbConnection = DriverManager.getConnection(dbURL, username, password);
					System.out.println("======================" + dbURL);
					System.out.println("=======================" + username);
					System.out.println("===========================" + password);
				} catch (ClassNotFoundException | SQLException | JSONException e) {
					e.printStackTrace();
				}

			}
		return dbConnection;
	}

}
