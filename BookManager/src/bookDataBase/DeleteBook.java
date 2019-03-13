package bookDataBase;

import java.sql.SQLException;

import book.Book;

public class DeleteBook extends AbstractBookDatabase<Void> {

	private Book book;
	public DeleteBook(Book book){
		this.book = book;
	}
	
	@Override
	void process() throws SQLException {
		String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
		
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, book.getId());
		
		preparedStatement.executeUpdate();
	}

}
