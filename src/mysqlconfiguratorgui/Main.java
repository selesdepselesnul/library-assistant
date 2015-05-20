package mysqlconfiguratorgui;

import java.nio.file.Files;
import java.nio.file.Paths;

import simpleui.util.PasswordAskerWindow;
import simpleui.util.WindowLoader;
import library.main.model.Admin;
import library.main.util.dao.filesystem.AdminDaoFS;
import mysqlconfiguratorgui.controller.MysqlConfigurationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			String configPath = "resources/properties/";
			String adminPath = configPath + "admin.dat";
			if (!Files.exists(Paths.get(adminPath))) {
				new AdminDaoFS(adminPath).updateIfExist(new Admin("root",
						"indonesiaraya"));
			}

			new PasswordAskerWindow(false, () -> {
				try {
					new WindowLoader(
							"mysqlconfiguratorgui/view/MysqlConfigurator.fxml",
							"MYSQL Configurator",
							(fxmlLoader, stage) -> {
								try {
									MysqlConfigurationController mysqlConfigurationController = (MysqlConfigurationController) fxmlLoader
											.getController();
									mysqlConfigurationController
									.setConfigPath(configPath + "sql.dat");
									mysqlConfigurationController.writeToForm();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}).show(WindowLoader.SHOW_ONLY);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}, new AdminDaoFS(adminPath)).showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
