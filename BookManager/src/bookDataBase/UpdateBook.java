package bookDataBase;

import java.sql.SQLException;

import book.Book;

public class UpdateBook extends AbstractBookDatabase<Void> {

	private Book oldBook;
	private Book newBook;
	public UpdateBook(Book oldBook, Book newBook) {
		this.oldBook = oldBook;
		this.newBook = newBook;
	}
	
	@Override
	void process() throws SQLException {
		String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE id = ?",
				TABLE_NAME,
				TITLE,
				AUTHOR,
				READ,
				IMAGE_URL,
				RELEASE_DAY,
				ISBN);
		
		preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, newBook.getTitle());
		preparedStatement.setString(2, newBook.getAuthor());
		preparedStatement.setBoolean(3, newBook.read());
		
		if(newBook.getImageURL() != null){
			preparedStatement.setURL(4, newBook.getImageURL());
		}else{
			preparedStatement.setURL(4, null);
		}
		
		if(newBook.getReleaseDay() != null){
			preparedStatement.setString(5, newBook.getReleaseDay().toString());
		}else{
			preparedStatement.setString(5, null);
		}
		
		if(newBook.getISBN() != null){
			preparedStatement.setLong(6, newBook.getISBN());
		}else{
			preparedStatement.setLong(6, -1);
		}
		
		preparedStatement.setInt(7, oldBook.getId());
		
		preparedStatement.executeUpdate();
	}

}
