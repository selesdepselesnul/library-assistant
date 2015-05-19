package library.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import library.main.model.CalculationConfiguration;

public class CalculationConfigurationDaoMYSQL {

	private Connection connection;

	public CalculationConfigurationDaoMYSQL(Connection connection)
			throws SQLException {
		this.connection = connection;
		ResultSet resultSet = connection.createStatement().executeQuery(
				"SHOW TABLES");
		boolean isTableExist = false;
		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase(
					"CalculationConfiguration")) {
				isTableExist = true;
			}

		}

		if (!isTableExist) {
			this.connection
					.createStatement()
					.execute(
							"CREATE TABLE CalculationConfiguration ( "
									+ "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
									+ "timeStampOfConfiguration TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
									+ "memberRoutinePayment BIGINT NOT NULL, "
									+ "memberPenaltyPayment BIGINT NOT NULL, "
									+ "memberMaxDaysOfPayment BIGINT NOT NULL, "
									+ "bookPenaltyPayment BIGINT NOT NULL, "
									+ "bookMaxDaysOfBorrowing BIGINT NOT NULL )");
			this.connection.createStatement().execute(
					"INSERT INTO CalculationConfiguration (memberRoutinePayment, "
							+ "memberPenaltyPayment, memberMaxDaysOfPayment, "
							+ "bookPenaltyPayment, bookMaxDaysOfBorrowing) "
							+ "VALUES (10000, 5000, 365, 500, 7)");

		}

	}

	public List<CalculationConfiguration> readAll() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM CalculationConfiguration");
		List<CalculationConfiguration> calculationConfigurationList = new ArrayList<>();
		while (resultSet.next()) {
			calculationConfigurationList.add(new CalculationConfiguration(
					resultSet.getLong("id"), resultSet
							.getTimestamp("timeStampOfConfiguration") + "",
					resultSet.getLong("memberRoutinePayment"), resultSet
							.getLong("memberPenaltyPayment"), resultSet
							.getLong("memberMaxDaysOfPayment"), resultSet
							.getLong("bookPenaltyPayment"), resultSet
							.getLong("bookMaxDaysOfBorrowing")));
		}
		return calculationConfigurationList;
	}

	public CalculationConfiguration readLastConfig() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM CalculationConfiguration");
		resultSet.last();
		CalculationConfiguration calculationConfiguration = new CalculationConfiguration(
				resultSet.getLong("id"),
				resultSet.getTimestamp("timeStampOfConfiguration") + "",
				resultSet.getLong("memberRoutinePayment"),
				resultSet.getLong("memberPenaltyPayment"),
				resultSet.getLong("memberMaxDaysOfPayment"),
				resultSet.getLong("bookPenaltyPayment"),
				resultSet.getLong("bookMaxDaysOfBorrowing"));
		resultSet.close();
		return calculationConfiguration;
	}

	public long write(CalculationConfiguration calculationConfiguration)
			throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("INSERT INTO CalculationConfiguration "
						+ "(memberRoutinePayment, memberPenaltyPayment, "
						+ "memberMaxDaysOfPayment, bookPenaltyPayment, bookMaxDaysOfBorrowing) "
						+ "VALUES (?, ?, ?, ?, ?)");
		prepareStatement.setLong(1,
				calculationConfiguration.getMemberRoutinePayment());
		prepareStatement.setLong(2,
				calculationConfiguration.getMemberPenaltyPayment());
		prepareStatement.setLong(3,
				calculationConfiguration.getMemberMaxDaysOfPayment());
		prepareStatement.setLong(4,
				calculationConfiguration.getBookPenaltyPayment());
		prepareStatement.setLong(5,
				calculationConfiguration.getBookMaxDaysOfBorrowing());
		prepareStatement.execute();

		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT LAST_INSERT_ID()");
		resultSet.next();
		long id = resultSet.getLong(1);
		resultSet.close();
		prepareStatement.close();
		return id;

	}
}
