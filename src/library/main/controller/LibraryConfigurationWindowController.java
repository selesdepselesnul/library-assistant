package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import library.main.util.configuration.Admin;

public class LibraryConfigurationWindowController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Button submitForAdminAccountButton;

	@FXML
	private Button editForAdminButton;

	@FXML
	private Text weakAccountText;

	@FXML
	public void handleEditForAdminAccount() {
		this.usernameTextField.setDisable(false);
		this.passwordField.setDisable(false);
		this.editForAdminButton.setDisable(true);
		this.submitForAdminAccountButton.setDisable(false);
	}

	@FXML
	public void handleSubmitForAdminAccount() {
		try {
			String username = this.usernameTextField.getText();
			String password = this.passwordField.getText();
			boolean notEmpty = !(username.isEmpty()) && !(password.isEmpty());
			if (notEmpty && password.matches(".{8,}")) {
				Admin.setUsername(username);
				Admin.setPassword(password);
				this.usernameTextField.setDisable(true);
				this.passwordField.setDisable(true);
				this.submitForAdminAccountButton.setDisable(false);
				this.editForAdminButton.setDisable(false);
				this.weakAccountText.setVisible(false);
			} else {
				this.weakAccountText.setVisible(true);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
