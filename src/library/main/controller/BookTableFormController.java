package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import simpleui.util.ErrorMessageWindowLoader;
import simpleui.util.PasswordAskerWindow;
import simpleui.util.WindowLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import library.main.model.Book;
import library.main.model.IndividualBook;
import library.main.util.BookGrouper;
import library.main.util.BookPieChartUtil;
import library.main.util.dao.filesystem.AdminDaoFS;
import library.main.util.dao.mysql.BookDaoMYSQL;
import library.main.util.dao.mysql.IndividualBookDaoMYSQL;

public class BookTableFormController implements Initializable {

	private BookDaoMYSQL bookDaoMYSQL;

	@FXML
	private TableView<IndividualBook> individualBookTableView;

	@FXML
	private TableColumn<IndividualBook, Long> idBookTableColumn;

	@FXML
	private TableColumn<IndividualBook, String> isbnTableColumn;

	@FXML
	private ComboBox<String> groupByCategoryComboBox;

	@FXML
	private ComboBox<String> groupByPublisherComboBox;

	@FXML
	private TextField isbnTextField;

	@FXML
	private TextField amountTextField;

	@FXML
	private TextField regexTextField;

	@FXML
	private MenuItem deleteMenuItem;

	@FXML
	private MenuItem seeDetailsMenuItem;

	@FXML
	private MenuItem deleteAllBooksMenuItem;

	private BookPieChartUtil bookPieChartUtil;

	private IndividualBookDaoMYSQL individualBookDaoMYSQL;

