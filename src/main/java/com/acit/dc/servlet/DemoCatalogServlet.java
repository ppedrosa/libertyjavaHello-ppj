package com.acit.dc.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.acit.dc.service.DemoCatalogService;
import com.acit.dc.util.DashDBUtil;
import com.acit.dc.util.DemoCatalogConstants;

/**
 * Servlet implementation class for Demo Catalog App
 */
@WebServlet("/DemoCatalogServlet")
public class DemoCatalogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	JSONArray sliderJsonArray = new JSONArray();
    	JSONArray appJsonArray = new JSONArray();
    	JSONObject landingPageJsonObject = new JSONObject();
    	DemoCatalogService demoCatalogService = new DemoCatalogService();
    	try {
    		sliderJsonArray = demoCatalogService.createSliderJSONArray(getServletContext());
    		appJsonArray = demoCatalogService.createLandingPageAppsJSONArray(getServletContext());
    		landingPageJsonObject.put("slider", sliderJsonArray);
    		landingPageJsonObject.put("appDetails", appJsonArray);
			
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
    	response.setContentType(DemoCatalogConstants.CONTENT_TYPE_JSON);
        response.getWriter().print(landingPageJsonObject);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String operatingGroup = URLDecoder.decode(req.getParameter("catalog"), "UTF-8");
    	JSONObject appDetails = new JSONObject();
    	DemoCatalogService demoCatalogService = new DemoCatalogService();
    	JSONArray app;
		try {
			app = demoCatalogService.getAppDetailsBasedOnOpertingGroup(operatingGroup, getServletContext());
			appDetails.put("appDetails", app);
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		resp.setContentType(DemoCatalogConstants.CONTENT_TYPE_JSON);
		resp.getWriter().print(appDetails);
    }
     /*@Override
    public void init() throws ServletException {
    	 Connection connection = DashDBUtil.connectDashDB();
    	 try {
    		 	     String query2="UPDATE SLIDER1 set IMAGE_TYPE= ?";
    		 	     PreparedStatement ps2 = connection.prepareStatement(query2);
    		 	     ps2.setString(1, "image/jpeg");
    		 	     ps2.executeUpdate();
    		 	     System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~table record updated");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    }*/
}
