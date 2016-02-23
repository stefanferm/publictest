package main.javacode.bookstore.service;

import java.math.BigDecimal;

import main.javacode.bookstore.entity.Book;

public interface Cart {

	public boolean addToCart(Book book);

	public boolean removeFromCart(Book book);
	public void clearList();

	public BigDecimal getTotal();
	
	public Book[] getCart();
}
