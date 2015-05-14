package library.main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MYSQLConnector {
	private Properties properties;
	private String databaseName;

	public MYSQLConnector(Properties properties) {
		this.properties = properties;
		this.databaseName = "";
	}

	public MYSQLConnector(Properties properties, String databaseName) {
		this.properties = properties;
		this.databaseName = databaseName;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://" + properties.getProperty("hostname") + ":"
						+ properties.getProperty("port") + "/" + databaseName,
				properties.getProperty("username"),
				properties.getProperty("password"));
	}

}
