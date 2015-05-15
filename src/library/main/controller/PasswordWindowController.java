package library.main.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.main.util.configuration.Admin;

public class PasswordWindowController implements Initializable {

	@FXML
	private ImageView unlockImageView;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Text passwordNotMatchedText;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.unlockImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/unlock.png")));
	}

	@FXML
	public void handleSubmitButton() {
		String password = passwordField.getText();
		if (Admin.isValid(Admin.getUsername(), password)) {
			this.passwordNotMatchedText.setVisible(false);
			System.exit(0);
		} else {
			this.passwordNotMatchedText.setVisible(true);
		}
	}

	@FXML
	public void handleCancelButton() {
		this.stage.close();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
