package mysqlconfigurator.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import mysqlconfigurator.model.MYSQLConfiguration;
import mysqlconfigurator.util.MYSQLConfigurationDaoFS;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MysqlConfigurationController {

	@FXML
	private TextField mysqlUsernameTextField;

	@FXML
	private PasswordField mysqlPasswordField;

	@FXML
	private TextField mysqlPortTextField;

	@FXML
	private TextField mysqlHostnameTextField;

	@FXML
	private Button submitButton;

	@FXML
	private Button editButton;

	private MYSQLConfigurationDaoFS mysqlConfigurationDaoFS;

	private String configPath;

	public void writeToForm() throws IOException, ClassNotFoundException {
		mysqlConfigurationDaoFS = new MYSQLConfigurationDaoFS(this.configPath);

		if (!Files.exists(Paths.get(this.configPath))) {
			mysqlConfigurationDaoFS.writeConfiguration(new MYSQLConfiguration(
					"", "", "", ""));
		}

		MYSQLConfiguration mysqlConfiguration = mysqlConfigurationDaoFS.read();
		this.mysqlUsernameTextField.setText(mysqlConfiguration.getUsername());
		this.mysqlPasswordField.setText(mysqlConfiguration.getPassword());
		this.mysqlPortTextField.setText(mysqlConfiguration.getPort());
		this.mysqlHostnameTextField.setText(mysqlConfiguration.getHostname());
	}

	@FXML
	public void handleEditButton() {
		setMysqlConfiguratorFieldsDisable(false);
		this.submitButton.setDisable(false);
		this.editButton.setDisable(true);
	}

	@FXML
	public void handleSubmitButton() {
		try {
			setMysqlConfiguratorFieldsDisable(true);
			this.submitButton.setDisable(true);
			this.editButton.setDisable(false);
			this.mysqlConfigurationDaoFS
					.writeConfiguration(new MYSQLConfiguration(
							this.mysqlUsernameTextField.getText(),
							this.mysqlPasswordField.getText(),
							this.mysqlPortTextField.getText(),
							this.mysqlHostnameTextField.getText()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setMysqlConfiguratorFieldsDisable(boolean isDisable) {
		this.mysqlUsernameTextField.setDisable(isDisable);
		this.mysqlPasswordField.setDisable(isDisable);
		this.mysqlPortTextField.setDisable(isDisable);
		this.mysqlHostnameTextField.setDisable(isDisable);
	}

	public void setConfigPath(String configPath) throws ClassNotFoundException,
			IOException {
		this.configPath = configPath;
	}
}
