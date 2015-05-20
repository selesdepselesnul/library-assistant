package mysqlconfigurator;

import simpleui.util.WindowLoader;
import mysqlconfigurator.controller.MysqlConfigurationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			new WindowLoader(
					"mysqlconfigurator/MysqlConfigurator.fxml",
					"MYSQL Configurator",
					(fxmlLoader, stage) -> {
						try {
							MysqlConfigurationController mysqlConfigurationController = (MysqlConfigurationController) fxmlLoader
									.getController();
							mysqlConfigurationController
									.setConfigPath("resources/properties/sql.dat");
							mysqlConfigurationController.writeToForm();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
