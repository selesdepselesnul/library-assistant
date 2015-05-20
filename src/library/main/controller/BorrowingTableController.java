package library.main.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.main.model.Borrowing;

public class BorrowingTableController implements Initializable {

	@FXML
	private TableView<Borrowing> borrowingTableView;

	@FXML
	private TableColumn<Borrowing, Long> borrowingIdTableColumn;

	@FXML
	private TableColumn<Borrowing, Long> memberIdTableColumn;

	@FXML
	private TableColumn<Borrowing, Long> bookIdTableColumn;


	@FXML
	private TableColumn<Borrowing, LocalDate> timeOfBorrowingTableColumn;

	private List<Borrowing> borrowingList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.borrowingIdTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.memberIdTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("memberId"));
		this.bookIdTableColumn.setCellValueFactory(new PropertyValueFactory<>(
				"bookId"));
		this.timeOfBorrowingTableColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"timeOfBorrowing"));

	}

	public void setBorrowingList(List<Borrowing> borrowingList) {
		this.borrowingList = borrowingList;

		this.borrowingTableView.getItems().setAll(this.borrowingList);

	}

}
