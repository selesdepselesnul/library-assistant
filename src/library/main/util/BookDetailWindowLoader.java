package library.main.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.main.controller.BookDetailController;

public class BookDetailWindowLoader {
	private Stage stage;

	public BookDetailWindowLoader(String isbn, BookDaoMYSQL bookDaoMYSQL)
			throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
				ClassLoader
						.getSystemResource("library/main/view/BookDetail.fxml"));
		AnchorPane root = (AnchorPane) fxmlLoader.load();
		stage = new Stage();
		BookDetailController bookDetailController = (BookDetailController) fxmlLoader
				.getController();
		bookDetailController.setBookIsbn(isbn);
		bookDetailController.setBookDaoMYSQL(bookDaoMYSQL);
		stage.setScene(new Scene(root));
		stage.setTitle("Form Buku");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
	}

	public void show() {

		stage.showAndWait();
	}

}
