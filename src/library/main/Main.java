package library.main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;

import javafx.application.Application;
import javafx.stage.Stage;
import library.main.controller.LoginPanelController;
import library.main.util.AdminDaoMYSQL;
import library.main.util.BookDaoMYSQL;
import library.main.util.BookPenaltyDaoMYSQL;
import library.main.util.BorrowingDaoMYSQL;
import library.main.util.Calculation;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.IndividualBookDaoMYSQL;
import library.main.util.MYSQLConnector;
import library.main.util.MemberDaoMYSQL;
import library.main.util.MemberPaymentDaoMYSQL;
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
							sqlProperties.load(Files.newInputStream(Paths
									.get("resources/properties/sql.properties")));
							LoginPanelController loginPanelController = (LoginPanelController) fxmlLoader
									.getController();
							
							// and the life is begun :v
							loginPanelController
									.setAdminDaoMYSQL(new AdminDaoMYSQL(
											new MYSQLConnector(sqlProperties)
													.getConnection()));

							Connection connectionWithSelectedDBase = new MYSQLConnector(
									sqlProperties, "library").getConnection();

							Calculation
									.initConnection(connectionWithSelectedDBase);

							loginPanelController
									.setMemberDaoMYSQL(new MemberDaoMYSQL(
											connectionWithSelectedDBase));
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
							loginPanelController
									.setMemberMonthlyPaymentDaoMYSQL(new MemberPaymentDaoMYSQL(
											connectionWithSelectedDBase));
							loginPanelController
									.setBookPenaltyPaymentDaoMYSQL(new BookPenaltyDaoMYSQL(
											connectionWithSelectedDBase));
							loginPanelController.setLoginPanelStage(stage);
						} catch (Exception e) {
//							new ErrorMessageWindowLoader(e.getMessage());
							e.printStackTrace();
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
