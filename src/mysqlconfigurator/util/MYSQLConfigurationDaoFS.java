package mysqlconfigurator.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import mysqlconfigurator.model.MYSQLConfiguration;

public class MYSQLConfigurationDaoFS {

	private String configPath;

	public MYSQLConfigurationDaoFS(String configPath) throws IOException,
			ClassNotFoundException {
		this.configPath = configPath;
	}

	public void writeConfiguration(MYSQLConfiguration mysqlConfiguration)
			throws IOException {

		if (Files.exists(Paths.get(this.configPath))) {
			Files.delete(Paths.get(this.configPath));
		}
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				Files.newOutputStream(Paths.get(this.configPath)));
		objectOutputStream.writeObject(mysqlConfiguration);
		objectOutputStream.close();
	}

	public MYSQLConfiguration read() throws IOException, ClassNotFoundException {

		ObjectInputStream objectInputStream = new ObjectInputStream(
				Files.newInputStream(Paths.get(this.configPath)));
		MYSQLConfiguration mysqlConfiguration = (MYSQLConfiguration) objectInputStream
				.readObject();
		objectInputStream.close();
		return mysqlConfiguration;
	}
}
