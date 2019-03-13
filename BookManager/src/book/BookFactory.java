package book;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class BookFactory {
	
	private static final String DOMAIN = "https://app.rakuten.co.jp/services/api/BooksBook/Search/20130522";
	private static final long APP_ID = 1075382459761769142L;
	private static final String CHAR_SET = "UTF-8";
	
	private static final String TITLE = "title";
	private static final String AUTHOR = "author";
	private static final String IMAGE_URL = "largeImageUrl";
	private static final String SALES_DATE = "salesDate";
	private static final String ISBN = "isbn";
	
	private BookFactory(){
	}
	
	private static BookFactory bookFactory = new BookFactory();
	public static BookFactory getInstance(){
		return bookFactory;
	}
	
	public Book create(Long isbn) throws IOException,SAXException,ParserConfigurationException {
		Book book = new Book();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(DOMAIN);
		builder.append(String.format("?format=%s", "xml"));
		builder.append(String.format("&isbn=%d", isbn));
		builder.append(String.format("&applicationId=%d", APP_ID));

		URL url = new URL(builder.toString());

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setInstanceFollowRedirects(false);

		connection.connect();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_SET));
		builder.setLength(0);
		
		while(true){
			String context = reader.readLine();
			if(context == null){
				break;
			}
			builder.append(context);
		}

		String context = builder.toString();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		
		Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(context.getBytes(CHAR_SET))));
		
		Element element = document.getDocumentElement();
		NodeList items = element.getElementsByTagName("Items").item(0).getChildNodes();
		
		for(int index = 0; index < items.getLength(); index++){
			Node node = items.item(index);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList item = node.getChildNodes();
				for(int i = 0; i < item.getLength(); i++){
					Node value = item.item(i);
					
					switch(value.getNodeName()){
						case TITLE:{
							book.setTitle(value.getTextContent());
							break;
						}
						case AUTHOR:{
							String data = value.getTextContent();
							if(data.contains("/")){
								String[] array = data.split("/");
								
								book.setAuthor(array[0]);
							}else{
								book.setAuthor(data);
							}
							
							break;
						}
						case ISBN:{
							book.setISBN(Long.parseLong(value.getTextContent()));
							break;
						}
						case SALES_DATE:{
							book.setReleaseDay(value.getTextContent());
							break;
						}
						case IMAGE_URL:{
							book.setImageURL(new URL(value.getTextContent().replace("?_ex=200x200", "")));
							break;
						}
					}
				}
			}
		}
		
		connection.disconnect();
		reader.close();

		return book;
	}
	
	public Book create(String title, String author, URL imageURL, String releaseDay){
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		book.setImageURL(imageURL);
		book.setReleaseDay(releaseDay);
		return book;
	}
	
	public Book create(String title, String author, Boolean read, URL imageURL, String releaseDay, Long isbn){
		Book book = create(title, author, imageURL, releaseDay);
		book.isRead(read);
		book.setISBN(isbn);
		
		return book;
	}
}
