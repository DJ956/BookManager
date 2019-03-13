package book;

import java.net.URL;

public interface Item {
	String getTitle();
	void setTitle(String title);
	
	String getAuthor();
	void setAuthor(String author);
	
	boolean read();
	void isRead(boolean read);
	
	URL getImageURL();
	void setImageURL(URL imageURL);
	
	String getReleaseDay();
	void setReleaseDay(String releaseDay);
	
	Long getISBN();
	void setISBN(Long isbn);
	
	int getId();
	void setId(int id);
}
