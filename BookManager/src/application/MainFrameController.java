package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import book.Book;
import book.BookFactory;
import bookDataBase.DeleteBook;
import bookDataBase.DownloadBook;
import bookDataBase.InsertBook;
import bookDataBase.UpdateBook;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class MainFrameController {

	private ObservableList<Book> bookList;

	private TableColumn<Book, Number> idTableColumn;
	private TableColumn<Book, String> titleTableColumn;
	private TableColumn<Book, String> authorTableColmn;
	private TableColumn<Book, String> readTableColumn;
	private TableColumn<Book, String> releaseTableColumn;
	private TableColumn<Book, Number> isbnTableColumn;

	private BookFactory bookFactory = BookFactory.getInstance();

	@FXML
	protected void initialize() {
		bookList = FXCollections.observableArrayList();
		tableView.setItems(bookList);
		
		idTableColumn = new TableColumn<Book, Number>("ID");
		idTableColumn.setPrefWidth(50);
		idTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Book, Number>, ObservableValue<Number>>() {
					@Override
					public ObservableValue<Number> call(CellDataFeatures<Book, Number> param) {
						return new SimpleIntegerProperty(param.getValue().getId());
					}
				});

		titleTableColumn = new TableColumn<Book, String>("ƒ^ƒCƒgƒ‹");
		titleTableColumn.setPrefWidth(150);
		titleTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Book, String> param) {
				return new SimpleStringProperty(param.getValue().getTitle());
			}
		});

		authorTableColmn = new TableColumn<Book, String>("’˜ŽÒ");
		authorTableColmn.setPrefWidth(130);
		authorTableColmn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Book, String> param) {
						return new SimpleStringProperty(param.getValue().getAuthor());
					}
				});

		readTableColumn = new TableColumn<Book, String>("Šù“Ç");
		readTableColumn.setPrefWidth(40);
		readTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Book, String> param) {
						if (param.getValue().read()) {
							
							return new SimpleStringProperty("Šù“Ç");
						} else {
							return new SimpleStringProperty("–¢“Ç");
						}
					}
				});
		readTableColumn.setCellFactory(new Callback<TableColumn<Book,String>, TableCell<Book,String>>() {
			@Override
			public TableCell<Book, String> call(TableColumn<Book, String> bookTableColumn){
				return new TableCell<Book,String>(){
					@Override
					protected void updateItem(String item, boolean empty){
						super.updateItem(item,empty);
						if(!empty && item.equals("Šù“Ç")){
							setText("Šù“Ç");
							setTextFill(Color.RED);
						}else{
							setText("–¢“Ç");
							setTextFill(Color.BLUE);
						}
					}
				};
			}
		});

		releaseTableColumn = new TableColumn<Book, String>("”­”„“ú");
		releaseTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Book, String> param) {
						return new SimpleStringProperty(param.getValue().getReleaseDay());
					}
				});

		isbnTableColumn = new TableColumn<Book, Number>("ISBN");
		isbnTableColumn.setPrefWidth(100);
		isbnTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Book, Number>, ObservableValue<Number>>() {
					@Override
					public ObservableValue<Number> call(CellDataFeatures<Book, Number> param) {
						return new SimpleLongProperty(param.getValue().getISBN());
					}
				});

		tableView.getColumns().add(idTableColumn);
		tableView.getColumns().add(titleTableColumn);
		tableView.getColumns().add(authorTableColmn);
		tableView.getColumns().add(releaseTableColumn);
		tableView.getColumns().add(readTableColumn);
		tableView.getColumns().add(isbnTableColumn);

		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
			@Override
			public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
				if (tableView.getSelectionModel().getSelectedItem() != null) {
					idLabel.setText(String.format("ID:%d", newValue.getId()));
					titleTextField.setText(newValue.getTitle());
					authorTextField.setText(newValue.getAuthor());

					if (newValue.read()) {
						readToggleButton.setSelected(true);
					} else {
						readToggleButton.setSelected(false);
					}

					if (!newValue.getReleaseDay().isEmpty()) {
						releaseLabel.setText(newValue.getReleaseDay());
					}

					isbnTextField.setText(newValue.getISBN().toString());
					
					if(newValue.getImageURL() != null){
						imageURLTextField.setText(newValue.getImageURL().toString());
						try {
							HttpURLConnection connection = (HttpURLConnection)newValue.getImageURL().openConnection();
							connection.setAllowUserInteraction(false);
							connection.setInstanceFollowRedirects(false);
							connection.setRequestMethod("GET");
							connection.connect();

							DataInputStream inputStream = new DataInputStream(connection.getInputStream());

							imageView.setImage(new Image(inputStream));
							
							inputStream.close();
							connection.disconnect();
						} catch (IOException ioException) {
							consoleTextArea.appendText(ioException.getMessage());
							consoleTextArea.appendText("\n");
						}
					}else{
						imageURLTextField.setText("");
						imageView.setImage(null);
					}
					
				}
			}
		});

		readToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					readToggleButton.setText("Šù“Ç");
				} else {
					readToggleButton.setText("–¢“Ç");
				}
			}
		});
	}

	@FXML
	private TextField titleTextField;
	@FXML
	private TextField authorTextField;
	@FXML
	private TextField isbnTextField;

	@FXML
	private void isbnTextField_Action() {
		Long isbn = -1L;
		try {
			isbn = Long.parseLong(isbnTextField.getText());
		} catch (NumberFormatException e) {
			consoleTextArea.appendText(e.getMessage());
			consoleTextArea.appendText("\n");
		}

		if (isbn != -1L) {
			try {
				Book book = bookFactory.create(isbn);
				bookList.add(book);
				isbnTextField.setText("");
			} catch (Exception e) {
				consoleTextArea.appendText(e.getMessage());
				consoleTextArea.appendText("\n");
			}
		}
	}

	@FXML
	private TextField imageURLTextField;

	@FXML
	private void imageURLTextField_Action() {
		try{
			URL url = new URL(imageURLTextField.getText());
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setAllowUserInteraction(false);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.connect();
			
			DataInputStream inputStream = new DataInputStream(connection.getInputStream());
			imageView.setImage(new Image(inputStream));
			
			connection.disconnect();
			inputStream.close();
		}catch(IOException ioException){
			consoleTextArea.appendText(ioException.getMessage());
			consoleTextArea.appendText("\n");
		}
	}

	@FXML
	private ToggleButton readToggleButton;

	@FXML
	private TextArea consoleTextArea;
	
	@FXML
	private Label releaseLabel;
	@FXML
	private Label idLabel;

	@FXML
	private Button searchButton;

	@FXML
	private void searchButton_Click() {
		DownloadBook downloadBook = new DownloadBook();
		List<Book> books = downloadBook.execute();
		books.stream().forEach(b -> bookList.add(b));
	}

	@FXML
	private Button updateButton;

	@FXML
	private void updateButton_Click() {
		URL imageURL = null;
		try{
			imageURL = new URL(imageURLTextField.getText());
		}catch(MalformedURLException e){
			consoleTextArea.appendText(e.getMessage());
			consoleTextArea.appendText("\n");
		}
		
		Book oldBook = tableView.getSelectionModel().getSelectedItem();
		if (oldBook != null && oldBook.getId() != -1) {
			Book newBook = bookFactory.create(titleTextField.getText(), authorTextField.getText(),
					readToggleButton.isSelected(), oldBook.getImageURL(), releaseLabel.getText(),
					Long.parseLong(isbnTextField.getText()));

			if(imageURL != null){
				newBook.setImageURL(imageURL);
			}
			
			UpdateBook updateBook = new UpdateBook(oldBook, newBook);
			updateBook.execute();

			newBook.setId(oldBook.getId());
			bookList.set(bookList.indexOf(oldBook), newBook);
		}else if(oldBook != null && oldBook.getId() == -1){
			Book newBook = bookFactory.create(titleTextField.getText(), authorTextField.getText(),
					readToggleButton.isSelected(), oldBook.getImageURL(), releaseLabel.getText(),
					Long.parseLong(isbnTextField.getText()));

			if(imageURL != null){
				newBook.setImageURL(imageURL);
			}
			
			bookList.set(bookList.indexOf(oldBook), newBook);
		}
	}

	@FXML
	private Button deleteButton;
	@FXML
	private void deleteButton_Click() {
		Book book = tableView.getSelectionModel().getSelectedItem();
		
		if (book != null && book.getId() != -1 && confirm("", "ìœ‚µ‚Ä‚à‚æ‚ë‚µ‚¢‚Å‚µ‚å‚¤‚©?")) {
			book = tableView.getSelectionModel().getSelectedItem();
			DeleteBook deleteBook = new DeleteBook(book);
			deleteBook.execute();
			bookList.remove(book);
		}else if(book != null && book.getId() == -1 && confirm("", "ìœ‚µ‚Ä‚à‚æ‚ë‚µ‚¢‚Å‚µ‚å‚¤‚©?")){
			bookList.remove(book);
		}
	}

	@FXML
	private Button addButton;

	@FXML
	private void addButton_Click() {
		bookList.stream().filter(b -> b.getId() == -1).forEach(b -> {
			InsertBook insertBook = new InsertBook(b);
			insertBook.execute();
		});
		bookList.clear();
	}

	@FXML
	private Button addBookButton;

	@FXML
	private void addBookButton_Click() {
		if(!titleTextField.getText().isEmpty()){
			Book book = bookFactory.create(
					titleTextField.getText(),
					authorTextField.getText(),
					readToggleButton.isSelected(),
					null,
					releaseLabel.getText(),
					0L);
			book.setId(-1);
			
			try{
				URL imageURL = new URL(imageURLTextField.getText());
				book.setImageURL(imageURL);
			}catch(MalformedURLException e){
				book.setImageURL(null);
			}
			
			try{
				Long isbn = Long.parseLong(isbnTextField.getText());
				book.setISBN(isbn);
			}catch(NumberFormatException e){
				book.setISBN(0L);
			}
			
			bookList.add(book);
		}
	}
	
	@FXML
	private Button clearButton;
	@FXML
	private void clearButton_Click(){
		bookList.clear();
		clearFieldButton_Click();
	}
	
	@FXML
	private Button clearFieldButton;
	@FXML
	private void clearFieldButton_Click(){
		idLabel.setText("ID:");
		titleTextField.setText("");
		authorTextField.setText("");
		readToggleButton.setSelected(false);
		releaseLabel.setText("0000/00/00");
		isbnTextField.setText("");
		imageURLTextField.setText("");
		imageView.setImage(null);
	}

	@FXML
	private TableView<Book> tableView;
	
	@FXML
	private ImageView imageView;
	
	private boolean confirm(String title, String contextMessage){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(contextMessage);
		alert.setTitle(title);
		
		alert.showAndWait();
		ButtonType result = alert.getResult();
		if(result == ButtonType.OK){
			return true;
		}else{
			return false;
		}
	}
}
