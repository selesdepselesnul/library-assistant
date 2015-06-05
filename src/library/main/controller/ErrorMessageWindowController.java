package library.main.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorMessageWindowController implements Initializable {

	@FXML
	private TextArea errorMesageTextArea;

	@FXML
	private ImageView errorImageView;

	@FXML
	private Text errorexplanationtext;

	@FXML
	private Text quotationText;

	private String errorMessage;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			this.errorImageView
					.setImage(new Image(
							ClassLoader
									.getSystemResourceAsStream("library/main/resources/images/error_icon.png")));
			this.quotationText
					.setText(readMessage("library/main/resources/text/quotations_about_mistakes.txt"));
			this.errorexplanationtext
					.setText(readMessage("library/main/resources/text/error_explanation.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.errorMesageTextArea.setText(this.errorMessage);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleLaterButton() {
		this.stage.close();
	}

	@FXML
	public void handleReportingButton() {
		this.stage.close();
	}

	private String readMessage(String urlString) throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream(urlString)));
		StringBuilder sb = new StringBuilder();
		buff.lines().forEach(line -> sb.append(line + "\n"));
		buff.close();
		return sb.toString();
	}

}
