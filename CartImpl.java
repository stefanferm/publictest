package main.javacode.bookstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import main.javacode.bookstore.entity.Book;

public class CartImpl implements Cart {
	private List<Book> books = new ArrayList<Book>();

	@Override
	public boolean addToCart(Book book) {

		return books.add(book);
	}

	@Override
	public boolean removeFromCart(Book book) {
		return books.remove(book);

	}

	@Override
	public BigDecimal getTotal() {
		// TODO Auto-generated method stub
		BigDecimal total = new BigDecimal(0);
		for (Book book : books) {
			total = total.add(book.getPrice());
		}
		return total;
	}

	@Override
	public Book[] getCart() {
		return books.toArray(new Book[books.size()]);
	}

	@Override
	public void clearList() {
		// TODO Auto-generated method stub
		 books.clear();
	}

}
