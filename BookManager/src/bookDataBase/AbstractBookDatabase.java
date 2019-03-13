package bookDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractBookDatabase<T> {
	protected T value;
	protected Connection connection = null;
	protected PreparedStatement preparedStatement = null;
	protected ResultSet resultSet = null;
	
	protected static final String PATH = "jdbc:mysql://192.168.1.17:3306/media";
	protected static final String TABLE_NAME = "book";
	protected static final String USER_NAME = "george";
	protected static final String PASSWORD = "e9fgy7kz";
	
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ISBN = "isbn";
	public static final String IMAGE_URL = "imageURL";
	public static final String RELEASE_DAY = "release_day";
	public static final String READ = "is_read";
	public static final String ID = "id";
	
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException classNotFoundException){
			classNotFoundException.printStackTrace();
		}
	}

	public AbstractBookDatabase() {
	}
	
	protected void init() throws SQLException {
		connection = DriverManager.getConnection(PATH, USER_NAME, PASSWORD);
		connection.setAutoCommit(false);
	}
	
	abstract void process() throws SQLException;
	
	protected void close() throws SQLException {
		if(connection != null){
			connection.close();
		}
	}
	
	public final T execute(){
		try{
			init();
			process();
			connection.commit();
		}catch(SQLException sqlException){
			sqlException.printStackTrace();
			try {
				if(connection != null){
					connection.rollback();
				}
			} catch (SQLException e) {
			}
		}finally{
			try{
				close();
			}catch (SQLException e) {
			}
		}
		return value;
	}
	
	
	
}
