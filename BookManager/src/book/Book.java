package book;

import java.net.URL;

public class Book implements Item {

	private String title = "";
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		if(title != null && !title.isEmpty()){
			this.title = title;
		}
	}

	private String author = "";
	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public void setAuthor(String author) {
		this.author = author;
	}

	private boolean read = false;
	@Override
	public boolean read() {
		return read;
	}

	@Override
	public void isRead(boolean read) {
		this.read = read;
	}

	private URL imageURL = null;
	@Override
	public URL getImageURL() {
		return imageURL;
	}

	@Override
	public void setImageURL(URL imageURL) {
		this.imageURL = imageURL;
	}

	private String releaseDay = "";
	@Override
	public String getReleaseDay() {
		return releaseDay;
	}

	@Override
	public void setReleaseDay(String releaseDay) {
		this.releaseDay = releaseDay;
	}

	private long isbn = 0L;
	@Override
	public Long getISBN(){
		return isbn;
	}
	
	@Override
	public void setISBN(Long isbn) {
		this.isbn = isbn;
	}
	
	private int id = -1;
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public void setId(int id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return String.format("Title:%s  Author:%s  Read:%b  ImageURL:%s  ReleaseDay:%s  Isbn:%s  ID:%d",
				getTitle(),
				getAuthor(),
				read(),
				getImageURL(),
				getReleaseDay(),
				getISBN(),
				getId());
	}
}
