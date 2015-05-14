package library.main;

import java.sql.Connection;
import java.util.Properties;

import javafx.application.Application;
import javafx.stage.Stage;
import library.main.controller.LoginPanelController;
import library.main.util.BookDaoMYSQL;
import library.main.util.BorrowingDaoMYSQL;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.IndividualBookDaoMYSQL;
import library.main.util.MYSQLConnector;
import library.main.util.MemberDaoMYSQL;
import library.main.util.WindowLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			new WindowLoader(
					"library/main/view/LoginWindow.fxml",
					"Login Window",
					(fxmlLoader, stage) -> {
						try {

							Properties sqlProperties = new Properties();
							sqlProperties.load(ClassLoader
									.getSystemResourceAsStream("library/main/resources/sql.properties"));
							LoginPanelController loginPanelController = (LoginPanelController) fxmlLoader
									.getController();
							loginPanelController
									.setMemberDaoMYSQL(new MemberDaoMYSQL(
											new MYSQLConnector(sqlProperties)
													.getConnection()));
							Connection connectionWithSelectedDBase = new MYSQLConnector(
									sqlProperties, "library").getConnection();
							loginPanelController
									.setBookDaoMYSQL(new BookDaoMYSQL(
											connectionWithSelectedDBase));
							IndividualBookDaoMYSQL individualBookDaoMYSQL = new IndividualBookDaoMYSQL(
									connectionWithSelectedDBase);
							loginPanelController
									.setIndividualBookDaoMYSQL(individualBookDaoMYSQL);
							loginPanelController
									.setBorrowingDaoMYSQL(new BorrowingDaoMYSQL(
											connectionWithSelectedDBase,
											individualBookDaoMYSQL));
							loginPanelController.setLoginPanelStage(stage);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
							System.exit(1);
						}
					}).show(WindowLoader.SHOW_ONLY);
		} catch (Exception e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
