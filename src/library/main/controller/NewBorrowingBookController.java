package library.main.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.main.model.Borrowing;
import library.main.util.BorrowingDaoMYSQL;
import library.main.util.ErrorMessageWindowLoader;

public class NewBorrowingBookController {

	@FXML
	private TextField memberIdTextField;

	@FXML
	private TextField bookIdTextField;

	private Stage stage;

	private BorrowingDaoMYSQL borrowingDaoMYSQL;

	private Runnable runnable;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleSubmitButton() throws IOException {
		try {
			Borrowing borrowing = new Borrowing(
					Long.parseLong(this.memberIdTextField.getText()),
					Long.parseLong(this.bookIdTextField.getText()));
			this.borrowingDaoMYSQL.write(borrowing);
			this.bookIdTextField.clear();
			this.memberIdTextField.clear();
			if(this.runnable != null) {
				this.runnable.run();
			}
			this.stage.close();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleCancelButton() {
		this.stage.close();
	}

	public void setBorrowingDaoMYSQL(BorrowingDaoMYSQL borrowingDaoMYSQL) {
		this.borrowingDaoMYSQL = borrowingDaoMYSQL;
	}

	public void doActionBeforeExit(Runnable runnable) {
		this.runnable = runnable;
	}

}
