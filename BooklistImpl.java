package main.javacode.bookstore.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import main.javacode.bookstore.entity.Book;

public class BooklistImpl implements BookList {

	private final static String Book_Path = "http://www.contribe.se/bookstoredata/bookstoredata.txt";

	private static ArrayList<Book> bookList;
	private static ArrayList<Book> soldBookList;
	
	public BooklistImpl(){
		if(bookList == null){
			bookList = initialize();
		}
		if(soldBookList == null){
			soldBookList = new ArrayList<Book>();
		}
	}

	private static ArrayList<Book> initialize() {

		BufferedReader in = null;
		ArrayList<Book> temp = new ArrayList<Book>();
		try {
			URL url = new URL(Book_Path);
			in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				String[] bookArray = inputLine.split(";");

				int quantity = 0;
				try {
					quantity = Integer.parseInt(bookArray[3]);
					for (int i = 0; i < quantity; i++) {
						temp.add(
								new Book(bookArray[0], bookArray[1], new BigDecimal(bookArray[2].replaceAll(",", ""))));
					}
				} catch (NumberFormatException ne) {
					// logg exception stack and message
				} catch (Exception ex) {

				}

			}
		} catch (Exception e) {
			// logg exception stack and message

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return temp;
	}

	@Override
	public Book[] list(String searchString) throws Exception {
		// TODO Auto-generated method stub

		if (searchString.equalsIgnoreCase("all")) {
			return bookList.toArray(new Book[bookList.size()]);
		} else {
			ArrayList<Book> searchList = new ArrayList<Book>();
			for (Book book : bookList) {
				if (book.getTitle() != null && book.getTitle().equalsIgnoreCase(searchString)) {
					searchList.add(book);
				} else if (book.getAuthor() != null && book.getAuthor().equalsIgnoreCase(searchString)) {
					searchList.add(book);
				}
			}
			return searchList.toArray(new Book[searchList.size()]);
		}

	}

	@Override
	public boolean add(Book book, int amount) {
		// TODO Auto-generated method stub
		try {
			for (int i = 1; i <= amount; i++) {
				bookList.add(book);
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int[] buy(Book... books) {
		// TODO Auto-generated method stub
		int[] response = new int[books.length];
		int index = 0;
		for (Book b : books) {
			if (b.getAuthor() != null && b.getTitle() != null) {
				System.out.println(b.getTitle());
				boolean found = false;
				for (Book bb : bookList) {
					if (bb.getTitle() != null && bb.getTitle().equalsIgnoreCase(b.getTitle()) && bb.getAuthor() != null
							&& bb.getAuthor().equalsIgnoreCase(b.getAuthor())) {
						System.out.println(bb.getTitle());
						found = true;
						soldBookList.add(b);
						bookList.remove(bb);
						break;
					}
				}
				if (found) {
					response[index] = 0;
					index++;
				} else {
					boolean sold = false;
					for (Book bb : soldBookList) {
						if (bb.getTitle() != null && bb.getTitle().equalsIgnoreCase(b.getTitle())
								&& bb.getAuthor() != null && bb.getAuthor().equalsIgnoreCase(b.getAuthor())) {
							sold = true;
						}
					}
					if (sold) {
						response[index] = 1;
					} else {
						response[index] = 2;
					}
				}

			} else {
				response[index] = 2;
				index++;
			}
		}
		return response;
	}

}
