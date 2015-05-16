package library.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import library.main.model.Admin;

public class AdminDaoMYSQL {

	private Connection connection;

	public AdminDaoMYSQL(Connection connection) throws SQLException {
		this.connection = connection;
		boolean isDatabaseExist = false;
		Statement stat = this.connection.createStatement();
		ResultSet resultSet = stat.executeQuery("SHOW DATABASES");

		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("library")) {
				isDatabaseExist = true;
			}
		}

		if (!isDatabaseExist) {
			stat.execute("CREATE DATABASE library");
			stat.execute("USE library");
			stat.execute("CREATE TABLE Admin ( " + "id TINYINT, "
					+ "username TEXT, " + "password TEXT, " + "email TEXT )");
			stat.execute("INSERT INTO Admin VALUES "
					+ "(1, 'root', 'indonesiaraya', NULL)");
		} else {
			stat.execute("USE library");
		}

	}

	public void update(Admin admin) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("UPDATE Admin "
						+ "SET username = ?, password = ?, email = ? "
						+ "WHERE id = 1");
		prep.setString(1, admin.getUsername());
		prep.setString(2, admin.getPassword());
		prep.setString(3, admin.getEmail());
		prep.execute();
	}

	public Admin read() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM Admin");
		resultSet.next();
		Admin admin = new Admin(resultSet.getString("username"),
				resultSet.getString("password"), resultSet.getString("email"));
		resultSet.close();
		return admin;
	}
}
