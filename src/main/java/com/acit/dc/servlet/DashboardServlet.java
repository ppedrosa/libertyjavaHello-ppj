package com.acit.dc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.acit.dc.service.DashboardService;
import com.acit.dc.service.DemoCatalogService;
import com.acit.dc.util.DemoCatalogConstants;

/**
 * Servlet implementation class for adding new application in Demo Catalog.
 */
@WebServlet("/DashboardServlet")
@MultipartConfig
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashboardServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		DashboardService dashboardService = new DashboardService();
		String appId = request.getParameter("appId");
		int rows = 0;
		try {
			if (null != appId) {
				rows = dashboardService.updateApplication(request, appId);
			} else {
				rows = dashboardService.addNewApplication(request);
			}
			if (rows > 0) {
				response.sendRedirect(getServletContext().getContextPath() + "/#/demo-catalog-admin-console");
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
    	JSONArray appJsonArray = new JSONArray();
    	JSONObject landingPageJsonObject = new JSONObject();
    	DemoCatalogService demoCatalogService = new DemoCatalogService();
    	String appId = request.getParameter("appId");
    	try {
    		if (null != appId) {
    			appJsonArray = demoCatalogService.getApplicationDetailsBasedOnID(Integer.parseInt(appId), getServletContext());
    		} else {
    			appJsonArray = demoCatalogService.getApplicationDetails();    			
    		}
    		landingPageJsonObject.put("appDetails", appJsonArray);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	response.setContentType(DemoCatalogConstants.CONTENT_TYPE_JSON);
        response.getWriter().print(landingPageJsonObject);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String appId = req.getParameter("appId");
		DashboardService dashboardService = new DashboardService();
		if (null != appId) {
			int delete = dashboardService.deleteApp(appId);
			if (delete > 0) {
				resp.getWriter().write("success");
			} else {
				resp.getWriter().write("error");
			}
		} else {
			resp.getWriter().write("Invalid app id");
		}
	}

}
