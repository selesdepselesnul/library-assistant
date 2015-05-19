package library.main.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import library.main.model.Admin;
import library.main.model.CalculationConfiguration;
import library.main.util.AdminDaoMYSQL;
import library.main.util.CalculationConfigurationDaoMYSQL;
import library.main.util.ErrorMessageWindowLoader;

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
	private TextField memberRoutinePaymentTextField;

	@FXML
	private TextField memberPenaltyPaymentTextField;

	@FXML
	private TextField memberMaxDaysOfPayment;

	@FXML
	private TextField bookPenaltyPaymentTextField;

	@FXML
	private TextField bookMaxDaysOfBorrowingTextField;

	@FXML
	private Button submitForCalculationButton;

	@FXML
	private Button editForCalculationButton;

	@FXML
	private Text errorForCalculationText;

	private AdminDaoMYSQL adminDaoMYSQL;

	private CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL;

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

		this.memberRoutinePaymentTextField.setDisable(false);
		this.memberPenaltyPaymentTextField.setDisable(false);
		this.memberMaxDaysOfPayment.setDisable(false);

		this.bookPenaltyPaymentTextField.setDisable(false);
		this.bookMaxDaysOfBorrowingTextField.setDisable(false);

		this.editForCalculationButton.setDisable(true);
		this.submitForCalculationButton.setDisable(false);
	}

	@FXML
	public void handleSubmitForCalculation() {
		try {

			String memberRoutinePaymentString = this.memberRoutinePaymentTextField
					.getText();
			String memberMaxDaysOfPaymentString = this.memberMaxDaysOfPayment
					.getText();
			String memberPenaltyPaymentString = this.memberPenaltyPaymentTextField
					.getText();
			String bookPenaltyPaymentString = this.bookPenaltyPaymentTextField
					.getText();
			String bookMaxDaysOfBorrowingString = this.bookMaxDaysOfBorrowingTextField
					.getText();

			if (memberRoutinePaymentString.matches(DIGIT)
					&& memberMaxDaysOfPaymentString.matches(DIGIT)
					&& memberPenaltyPaymentString.matches(DIGIT)
					&& bookPenaltyPaymentString.matches(DIGIT)
					&& bookMaxDaysOfBorrowingString.matches(DIGIT)) {

				CalculationConfiguration calculationConfiguration = new CalculationConfiguration();

				calculationConfiguration.setMemberRoutinePayment(Long
						.parseLong(memberRoutinePaymentString));
				calculationConfiguration.setMemberPenaltyPayment(Long
						.parseLong(memberPenaltyPaymentString));
				calculationConfiguration.setMemberMaxDaysOfPayment(Long
						.parseLong(memberMaxDaysOfPaymentString));

				calculationConfiguration.setBookPenaltyPayment(Long
						.parseLong(bookPenaltyPaymentString));
				calculationConfiguration.setBookMaxDaysOfBorrowing(Long
						.parseLong(bookMaxDaysOfBorrowingString));

				this.calculationConfigurationDaoMYSQL
						.write(calculationConfiguration);

				this.submitForCalculationButton.setDisable(true);
				this.editForCalculationButton.setDisable(false);

				this.memberRoutinePaymentTextField.setDisable(true);
				this.memberPenaltyPaymentTextField.setDisable(true);
				this.memberMaxDaysOfPayment.setDisable(true);

				this.bookPenaltyPaymentTextField.setDisable(true);
				this.bookMaxDaysOfBorrowingTextField.setDisable(true);

			} else {
				this.errorForCalculationText.setVisible(true);
			}
		} catch (NumberFormatException | SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	public void setAdminDaoMYSQL(AdminDaoMYSQL adminDaoMYSQL)
			throws SQLException {
		this.adminDaoMYSQL = adminDaoMYSQL;
		writeAdminConfiguration();
	}

	private void writeCalculationConfiguration() throws SQLException {

		CalculationConfiguration lastCalculationConfiguration = this.calculationConfigurationDaoMYSQL
				.readLastConfig();

		this.memberRoutinePaymentTextField.setText(lastCalculationConfiguration
				.getMemberRoutinePayment() + "");
		this.memberPenaltyPaymentTextField.setText(lastCalculationConfiguration
				.getMemberPenaltyPayment() + "");
		this.memberMaxDaysOfPayment.setText(lastCalculationConfiguration
				.getMemberMaxDaysOfPayment() + "");

		this.bookPenaltyPaymentTextField.setText(lastCalculationConfiguration
				.getBookPenaltyPayment() + "");
		this.bookMaxDaysOfBorrowingTextField
				.setText(lastCalculationConfiguration
						.getBookMaxDaysOfBorrowing() + "");
	}

	private void writeAdminConfiguration() throws SQLException {
		Admin admin = this.adminDaoMYSQL.read();
		this.usernameTextField.setText(admin.getUsername());
		this.passwordField.setText(admin.getPassword());
		this.emailTextField.setText(admin.getEmail());
	}

	public void setCalculationConfigurationDaoMYSQL(
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL)
			throws SQLException {
		this.calculationConfigurationDaoMYSQL = calculationConfigurationDaoMYSQL;
		writeCalculationConfiguration();
	}

}
