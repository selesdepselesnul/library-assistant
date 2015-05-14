package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.main.model.Admin;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.LibraryUtil;

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
		Properties prop = new Properties();
		try {
			prop.load(ClassLoader
					.getSystemResourceAsStream("library/main/resources/admin.properties"));
			Admin admin = new Admin("root", this.passwordField.getText());
			if (new LibraryUtil(prop).isValid(admin)) {
				this.passwordNotMatchedText.setVisible(false);
				System.exit(0);
			} else {
				this.passwordNotMatchedText.setVisible(true);
			}
		} catch (IOException | ClassNotFoundException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
