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

import com.acit.dc.service.AuthorizationService;
import com.acit.dc.service.DemoCatalogService;
import com.acit.dc.util.DemoCatalogConstants;

/**
 * Servlet implementation class for adding new application in Demo Catalog.
 */
@WebServlet("/AuthorizationServlet")
@MultipartConfig
public class AuthorizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthorizationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
		AuthorizationService authorizationService = new AuthorizationService();
		String username = request.getParameter("username");
		boolean authFlag = false;
		try {
			if (null != username) {
				authFlag = authorizationService.authorizeUser(username);
			}
			System.out.println("Authorization Success"+username);
			if (authFlag ==true) {
				response.getWriter().write("Authorization Successful");
				//response.sendRedirect(getServletContext().getContextPath() + "/#/demo-catalog-dashboard");
			} else {
				response.getWriter().write("Authorization Failure");
			}
		} catch (IOException exe) {
			try {
				exe.printStackTrace();
				response.getWriter().write("Error while authorization");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
}
