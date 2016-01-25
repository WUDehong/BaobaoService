package com.wu.entity;

import java.io.Serializable;

public class Cart implements Serializable{

	private int cartId;
	private Book book;
	private int num;
	private boolean isChecked;
	
	public Cart(){}

	public Cart(int cartId, Book book, int num, boolean isChecked) {
		this.cartId = cartId;
		this.book = book;
		this.num = num;
		this.isChecked = isChecked;
	}


	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", book=" + book + ", num=" + num
				+ ", isChecked=" + isChecked + "]";
	}

	
	
}
