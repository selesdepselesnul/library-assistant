package library.main.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.main.util.BookBorrowingCalculator;
import library.main.util.BorrowingDaoMYSQL;

public class BorrowingBookDetailController implements Initializable {

	@FXML
	private Text pinaltyText;

	private BorrowingDaoMYSQL borrowingDaoMYSQL;

	private long borrowingId;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setBorrowingDaoMYSQL(BorrowingDaoMYSQL borrowingDaoMYSQL)
			throws SQLException {
		this.borrowingDaoMYSQL = borrowingDaoMYSQL;
		this.pinaltyText.setText(BookBorrowingCalculator
				.calculatePinaltyPayment(this.borrowingDaoMYSQL,
						this.borrowingId)
				+ "");

	}

	public void setBorrowingId(long borrowingId) {
		this.borrowingId = borrowingId;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleOkButton() {
		this.stage.close();
	}

}
