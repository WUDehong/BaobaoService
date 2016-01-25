/**
 * 
 */
package com.wu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.wu.entity.User;


/**
 * @author wu
 * 
 */
public class UserDBService {

	private Connection connection = null;

	public UserDBService() {
		try {
			Context ic = new InitialContext();
			DataSource source = (DataSource) ic
					.lookup("java:comp/env/jdbc/shopping");
			connection = source.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * return true when the user is legal
	 * 
	 * @param user
	 * @return
	 */
	public boolean checkUser(User user) {
		boolean flag = false;
		try {
			PreparedStatement ps = connection.prepareCall("select * from user where email=? and password=?");
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ResultSet rs = ps.executeQuery();
			flag = rs.next();
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * add user info to database
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		try {
			PreparedStatement ps = connection
					.prepareCall("insert into user(email,password) values(?,?)");
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * return true when email already exists in database
	 * 
	 * @param email
	 * @return
	 */
	public boolean existsEmail(String email) {
		boolean flag = true;
		try {
			PreparedStatement ps = connection
					.prepareCall("select * from user where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			flag = rs.next();
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 
	 * @param user
	 * @return true if update user's password success
	 */
	public boolean changePassword(User user) {
		boolean flag = false;
		try {
			PreparedStatement ps = connection
					.prepareCall("update user set password=? where email=?");
			ps.setString(1, user.getPassword());
			ps.setString(2, user.getEmail());
			ps.executeUpdate();
			ps.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public int getBalance(String email){
		int balance = 0;
		try {
			PreparedStatement ps = connection
					.prepareCall("select balance from user where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			balance = rs.getInt("balance");
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

}
