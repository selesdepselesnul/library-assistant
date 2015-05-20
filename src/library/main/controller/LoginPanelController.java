package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import simpleui.util.ErrorMessageWindowLoader;
import simpleui.util.WindowLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.main.model.Admin;
import library.main.util.dao.filesystem.AdminDaoFS;
import library.main.util.dao.mysql.BookDaoMYSQL;
import library.main.util.dao.mysql.BookPenaltyDaoMYSQL;
import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;
import library.main.util.dao.mysql.IndividualBookDaoMYSQL;
import library.main.util.dao.mysql.MemberDaoMYSQL;
import library.main.util.dao.mysql.MemberPaymentDaoMYSQL;

public class LoginPanelController implements Initializable {

	@FXML
	private Button okButton;

	@FXML
	private ImageView loginImageView;

	@FXML
	private Label accountErrorLabel;

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	private Stage loginPanelStage;

	private MemberDaoMYSQL memberDaoMYSQL;

	private BookDaoMYSQL bookDaoMYSQL;

	private IndividualBookDaoMYSQL individualBookDaoMYSQL;

	private BorrowingDaoMYSQL borrowingDaoMYSQL;

	private MemberPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL;

	private BookPenaltyDaoMYSQL bookPenaltyDaoMYSQL;

	private AdminDaoFS adminDaoMYSQL;

	private CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.loginImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/login.png")));
	}

	public void setLoginPanelStage(Stage stage) {
		this.loginPanelStage = stage;
	}

	@FXML
	public void handleOnFieldNull() {
		if (!this.usernameTextField.getText().equalsIgnoreCase("")
				&& !this.passwordField.getText().equalsIgnoreCase("")) {
			this.okButton.setDisable(false);
		} else {
			this.okButton.setDisable(true);
		}
	}

	@FXML
	public void handleOkButton() {

		try {

			if (!Files.exists(Paths.get(this.adminDaoMYSQL.getAdminPath()))) {
				this.adminDaoMYSQL.updateIfExist(new Admin("root",
						"indonesiaraya"));
			}

			Admin admin = new Admin(this.usernameTextField.getText(),
					this.passwordField.getText());
			if (this.adminDaoMYSQL.read().equals(admin)) {
				try {
					new WindowLoader(
							"library/main/view/MainWindow.fxml",
							"Library Assistant -BETA-",
							(fxmlLoader, stage) -> {
								try {
									this.accountErrorLabel.setVisible(false);

									MainWindowController mainWindowController = (MainWindowController) fxmlLoader
											.getController();
									mainWindowController
											.setAdminDaoMYSQL(this.adminDaoMYSQL);
									mainWindowController
											.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
									mainWindowController
											.setMemberDaoMYSQL(this.memberDaoMYSQL);
									mainWindowController
											.setBookDaoMYSQL(this.bookDaoMYSQL);
									mainWindowController
											.setIndividualBookDaoMYSQL(this.individualBookDaoMYSQL);
									mainWindowController
											.setBorrowingDaoMYSQL(this.borrowingDaoMYSQL);
									mainWindowController
											.setMemberMonthlyPaymentDaoMYSQL(this.memberMonthlyPaymentDaoMYSQL);
									mainWindowController
											.setBookPenaltyPaymentDaoMYSQL(this.bookPenaltyDaoMYSQL);
									mainWindowController.init();

									this.loginPanelStage.close();

									stage.initStyle(StageStyle.UNDECORATED);
									stage.setMaximized(true);
									stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
									stage.setFullScreen(true);
								} catch (Exception e) {
									new ErrorMessageWindowLoader(e.getMessage())
											.show();
								}

							}).show(WindowLoader.SHOW_AND_WAITING);
				} catch (IOException e) {
					new ErrorMessageWindowLoader(e.getMessage()).show();
				}
			} else {
				this.accountErrorLabel.setVisible(true);
			}
		} catch (IOException | ClassNotFoundException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
	}

	@FXML
	public void handleCancelButton() {
		Platform.exit();
	}

	public void setBookDaoMYSQL(BookDaoMYSQL bookDaoMYSQL) {
		this.bookDaoMYSQL = bookDaoMYSQL;
	}

	public void setIndividualBookDaoMYSQL(
			IndividualBookDaoMYSQL individualBookDaoMYSQL) {
		this.individualBookDaoMYSQL = individualBookDaoMYSQL;
	}

	public void setBorrowingDaoMYSQL(BorrowingDaoMYSQL borrowingDaoMYSQL) {
		this.borrowingDaoMYSQL = borrowingDaoMYSQL;
	}

	public void setMemberMonthlyPaymentDaoMYSQL(
			MemberPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL) {
		this.memberMonthlyPaymentDaoMYSQL = memberMonthlyPaymentDaoMYSQL;
	}

	public void setBookPenaltyPaymentDaoMYSQL(
			BookPenaltyDaoMYSQL bookPenaltyDaoMYSQL) {
		this.bookPenaltyDaoMYSQL = bookPenaltyDaoMYSQL;
	}

	public void setAdminDaoMYSQL(AdminDaoFS adminDaoMYSQL) {
		this.adminDaoMYSQL = adminDaoMYSQL;
	}

	public void setCalculationConfigurationDaoMYSQL(
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL) {
		this.calculationConfigurationDaoMYSQL = calculationConfigurationDaoMYSQL;
	}

}
