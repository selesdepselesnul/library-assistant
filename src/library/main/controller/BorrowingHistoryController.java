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
import library.main.model.BorrowingHistory;

public class BorrowingHistoryController implements Initializable {
	@FXML
	private TableView<BorrowingHistory> borrowingHistoryTableView;

	@FXML
	private TableColumn<BorrowingHistory, Long> bookIdTableColumn;

	@FXML
	private TableColumn<BorrowingHistory, LocalDate> timeOfBorrowingTableColumn;

	@FXML
	private TableColumn<BorrowingHistory, Integer> pinaltyTableColumn;

	private List<BorrowingHistory> borrowingHistoryList;

	public void setBorrowingHistoryList(
			List<BorrowingHistory> borrowingHistoryList) {
		this.borrowingHistoryList = borrowingHistoryList;
		this.borrowingHistoryTableView.getItems().addAll(
				this.borrowingHistoryList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bookIdTableColumn.setCellValueFactory(new PropertyValueFactory<>(
				"bookId"));
		this.timeOfBorrowingTableColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"timeOfBorrowing"));
		this.pinaltyTableColumn.setCellValueFactory(new PropertyValueFactory<>(
				"pinalty"));
	}

}
