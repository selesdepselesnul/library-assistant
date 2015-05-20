package library.main.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import library.main.model.Book;
import library.main.util.dao.mysql.BookDaoMYSQL;

public class BookDetailController {

	@FXML
	private TextField isbnTextField;

	@FXML
	private TextField titleTextField;

	@FXML
	private TextField authorTextField;

	@FXML
	private TextField publisherTextField;

	@FXML
	private TextField dateOfPublishingTextField;

	@FXML
	private TextField categoryTextField;

	@FXML
	private TextField amountTextField;

	private String isbn;
	private BookDaoMYSQL bookDaoMYSQL;

	public void setBookDaoMYSQL(BookDaoMYSQL bookDaoMYSQL) {
		this.bookDaoMYSQL = bookDaoMYSQL;
	}

	public void setBookIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void init() throws SQLException {
		Book book = this.bookDaoMYSQL.read(this.isbn);
		this.amountTextField.setText(book.getAmount() + "");
		this.authorTextField.setText(book.getAuthors());
		this.categoryTextField.setText(book.getCategory());
		this.dateOfPublishingTextField.setText(book.getDateOfPublishing()
				.toString());
		this.isbnTextField.setText(book.getIsbn());
		this.publisherTextField.setText(book.getPublisher());
		this.titleTextField.setText(book.getTitle());
		
	}

}
