package main.javacode.bookstore.service;

import main.javacode.bookstore.entity.Book;

public interface BookList {
	public Book[] list(String searchString) throws Exception;

	public boolean add(Book book, int amount);

	public int[] buy(Book... books);
}
