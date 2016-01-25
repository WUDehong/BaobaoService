/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WDH
 * @Date 2016-1-15
 * 
 */
public class GetBookImageServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/png;image/jpeg;charset=UTF-8");
		String imagePath = getServletContext().getRealPath("/upload")+"/"+request.getParameter("path");
		System.out.println("imagePath:"+imagePath);
		FileInputStream fis = new FileInputStream(imagePath);
		int lenght=fis.available();
		byte[] buffer = new byte[lenght];
		OutputStream os = response.getOutputStream();
		fis.read(buffer);
		os.write(buffer,0,lenght);
		os.flush();
		os.close();
	}

}
