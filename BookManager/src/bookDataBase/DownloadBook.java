package bookDataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import book.Book;
import book.BookFactory;

public class DownloadBook extends AbstractBookDatabase<List<Book>> {

	@Override
	void process() throws SQLException {
		List<Book> books = new ArrayList<>();
		
		String query = String.format("SELECT * FROM %s", TABLE_NAME);
		
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		
		BookFactory factory = BookFactory.getInstance();
		while(resultSet.next()){
			Book book = factory.create(
					resultSet.getString(TITLE),
					resultSet.getString(AUTHOR),
					resultSet.getURL(IMAGE_URL),
					resultSet.getString(RELEASE_DAY));
			
			book.setISBN(resultSet.getLong(ISBN));
			book.isRead(resultSet.getBoolean(READ));
			book.setId(resultSet.getInt(ID));
			
			books.add(book);
		}
		
		value = books;
	}

}
