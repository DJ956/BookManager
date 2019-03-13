package bookDataBase;

import java.sql.SQLException;

import book.Book;

public class InsertBook extends AbstractBookDatabase<Void> {

	private Book book;
	
	public InsertBook(Book book) {
		this.book = book;
	}
	
	@Override
	void process() throws SQLException {
		String query = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)",
				TABLE_NAME,
				TITLE,
				AUTHOR,
				READ,
				IMAGE_URL,
				RELEASE_DAY,
				ISBN);
		
		preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, book.getTitle());
		preparedStatement.setString(2, book.getAuthor());
		preparedStatement.setBoolean(3, book.read());
		
		if(book.getImageURL() != null){
			preparedStatement.setURL(4, book.getImageURL());
		}else{
			preparedStatement.setURL(4, null);
		}
		
		if(book.getReleaseDay() != null){
			preparedStatement.setString(5, book.getReleaseDay().toString());
		}else{
			preparedStatement.setString(5, null);
		}
		
		if(book.getISBN() != null){
			preparedStatement.setLong(6, book.getISBN());
		}else{
			preparedStatement.setLong(6, -1);
		}
		
		preparedStatement.executeUpdate();
	}

}
