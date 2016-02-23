package main.javacode.bookstore;

import java.math.BigDecimal;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import main.javacode.bookstore.entity.Book;
import main.javacode.bookstore.service.BookList;
import main.javacode.bookstore.service.BooklistImpl;
import main.javacode.bookstore.service.Cart;
import main.javacode.bookstore.service.CartImpl;

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		BookList bookList = new BooklistImpl();
		Cart cart = new CartImpl();
		Book[] books;
		Book book;
		int action = 0, step = 0;
		String title = "", author = "", price = "";
		showMenu();
		while (true) {
			String str = scanner.nextLine();
			if (str.equalsIgnoreCase("menu")) {
				showMenu();
				action = 0;
				step = 0;
			}

			if (str.equalsIgnoreCase("add")) {
				action = 1;
				step = 0;
			} else if (str.equalsIgnoreCase("remove")) {
				action = 2;
				step = 0;
			} else if (str.equalsIgnoreCase("buy")) {
				action = 3;
			} else if (str.equalsIgnoreCase("add to cart")) {
				step = 0;
				action = 4;
			} else if (str.equalsIgnoreCase("cart")) {
				action = 5;
			}
			switch (action) {
			case 1:

				if (step == 0) {
					System.out.println("Enter title");
					System.out.println();
				} else if (step == 1) {
					title = str;
					System.out.println("Enter author");
					System.out.println();
				} else if (step == 2) {
					author = str;
					System.out.println("Enter price ex 45.8");

				} else if (step == 3) {
					price = str;
					System.out.println("Enter amount");
				} else {
					int amount = 0;
					try {
						amount = Integer.parseInt(str);

						if (bookList.add(new Book(title, author, new BigDecimal(price)), amount)) {
							System.out.println("Books added");
							action =0;
						} else {
							System.out.println("Books not added");
						}
					} catch (NumberFormatException ne) {
						System.out.println("Number format wrong " + ne.getMessage());

					}

				}
				step++;
				break;
			case 2:

				if (step == 0) {
					System.out.println("Enter title");
					System.out.println();
				} else if (step == 1) {
					title = str;
					System.out.println("Enter author");
					System.out.println();
				} else {
					try {
						books = cart.getCart();
						for (Book b : books) {
							if (b.getTitle() != null && b.getTitle().equalsIgnoreCase(title) && b.getAuthor() != null
									&& b.getAuthor().equalsIgnoreCase(author)) {
								if (cart.removeFromCart(b)) {
									System.out.println("Book removed from cart");
									action =0;
								} else {
									System.out.println("Book not found in cart");
								}
							}
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
				}
				step++;
				break;
			case 3:
				books = cart.getCart();
				int[] buy = bookList.buy(books);
				for (int i = 0; i < books.length; i++) {
					System.out.println("Book :" + books[i].getTitle() + " " + Status.getStatus(buy[i]));
				}
				System.out.println("Totalt : " + cart.getTotal());
				cart.clearList();
				action =0;

				break;
			case 4:

				if (step == 0) {
					System.out.println("Enter title");
					System.out.println();
				} else if (step == 1) {
					title = str;
					System.out.println("Enter author");
					System.out.println();
				} else {
					author = str;
					try {
						book = foundBook(title, author, bookList.list("all"));
						if (cart.addToCart(book)) {
							System.out.println("Book " + title + " add to cart");
							action =0;
							
						} else {
							System.out.println("Book " + title + " not add to cart");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());

					}

				}
				step++;
				break;
			case 5:
				books = cart.getCart();
				for (Book b : books) {
					System.out.println(
							"Title : " + b.getTitle() + ", Author : " + b.getAuthor() + ", Price : " + b.getPrice());
				}
				System.out.println("Total : "+ cart.getTotal());
				action = 0;

				break;
			default:
				if (!str.equalsIgnoreCase("menu")) {
					try {
						books = bookList.list(str);
						for (Book b : books) {
							System.out.println("Title : " + b.getTitle() + ", Author : " + b.getAuthor() + ", Price : "
									+ b.getPrice());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				}
				break;
			}

			if ("exit".equalsIgnoreCase(str)) {
				System.out.println("Bye!");
				break;
			}
		}
		scanner.close();

	}

	private static Book foundBook(String title, String author, Book[] books) throws Exception {
		// trace begin

		try {

			BigDecimal dm = null;
			for (Book b : books) {
				if (b.getAuthor() != null && b.getAuthor().equalsIgnoreCase(author) && b.getTitle() != null
						&& b.getTitle().equalsIgnoreCase(title)) {
					dm = b.getPrice();
					break;
				}
			}
			if (dm == null) {
				dm = new BigDecimal(0);
			}
			return new Book(title, author, dm);
		} catch (Exception e) {
			// log stack and message
			throw new Exception(e.getMessage());
		} finally {
			// trace end
		}
	}

	private static void showMenu() {
		System.out.println("Enter (exit) to quit");
		System.out.println("Enter (title) or (Author) to search for book ex  How To Spend Money");
		System.out.println("Enter (all) to se all books");
		System.out.println("Enter (add) to add a new book");
		System.out.println("Enter (add to cart) to add bok to cart");
		System.out.println("Enter remove to remove from cart");
		System.out.println("Enter cart to see cart");
		System.out.println("Enter buy to buy all from cart");
		System.out.println("Enter menu to see menu again");
		System.out.println();
	}

	public enum Status {
		OK(0, "Passed", "The book was in stock."), NOT_IN_STOCK(1, "Sold out",
				"The book was sold out."), DOES_NOT_EXIST(2, "Did not exist", "Book did never exist.");

		private int code;
		private String label;
		private String description;

		private static Map<Integer, Status> codeToStatusMapping;

		private Status(int code, String label, String description) {
			this.code = code;
			this.label = label;
			this.description = description;
		}

		public static Status getStatus(int i) {
			if (codeToStatusMapping == null) {
				initMapping();
			}
			return codeToStatusMapping.get(i);
		}

		private static void initMapping() {
			codeToStatusMapping = new HashMap<Integer, Status>();
			for (Status s : values()) {
				codeToStatusMapping.put(s.code, s);
			}
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Status");
			sb.append("{code=").append(code);
			sb.append(", label='").append(label).append('\'');
			sb.append(", description='").append(description).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}

}
