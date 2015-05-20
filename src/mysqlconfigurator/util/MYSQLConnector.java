package mysqlconfigurator.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mysqlconfigurator.model.MYSQLConfiguration;


public class MYSQLConnector {
	private String databaseName;
	private MYSQLConfiguration mysqlConfiguration;

	public MYSQLConnector(MYSQLConfiguration mysqlConfiguration,
			String databaseName) {
		this.mysqlConfiguration = mysqlConfiguration;
		this.databaseName = databaseName;
	}

	public MYSQLConnector(MYSQLConfiguration mysqlConfiguration) {
		this.mysqlConfiguration = mysqlConfiguration;
		this.databaseName = "";
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://"
				+ this.mysqlConfiguration.getHostname() + ":"
				+ this.mysqlConfiguration.getPort() + "/" + databaseName,
				this.mysqlConfiguration.getUsername(),
				this.mysqlConfiguration.getPassword());
	}
	
}
