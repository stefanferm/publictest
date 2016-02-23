package main.javacode.bookstore.entity;

import java.math.BigDecimal;

public class Book {

	private String title;
	private String author;
	private BigDecimal price;

	public Book(String title, String author, BigDecimal price) {
		super();
		this.setTitle(title);
		this.setAuthor(author);
		this.setPrice(price);
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
