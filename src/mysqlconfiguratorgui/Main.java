package mysqlconfiguratorgui;

import java.nio.file.Files;
import java.nio.file.Paths;

import simpleui.util.PasswordAskerWindow;
import simpleui.util.WindowLoader;
import library.main.model.Admin;
import library.main.util.LibraryAssistantResourcesPath;
import library.main.util.dao.filesystem.AdminDaoFS;
import mysqlconfiguratorgui.controller.MysqlConfigurationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			if (!Files.exists(Paths
					.get(LibraryAssistantResourcesPath.ADMIN_CONFIG_PATH))) {
				new AdminDaoFS(LibraryAssistantResourcesPath.ADMIN_CONFIG_PATH)
						.updateIfExist(new Admin("root", "indonesiaraya"));
			}

			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(
									"mysqlconfiguratorgui/view/MysqlConfigurator.fxml",
									"MYSQL Configurator",
									(fxmlLoader, stage) -> {
										try {
											MysqlConfigurationController mysqlConfigurationController = (MysqlConfigurationController) fxmlLoader
													.getController();
											mysqlConfigurationController
													.setConfigPath(LibraryAssistantResourcesPath.SQL_CONFIG_PATH);
											mysqlConfigurationController
													.writeToForm();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}).show(WindowLoader.SHOW_ONLY);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}, new AdminDaoFS(
							LibraryAssistantResourcesPath.ADMIN_CONFIG_PATH))
					.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
