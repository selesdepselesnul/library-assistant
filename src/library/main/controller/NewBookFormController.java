package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.main.model.Book;
import library.main.util.BookDaoMYSQL;
import library.main.util.BookGrouper;
import library.main.util.ErrorMessageWindowLoader;

public class NewBookFormController implements Initializable {

	@FXML
	private TextField isbnTextField;

	@FXML
	private TextField titleTextField;

	@FXML
	private TextField authorTextField;

	@FXML
	private ComboBox<String> publisherComboBox;

	@FXML
	private DatePicker dateOfPublishingDatePicker;

	@FXML
	private ComboBox<String> categoryComboBox;

	@FXML
	private TextField amountTextField;

	private Stage stage;

	private BookDaoMYSQL bookDaoMYSQL;

	private String bookIsbn;

	private Runnable runnable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bookIsbn = null;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public void setBookDaoMYSQL(BookDaoMYSQL bookDaoMYSQL) throws SQLException {
		this.bookDaoMYSQL = bookDaoMYSQL;

	}
	
	public void init() throws SQLException {
		if (!this.bookDaoMYSQL.isEmpty()) {
			List<Book> bookList = this.bookDaoMYSQL.readAll();
			BookGrouper.groupBasedOn(bookList, book -> book.getCategory(),
					category -> this.categoryComboBox.getItems().add(category));
			BookGrouper.groupBasedOn(
					bookList,
					book -> book.getPublisher(),
					publisher -> this.publisherComboBox.getItems().add(
							publisher));
		}
	}

	@FXML
	public void handleSubmitButton() throws IOException {
		try {
			if (this.bookIsbn == null) { //insert mode
				this.bookDaoMYSQL.write(new Book(this.titleTextField.getText(),
						this.dateOfPublishingDatePicker.getValue(),
						this.authorTextField.getText(), this.isbnTextField
								.getText(), this.publisherComboBox.getValue(),
						this.categoryComboBox.getValue(), Integer
								.parseInt(this.amountTextField.getText())));
			} else { // update mode
				this.bookDaoMYSQL.update(new Book(
						this.titleTextField.getText(),
						this.dateOfPublishingDatePicker.getValue(),
						this.authorTextField.getText(), this.bookIsbn,
						this.publisherComboBox.getValue(),
						this.categoryComboBox.getValue(), Integer
								.parseInt(this.amountTextField.getText())));
			}
			if(this.runnable != null) {
				this.runnable.run();
			}
			this.stage.close();
		} catch (NumberFormatException | SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleCancelButton() {
		this.stage.close();
	}

	public void writeToForm(Book book) {
		this.isbnTextField.setText(book.getIsbn());
		this.isbnTextField.setDisable(true);
		this.amountTextField.setText(book.getAmount() + "");
		this.categoryComboBox.setValue(book.getCategory());
		this.dateOfPublishingDatePicker.setValue(book.getDateOfPublishing());
		this.publisherComboBox.setValue(book.getPublisher());
		this.titleTextField.setText(book.getTitle());
		this.authorTextField.setText(book.getAuthors());
	}

	public void doActionBeforeExit(Runnable runnable) {
		this.runnable = runnable;
	}

}
