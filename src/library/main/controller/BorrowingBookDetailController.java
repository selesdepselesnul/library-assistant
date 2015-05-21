package library.main.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.main.model.BookPenalty;
import library.main.util.BookBorrowingCalculator;
import library.main.util.dao.mysql.BookPenaltyDaoMYSQL;
import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;

public class BorrowingBookDetailController implements Initializable {

	@FXML
	private Text pinaltyText;

	private BorrowingDaoMYSQL borrowingDaoMYSQL;

	private long borrowingId;

	private Stage stage;

	private BookPenaltyDaoMYSQL bookPenaltyPaymentDaoMYSQL;

	private CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setBorrowingDaoMYSQL(BorrowingDaoMYSQL borrowingDaoMYSQL)
			throws SQLException {
		this.borrowingDaoMYSQL = borrowingDaoMYSQL;

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

	public void setBookPenaltyPaymentDaoMYSQL(
			BookPenaltyDaoMYSQL bookPenaltyPaymentDaoMYSQL) throws SQLException {
		this.bookPenaltyPaymentDaoMYSQL = bookPenaltyPaymentDaoMYSQL;
		long bookPenaltyAmount = BookBorrowingCalculator
				.calculatePenaltyPayment(this.borrowingDaoMYSQL,
						this.calculationConfigurationDaoMYSQL, this.borrowingId);
		this.pinaltyText.setText(bookPenaltyAmount + "");
		if (bookPenaltyAmount > 0) {
			this.bookPenaltyPaymentDaoMYSQL.write(new BookPenalty(
					this.borrowingId, bookPenaltyAmount));
		}
	}

	public void setCalculationConfigurationDaoMYSQL(
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL) {
		this.calculationConfigurationDaoMYSQL = calculationConfigurationDaoMYSQL;
	}

}
