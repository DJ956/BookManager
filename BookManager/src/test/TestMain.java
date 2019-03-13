package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import book.Book;
import book.BookFactory;
import bookDataBase.InsertBook;

public class TestMain {

	public static void main(String[] args) {
		try{
			System.out.println("Enter isbn...");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			long isbn = Long.parseLong(reader.readLine());
			
			BookFactory factory = BookFactory.getInstance();
			Book book = factory.create(isbn);
			
			book.isRead(false);
			
			InsertBook insertBook = new InsertBook(book);
			insertBook.execute();
			
			System.out.println("Registry");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
