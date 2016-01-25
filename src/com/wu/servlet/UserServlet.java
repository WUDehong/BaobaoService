package com.wu.servlet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wu.entity.User;
import com.wu.service.UserDBService;


public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String operation = request.getParameter("operation");
		String result = "faile";
		UserDBService us = new UserDBService();

		if (operation.equalsIgnoreCase("checkUser")) {
			// check user legal
			User user = new User();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			if (us.checkUser(user)) {
				result = "success";
			}
		} else if (operation.equalsIgnoreCase("addUser")) {
			// user registe
			User user = new User();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			if (!us.existsEmail(user.getEmail())) {
				us.addUser(user);
				result = "success";
				// 为用户增加默认头像
				String imagePath = getServletContext().getRealPath("/upload")+"/";
				addDefaultHeadImage(user.getEmail(),imagePath);
			}
		} else if (operation.equalsIgnoreCase("changePassword")) {
			User user = new User();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			if (us.changePassword(user)) {
				result = "success";
			}
		}else if(operation.equalsIgnoreCase("getBalance")){
			String email = request.getParameter("email");
			result = us.getBalance(email)+"";
		}
		System.out.println("operation--->" + operation);
		System.out.println("result---->" + result);
		// not to use println
		pw.print(result);
		pw.flush();
		pw.close();
	}

	/**
	 * @param email
	 */
	private void addDefaultHeadImage(String email,String imagePath) {
		try {
			FileOutputStream fos = new FileOutputStream(imagePath+email + ".jpeg");
			FileInputStream fis =new FileInputStream(imagePath+"default.jpg");
			byte [] buffer = new byte[4*1024];
			int len = 0;
			while((len=fis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
