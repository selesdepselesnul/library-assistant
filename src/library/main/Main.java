package library.main;

import java.sql.Connection;

import simpleui.util.ErrorMessageWindowLoader;
import simpleui.util.WindowLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import library.main.controller.LoginPanelController;
import library.main.util.LibraryAssistantResourcesPath;
import library.main.util.dao.filesystem.AdminDaoFS;
import library.main.util.dao.mysql.BookDaoMYSQL;
import library.main.util.dao.mysql.BookPenaltyDaoMYSQL;
import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;
import library.main.util.dao.mysql.IndividualBookDaoMYSQL;
import library.main.util.dao.mysql.MemberDaoMYSQL;
import library.main.util.dao.mysql.MemberPaymentDaoMYSQL;
import mysqlconfigurator.model.MYSQLConfiguration;
import mysqlconfigurator.util.MYSQLConfigurationDaoFS;
import mysqlconfigurator.util.MYSQLConnector;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			new WindowLoader(
					"library/main/view/LoginWindow.fxml",
					"Login Window",
					(fxmlLoader, stage) -> {
						try {

							MYSQLConfigurationDaoFS mysqlConfigurationDaoFS = new MYSQLConfigurationDaoFS(
									LibraryAssistantResourcesPath.SQL_CONFIG_PATH);
							MYSQLConfiguration mysqlConfiguration = mysqlConfigurationDaoFS
									.read();

							new MYSQLConnector(mysqlConfiguration)
									.getConnection()
									.createStatement()
									.execute(
											"CREATE DATABASE IF NOT EXISTS library");

							// and the life is begun :v
							LoginPanelController loginPanelController = (LoginPanelController) fxmlLoader
									.getController();
							loginPanelController
									.setAdminDaoMYSQL(new AdminDaoFS(
											LibraryAssistantResourcesPath.ADMIN_CONFIG_PATH));

							Connection connectionWithSelectedDBase = new MYSQLConnector(
									mysqlConfiguration, "library")
									.getConnection();

							loginPanelController
									.setCalculationConfigurationDaoMYSQL(new CalculationConfigurationDaoMYSQL(
											connectionWithSelectedDBase));
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
