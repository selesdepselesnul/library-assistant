package simpleui.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.main.controller.ErrorMessageWindowController;

public class ErrorMessageWindowLoader {
	private Stage stage;

	public ErrorMessageWindowLoader(String errorMessage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					ClassLoader
							.getSystemResource("library/main/view/ErrorMessageWindow.fxml"));
			AnchorPane root;
			root = (AnchorPane) fxmlLoader.load();
			this.stage = new Stage();
			ErrorMessageWindowController ErrorMessageWindowController = (ErrorMessageWindowController) fxmlLoader
					.getController();
			ErrorMessageWindowController.setErrorMessage(errorMessage);
			ErrorMessageWindowController.setStage(stage);
			this.stage.setScene(new Scene(root));
			this.stage.setTitle("Terjadi kesalahan !");
			this.stage.setResizable(false);
			this.stage.initModality(Modality.APPLICATION_MODAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		this.stage.showAndWait();
	}
}
