package com.wu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wu.entity.Cart;
import com.wu.entity.User;
import com.wu.service.BookDBService;

public class CartServlet extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html,charset=UTF-8");
		PrintWriter pw = response.getWriter();
		String operation = request.getParameter("operation");
		String result = "faile";
		BookDBService us = new BookDBService();

		 if (operation.equalsIgnoreCase("addBook")) {
			// set user's cart product num+1
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			boolean flag = us.addBook(email, id);
			if (flag) {
				List<Cart> cartList = us.getCart(email);
				Gson gson = new Gson();
				result = gson.toJson(cartList);
			}
		} else if(operation.equalsIgnoreCase("addBookToCart")){
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			boolean flag = us.addBook(email, id);
			if (flag) {
				result = "success";
			}
		} else if (operation.equalsIgnoreCase("getCart")) {
			// return the user's cart info
			String email = request.getParameter("email");
			List<Cart> cartList = us.getCart(email);
			Gson gson = new Gson();
			result = gson.toJson(cartList);
		} else if (operation.equalsIgnoreCase("minusBook")) {
			// set user's cart product num-1
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			boolean flag = us.minBook(email, id);
			if (flag) {
				List<Cart> cartList = us.getCart(email);
				Gson gson = new Gson();
				result = gson.toJson(cartList);
			}
		} else if (operation.equalsIgnoreCase("setNum")) {
			// set user's cart product num=num
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			String num = request.getParameter("num");
			boolean flag = us.setNum(email, id, num);
			if (flag) {
				List<Cart> cartList = us.getCart(email);
				Gson gson = new Gson();
				result = gson.toJson(cartList);
			}
		} else if (operation.equalsIgnoreCase("deleteBook")) {
			// delete product from user's cart
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			boolean flag = us.deletebook(email, id);
			if (flag) {
				List<Cart> cartList = us.getCart(email);
				Gson gson = new Gson();
				result = gson.toJson(cartList);
			}
		} else if (operation.equalsIgnoreCase("deleteCart")) {
			String email = request.getParameter("email");
			if (us.deleteCart(email)) {
				result = "success";
			}
		} else if (operation.equalsIgnoreCase("changePassword")) {
			User user = new User();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			if (us.changePassword(user)) {
				result = "success";
			}
		} else if(operation.equalsIgnoreCase("updateChecked")){
			String email = request.getParameter("email");
			String cartId = request.getParameter("id");
			String isChecked = request.getParameter("num");
			us.updateChecked(email, cartId, isChecked);
			List<Cart> cartList = us.getCart(email);
			Gson gson = new Gson();
			result = gson.toJson(cartList);
		} else if(operation.equalsIgnoreCase("selectAll")){
			String email = request.getParameter("email");
			String isChecked = request.getParameter("num");
			us.updateChecked(email,isChecked);
			List<Cart> cartList = us.getCart(email);
			Gson gson = new Gson();
			result = gson.toJson(cartList);
		} else if(operation.equalsIgnoreCase("getPayList")){
			String email = request.getParameter("email");
			List<Cart> cartList = us.getPayList(email);
			Gson gson = new Gson();
			result = gson.toJson(cartList);
		} else if(operation.equalsIgnoreCase("pay")){
			String email = request.getParameter("email");
			result = us.pay(email);
		}
		System.out.println("operation--->" + operation);
		System.out.println("result---->" + result);
		// not to use println
		pw.print(result);
		pw.flush();
		pw.close();
	}
}
