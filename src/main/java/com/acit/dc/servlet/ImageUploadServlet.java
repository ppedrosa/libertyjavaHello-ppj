package com.acit.dc.servlet;

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
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.acit.dc.service.DashboardService;
import com.acit.dc.service.DemoCatalogService;
import com.acit.dc.util.DashDBUtil;
import com.acit.dc.util.DemoCatalogConstants;

/**
 * Servlet implementation class for adding new application in Demo Catalog.
 */
@WebServlet("/ImageUploadServlet")
@MultipartConfig
public class ImageUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageUploadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		//DashboardService dashboardService = new DashboardService();
		String appId = request.getParameter("sliderId");
		System.out.println("sliderID"+appId);
		int rows = 0;
		try {
			if (null != appId) {
				rows = updateSliderImage(request, appId);
			}
			
			if (rows > 0) {
				response.getWriter().write("Image Uploaded Success for ID::"+appId);
			} else {
				response.getWriter().write("Error while adding new application");
			}
		} catch (ServletException | IOException exe) {
			try {
				exe.printStackTrace();
				response.getWriter().write("Error while adding new application");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
	}
	
	public int updateSliderImage(HttpServletRequest request, String appId) throws IOException, ServletException {
		
		final Part filePart = request.getPart("image");
		long filePartSize = filePart.getSize();
		
		Connection connection = DashDBUtil.connectDashDB();
		PreparedStatement preparedStatement = null;		
		int rows = 0;		
		try {
				preparedStatement = connection.prepareStatement("UPDATE SLIDER1 SET IMAGE=?, IMAGE_TYPE='image/png' "						
						+ " WHERE ID = ?");
			preparedStatement.setBinaryStream(1, null, 0);
			if (null != filePart && filePartSize > 0) {
				System.out.println("image upload");
				//preparedStatement.setString(13, filePart.getContentType());
				preparedStatement.setBinaryStream(1, filePart.getInputStream(), filePart.getInputStream().available());
			}			
			preparedStatement.setInt(2, Integer.parseInt(appId));
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
	

}
