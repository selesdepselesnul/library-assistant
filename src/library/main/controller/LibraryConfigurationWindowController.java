package library.main.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import library.main.model.Admin;
import library.main.util.AdminDaoMYSQL;
import library.main.util.Calculation;

public class LibraryConfigurationWindowController {

	private static final String DIGIT = "^\\d+$";

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField emailTextField;

	@FXML
	private Button submitForAdminAccountButton;

	@FXML
	private Button editForAdminButton;

	@FXML
	private Text weakAccountText;

	@FXML
	private TextField memberMonthlyPaymentTextField;

	@FXML
	private TextField memberPenaltyPaymentTextField;

	@FXML
	private TextField bookPenaltyPaymentTextField;

	@FXML
	private TextField bookMaxDaysOfBorrowingTextField;

	@FXML
	private TextField memberDaysOfRoutinePayment;

	@FXML
	private Button submitForCalculationButton;

	@FXML
	private Button editForCalculationButton;

	@FXML
	private Text errorForCalculationText;

	private AdminDaoMYSQL adminDaoMYSQL;

	@FXML
	public void handleEditForAdminAccount() {
		this.usernameTextField.setDisable(false);
		this.passwordField.setDisable(false);
		this.emailTextField.setDisable(false);

		this.editForAdminButton.setDisable(true);
		this.submitForAdminAccountButton.setDisable(false);
	}

	@FXML
	public void handleSubmitForAdminAccount() {
		try {
			Admin admin = new Admin(this.usernameTextField.getText(),
					this.passwordField.getText(), this.emailTextField.getText());

			boolean notEmpty = !(admin.getUsername().isEmpty())
					&& !(admin.getPassword().isEmpty());

			if (notEmpty && admin.getPassword().matches(".{8,}")) {

				this.adminDaoMYSQL.update(admin);

				this.usernameTextField.setDisable(true);
				this.passwordField.setDisable(true);
				this.emailTextField.setDisable(true);

				this.submitForAdminAccountButton.setDisable(true);
				this.editForAdminButton.setDisable(false);
				this.weakAccountText.setVisible(false);

			} else {
				this.weakAccountText.setVisible(true);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleEditForCalculationButton() {

		this.memberMonthlyPaymentTextField.setDisable(false);
		this.memberPenaltyPaymentTextField.setDisable(false);
		this.bookPenaltyPaymentTextField.setDisable(false);
		this.bookMaxDaysOfBorrowingTextField.setDisable(false);
		this.memberDaysOfRoutinePayment.setDisable(false);

		this.editForCalculationButton.setDisable(true);
		this.submitForCalculationButton.setDisable(false);
	}

	@FXML
	public void handleSubmitForCalculation() {
		try {

			String memberMonthlyPaymentString = this.memberMonthlyPaymentTextField
					.getText();
			String memberPenaltyPaymentString = this.memberPenaltyPaymentTextField
					.getText();
			String bookPenaltyPaymentString = this.bookPenaltyPaymentTextField
					.getText();
			String bookMaxDaysOfBorrowingString = this.bookMaxDaysOfBorrowingTextField
					.getText();
			String memberDaysOfRoutinePaymentString = this.memberDaysOfRoutinePayment
					.getText();

			if (memberMonthlyPaymentString.matches(DIGIT)
					&& memberPenaltyPaymentString.matches(DIGIT)
					&& bookPenaltyPaymentString.matches(DIGIT)
					&& bookMaxDaysOfBorrowingString.matches(DIGIT)
					&& memberDaysOfRoutinePaymentString.matches(DIGIT)) {

				Calculation.setMemberMonthlyPayment(Long
						.parseLong(memberMonthlyPaymentString));
				Calculation.setMemberPenaltyPayment(Long
						.parseLong(memberPenaltyPaymentString));
				Calculation.setBookPenaltyPayment(Long
						.parseLong(bookPenaltyPaymentString));
				Calculation.setBookMaxDaysOfBorrowing(Long
						.parseLong(bookMaxDaysOfBorrowingString));
				Calculation.setMemberMaxDaysOfPayment(Long
						.parseLong(memberDaysOfRoutinePaymentString));

				this.submitForCalculationButton.setDisable(true);
				this.editForCalculationButton.setDisable(false);

				this.memberMonthlyPaymentTextField.setDisable(true);
				this.memberPenaltyPaymentTextField.setDisable(true);
				this.bookPenaltyPaymentTextField.setDisable(true);
				this.bookMaxDaysOfBorrowingTextField.setDisable(true);
				this.memberDaysOfRoutinePayment.setDisable(true);
			} else {
				this.errorForCalculationText.setVisible(true);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void setAdminDaoMYSQL(AdminDaoMYSQL adminDaoMYSQL) {
		this.adminDaoMYSQL = adminDaoMYSQL;
		try {
			writeAdmin();
			writeCalculation();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void writeCalculation() {
		this.memberMonthlyPaymentTextField.setText(Calculation
				.getMemberMonthlyPayment() + "");
		this.memberPenaltyPaymentTextField.setText(Calculation
				.getMemberPenaltyPayment() + "");
		this.bookPenaltyPaymentTextField.setText(Calculation
				.getBookPenaltyPayment() + "");
		this.bookMaxDaysOfBorrowingTextField.setText(Calculation
				.getBookMaxDaysOfBorrowing() + "");
		this.memberDaysOfRoutinePayment.setText(Calculation
				.getMemberMaxDaysOfPayment() + "");
	}

	private void writeAdmin() throws SQLException {
		Admin admin = this.adminDaoMYSQL.read();
		this.usernameTextField.setText(admin.getUsername());
		this.passwordField.setText(admin.getPassword());
		this.emailTextField.setText(admin.getEmail());
	}

}
