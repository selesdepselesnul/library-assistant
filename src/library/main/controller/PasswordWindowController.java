package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.main.util.dao.filesystem.AdminDaoFS;

public class PasswordWindowController implements Initializable {

	@FXML
	private ImageView unlockImageView;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Text passwordNotMatchedText;

	private Stage stage;

	private AdminDaoFS adminDaoMYSQL;

	private boolean isCloseSystem;

	private Runnable runnable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.unlockImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/unlock.png")));
		this.isCloseSystem = false;
	}

	@FXML
	public void handleSubmitButton() {
		try {
			String password = passwordField.getText();
			if (this.adminDaoMYSQL.read().getPassword().equals(password)) {
				this.passwordNotMatchedText.setVisible(false);
				if (isCloseSystem) {
					System.exit(0);
				} else {
					this.runnable.run();
					stage.close();
				}
			} else {
				this.passwordNotMatchedText.setVisible(true);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleCancelButton() {
		this.stage.close();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setAdminDaoMYSQL(AdminDaoFS adminDaoMYSQL) {
		this.adminDaoMYSQL = adminDaoMYSQL;
	}

	public void setCloseSystem(boolean isCloseSystem) {
		this.isCloseSystem = isCloseSystem;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

}
