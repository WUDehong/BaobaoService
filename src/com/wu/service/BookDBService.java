package com.wu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.wu.entity.Book;
import com.wu.entity.Cart;
import com.wu.entity.User;

public class BookDBService {

	private Connection connection = null;

	public BookDBService() {
		try {
			Context ic = new InitialContext();
			DataSource source = (DataSource) ic
					.lookup("java:comp/env/jdbc/shopping");
			connection = source.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertBookToDB(Book book) {
		PreparedStatement ps = null;
		try {
			ps = connection
					.prepareCall("insert into book(id,name,price,description,imageUrl,fileUrl,author) values (?,?,?,?,?,?,?)");
			ps.setInt(1, book.getId());
			ps.setString(2, book.getName());
			ps.setDouble(3, book.getPrice());
			ps.setString(4, book.getDescription());
			ps.setString(5, book.getImageUrl());
			ps.setString(6, book.getFileUrl());
			ps.setString(7, book.getAuthor());
			System.out.println(book.getName());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<Book> getBookList(int start, int pageSize) {
		PreparedStatement ps = null;
		List<Book> bookList = new ArrayList<Book>();
		try {
			ps = connection.prepareCall("select * from book limit ?,?");
			ps.setInt(1, start);
			ps.setInt(2, pageSize);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book(rs.getInt("id"), rs.getString("name"),
						rs.getDouble("price"), rs.getString("description"),
						rs.getString("imageUrl"), rs.getString("fileUrl"),
						rs.getString("author"));
				bookList.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bookList;
	}

	/**
	 * add the book to user's cart but set num+1 while cart exists this book
	 * 
	 * @param email
	 * @param id
	 * @return
	 */
	public boolean addBook(String email, String id) {
		boolean flag = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection
					.prepareCall("select count(*) from cart where email=? and id=?");
			ps.setString(1, email);
			ps.setInt(2, Integer.parseInt(id));
			rs = ps.executeQuery();
			rs.next();
			if (rs.getInt(1) != 0) {
				ps = connection
						.prepareCall("update cart set num=num+1 where email=? and id=?");
				ps.setString(1, email);
				ps.setInt(2, Integer.parseInt(id));
				ps.executeUpdate();
				flag = true;
			} else {
				ps = connection
						.prepareCall("insert into cart(email,id,num) values(?,?,?)");
				ps.setString(1, email);
				ps.setInt(2, Integer.parseInt(id));
				ps.setInt(3, 1);
				ps.executeUpdate();
				flag = true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * decrease user's cart book num and return true if success
	 * 
	 * @param email
	 * @param id
	 * @return
	 */
	public boolean minbook(String email, String id) {
		try {
			PreparedStatement ps = connection
					.prepareCall("select * from cart where email=? and id=? and num>1");
			ps.setString(1, email);
			ps.setInt(2, Integer.parseInt(id));
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ps = connection
						.prepareCall("update cart set num=num-1 where email=? and id=?");
				ps.setString(1, email);
				ps.setInt(2, Integer.parseInt(id));
				ps.executeUpdate();
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * set the user's cart book num and return true while success
	 * 
	 * @param email
	 * @param id
	 * @param num
	 * @return
	 */
	public boolean setNum(String email, String id, String num) {
		boolean flag = false;
		try {
			PreparedStatement ps = connection
					.prepareCall("update cart set num= ? where email=? and id=?");
			ps.setInt(1, Integer.parseInt(num));
			ps.setString(2, email);
			ps.setInt(3, Integer.parseInt(id));
			ps.executeUpdate();
			flag = true;
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * delete the user's cart book
	 * 
	 * @param email
	 * @param id
	 * @return
	 */
	public boolean deletebook(String email, String id) {
		boolean flag = false;
		try {
			PreparedStatement ps = connection
					.prepareCall("delete from cart where email=? and id=?");
			ps.setString(1, email);
			ps.setInt(2, Integer.parseInt(id));
			ps.executeUpdate();
			flag = true;
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * return all books info
	 * 
	 * @return
	 */
	public String getbook() {
		String result = "";
		try {
			PreparedStatement ps = connection.prepareCall("select * from book");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = result + "{id:" + rs.getInt("id") + ",name:\""
						+ rs.getString("name") + "\"," + "price:"
						+ rs.getDouble("price") + ",description:\""
						+ rs.getString("description") + "\",imageurl:\""
						+ rs.getString("imageurl") + "\"};";
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Return user's cart and return null when cart is empty
	 * 
	 * @return
	 */
	public List<Cart> getCart(String email) {
		List<Cart> cartList = new ArrayList<Cart>();
		try {
			PreparedStatement ps = connection
					.prepareCall("select cart.*,book.* from cart,book  where cart.id=book.id and email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int num = rs.getInt("num");
				int cartId = rs.getInt("cartId");
				boolean isChecked = rs.getBoolean("isChecked");
				Book book = new Book();
				book.setAuthor(rs.getString("author"));
				book.setDescription(rs.getString("description"));
				book.setFileUrl(rs.getString("fileUrl"));
				book.setId(rs.getInt("id"));
				book.setImageUrl(rs.getString("imageUrl"));
				book.setName(rs.getString("name"));
				book.setPrice(rs.getDouble("price"));
				Cart cart = new Cart();
				cart.setBook(book);
				cart.setNum(num);
				cart.setCartId(cartId);
				cart.setChecked(isChecked);
				cartList.add(cart);
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartList;
	}

	/**
	 * delete user's cart
	 * 
	 * @param email
	 */
	public boolean deleteCart(String email) {
		boolean flag = false;
		try {
			PreparedStatement ps = connection
					.prepareCall("delete from cart where email=?");
			ps.setString(1, email);
			ps.executeUpdate();
			flag = true;
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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
			flag = true;
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean minBook(String email, String id) {
		try {
			PreparedStatement ps = connection
					.prepareCall("select * from cart where email=? and id=? and num>0");
			ps.setString(1, email);
			ps.setInt(2, Integer.parseInt(id));
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ps = connection
						.prepareCall("update cart set num=num-1 where email=? and id=?");
				ps.setString(1, email);
				ps.setInt(2, Integer.parseInt(id));
				ps.executeUpdate();
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String getProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateChecked(String email, String cartId, String isChecked) {
		boolean checked = false;
		if (isChecked.equalsIgnoreCase("true")) {
			checked = true;
		}
		try {
			PreparedStatement ps = connection
					.prepareCall("update cart set isChecked=? where email=? and cartId=?");
			ps.setBoolean(1, checked);
			ps.setString(2, email);
			ps.setInt(3, Integer.parseInt(cartId));
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateChecked(String email, String isChecked) {
		boolean checked = false;
		if (isChecked.equalsIgnoreCase("true")) {
			checked = true;
		}
		try {
			PreparedStatement ps = connection
					.prepareCall("update cart set isChecked=? where email=?");
			ps.setBoolean(1, checked);
			ps.setString(2, email);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Cart> getPayList(String email) {
		List<Cart> cartList = new ArrayList<Cart>();
		try {
			PreparedStatement ps = connection
					.prepareCall("select cart.*,book.* from cart,book  where cart.id=book.id and email=? and isChecked=true");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int num = rs.getInt("num");
				int cartId = rs.getInt("cartId");
				boolean isChecked = rs.getBoolean("isChecked");
				Book book = new Book();
				book.setAuthor(rs.getString("author"));
				book.setDescription(rs.getString("description"));
				book.setFileUrl(rs.getString("fileUrl"));
				book.setId(rs.getInt("id"));
				book.setImageUrl(rs.getString("imageUrl"));
				book.setName(rs.getString("name"));
				book.setPrice(rs.getDouble("price"));
				Cart cart = new Cart();
				cart.setBook(book);
				cart.setNum(num);
				cart.setCartId(cartId);
				cart.setChecked(isChecked);
				cartList.add(cart);
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartList;
	}

	public String pay(String email) {
		String result = "购买失败";
		try {
			PreparedStatement ps = connection
					.prepareCall("select sum(c.num*b.price) pay from book b,cart c where c.id=b.id and c.email=? and c.isChecked=true");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				double pay = rs.getDouble("pay");
				ps = connection
						.prepareCall("select balance from user where email=?");
				ps.setString(1, email);
				ResultSet rs2 = ps.executeQuery();
				rs2.next();
				double balance = rs2.getDouble("balance");
				System.out.println("pay:" + pay + ",balance:" + balance);
				if (balance < pay) {
					result = "余额不足";
				} else {
					List<Integer> idList = new ArrayList<Integer>();
					ps = connection
							.prepareCall("select id from cart where email=? and isChecked=true");
					ps.setString(1, email);
					ResultSet rs1 = ps.executeQuery();
					while (rs1.next()) {
						idList.add(rs1.getInt("id"));
					}
					connection.setAutoCommit(false);
					try {
						PreparedStatement p0 = null;
						for (Integer i : idList) {
							p0 = connection
									.prepareStatement("insert into assets (email,id) values (?,?)");
							p0.setInt(2, i);
							p0.setString(1, email);
							p0.executeUpdate();
						}
						p0 = connection
								.prepareStatement("delete from cart where email=? and isChecked=true;");
						p0.setString(1, email);
						p0.executeUpdate();
						p0 = connection
								.prepareStatement("update user set balance=balance-? where email=?");
						p0.setString(2, email);
						p0.setDouble(1, pay);
						p0.executeUpdate();
						connection.commit();
						p0.close();
						result = "购买成功";
					} catch (Exception e) {
						connection.rollback();
					}
					connection.setAutoCommit(true);
				}
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
