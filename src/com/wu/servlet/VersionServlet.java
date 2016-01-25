/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wu.entity.Version;

/**
 * @author WDH
 * @Date 2016-1-4
 * 
 */
public class VersionServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		String versionPath = getServletContext().getRealPath("WEB-INF/classes/version.properties");
		FileInputStream fi = new FileInputStream(versionPath);
		Properties p = new Properties();
		p.load(fi);
		int versionCode = Integer.parseInt(p.getProperty("versionCode"));
		String versionName = p.getProperty("versionName");
		String versionDescription = p.getProperty("versionDescription");
		Version version = new Version(versionCode, versionName, versionDescription);
		Gson gson = new Gson();
		String versionJson = gson.toJson(version);
		printWriter.write(versionJson);
		printWriter.flush();
		printWriter.close();
	}
}
