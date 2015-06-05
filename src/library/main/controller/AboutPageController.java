package library.main.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutPageController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.aboutPageImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/logo.png")));
		handleLicenseButton();
	}

	@FXML
	private TextArea aboutTextArea;

	@FXML
	private ImageView aboutPageImageView;

	@FXML
	private void handleLicenseButton() {
		try {
			writeToAboutTextArea("library/main/resources/text/license.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeToAboutTextArea(String sourceURL) throws IOException {
		InputStream inputStream = ClassLoader
				.getSystemResourceAsStream(sourceURL);
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				inputStream));
		this.aboutTextArea.clear();
		buff.lines().forEach(line -> this.aboutTextArea.appendText(line+"\n"));
		buff.close();
	}

	@FXML
	private void handleCreditsButton() {
		try {
			writeToAboutTextArea("library/main/resources/text/developer.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}