	private AdminDaoFS adminDaoMYSQL;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.idBookTableColumn.setCellValueFactory(new PropertyValueFactory<>(
				"id"));
		this.isbnTableColumn.setCellValueFactory(new PropertyValueFactory<>(
				"isbn"));
		this.groupByCategoryComboBox.setDisable(true);
		this.groupByPublisherComboBox.setDisable(true);
	}

	public void setBookDaoMYSQL(BookDaoMYSQL bookDaoMYSQL) throws SQLException {
		this.bookDaoMYSQL = bookDaoMYSQL;

	}

	public void setIndividualBookDaoMYSQL(
			IndividualBookDaoMYSQL individualBookDaoMYSQL) {
		this.individualBookDaoMYSQL = individualBookDaoMYSQL;
	}

	public void init() throws SQLException {
		this.individualBookTableView.getItems().addAll(
				this.individualBookDaoMYSQL.readAll());

		if (!this.bookDaoMYSQL.isEmpty()) {
			this.groupByCategoryComboBox.setDisable(false);
			this.groupByPublisherComboBox.setDisable(false);
			List<Book> bookList = this.bookDaoMYSQL.readAll();

			BookGrouper.groupBasedOn(bookList, b -> b.getCategory(),
					c -> this.groupByCategoryComboBox.getItems().add(c));
			BookGrouper.groupBasedOn(bookList, b -> b.getPublisher(),
					p -> this.groupByPublisherComboBox.getItems().add(p));

		}

	}

	@FXML
	public void handleDeleteIndividualBookMenuItem() {
		try {
			new PasswordAskerWindow(false,
					() -> {
						try {
							this.individualBookDaoMYSQL
									.delete(this.individualBookTableView
											.getSelectionModel()
											.getSelectedItem());
							this.individualBookTableView.getItems().remove(
									this.individualBookTableView
											.getSelectionModel()
											.getSelectedItem());
							this.bookPieChartUtil.reloadData();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSelectedCategory() {
		try {
			if (this.groupByPublisherComboBox.getSelectionModel()
					.getSelectedItem() != null) {
				this.groupByPublisherComboBox.getSelectionModel()
						.clearSelection();
			}
			groupBasedOn(book -> book.getCategory().equalsIgnoreCase(
					this.groupByCategoryComboBox.getSelectionModel()
							.getSelectedItem()));
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleSelectedPublisher() {
		try {
			if (this.groupByCategoryComboBox.getSelectionModel()
					.getSelectedItem() != null) {
				this.groupByCategoryComboBox.getSelectionModel()
						.clearSelection();
			}
			groupBasedOn(book -> book.getPublisher().equalsIgnoreCase(
					this.groupByPublisherComboBox.getSelectionModel()
							.getSelectedItem()));
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleNoFilterButton() {
		try {
			List<IndividualBook> individualBooks = this.individualBookDaoMYSQL
					.readAll();
			this.individualBookTableView.getItems().setAll(individualBooks);
			this.groupByPublisherComboBox.getSelectionModel().clearSelection();
			this.groupByCategoryComboBox.getSelectionModel().clearSelection();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private void groupBasedOn(Predicate<Book> predicate) throws SQLException {
		List<Book> filteredBookList = this.bookDaoMYSQL.readAll().stream()
				.filter(predicate).collect(Collectors.toList());
		List<List<IndividualBook>> individualBooksList = new ArrayList<>();
		for (Book book : filteredBookList) {
			individualBooksList.add(this.individualBookDaoMYSQL
					.readAllBasedOnIsbn(book.getIsbn()));
		}

		this.individualBookTableView.getItems().clear();
		for (List<IndividualBook> listOfIndividualBook : individualBooksList) {
			this.individualBookTableView.getItems()
					.addAll(listOfIndividualBook);
		}
	}

	@FXML
	public void handleAddNewIndividualBookButton() throws IOException {
		try {
			new PasswordAskerWindow(false, () -> {
				try {
					this.individualBookDaoMYSQL.add(new IndividualBook(
							this.isbnTextField.getText()), Integer
							.parseInt(this.amountTextField.getText()));
					this.individualBookTableView.getItems().setAll(
							this.individualBookDaoMYSQL.readAll());
					this.isbnTextField.clear();
					this.amountTextField.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}, this.adminDaoMYSQL).showAndWait();
		} catch (NumberFormatException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleSeeBookDetailMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/BookDetail.fxml",
					"Detail Buku",
					(fxmlLoader, stage) -> {
						try {
							BookDetailController bookDetailController = (BookDetailController) fxmlLoader
									.getController();
							bookDetailController
									.setBookIsbn(this.individualBookTableView
											.getSelectionModel()
											.getSelectedItem().getIsbn());
							bookDetailController.setBookDaoMYSQL(bookDaoMYSQL);
							bookDetailController.init();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleFilteredRegexBasedOnTitle() {
		try {
			filterRegexOf(book -> book.getTitle().matches(
					this.regexTextField.getText()));
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleFilteredRegexBasedOnAuthors() {
		try {
			filterRegexOf(book -> book.getAuthors().matches(
					this.regexTextField.getText()));
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private void filterRegexOf(Predicate<Book> predicate) throws SQLException {
		List<Book> bookList = this.bookDaoMYSQL.readAll().stream()
				.filter(predicate).collect(Collectors.toList());
		List<List<IndividualBook>> individualBooksList = new ArrayList<>();
		for (Book book : bookList) {
			individualBooksList.add(this.individualBookDaoMYSQL
					.readAllBasedOnIsbn(book.getIsbn()));
		}

		this.individualBookTableView.getItems().clear();
		for (List<IndividualBook> listOfIndividualBook : individualBooksList) {
			this.individualBookTableView.getItems()
					.addAll(listOfIndividualBook);
		}
	}

	public void setBookPieChartUtil(BookPieChartUtil bookPieChartUtil) {
		this.bookPieChartUtil = bookPieChartUtil;
	}

	@FXML
	public void handleContextMenu() {
		if (!this.individualBookTableView.getItems().isEmpty()) {
			setContextMenuItem(false);
		} else {
			setContextMenuItem(true);
		}
	}

	@FXML
	public void handleDeleteAllBooks() {
		try {
			new PasswordAskerWindow(false, () -> {

				try {
					this.bookDaoMYSQL.deleteAll();
					this.bookPieChartUtil.reloadData();
					this.individualBookTableView.getItems().clear();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setContextMenuItem(boolean isDisable) {
		this.deleteMenuItem.setDisable(isDisable);
		this.seeDetailsMenuItem.setDisable(isDisable);
		this.deleteAllBooksMenuItem.setDisable(isDisable);
	}

	public void setAdminDaoMYSQL(AdminDaoFS adminDaoMYSQL) {
		this.adminDaoMYSQL = adminDaoMYSQL;
	}

}
