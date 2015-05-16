package library.main.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import library.main.model.MemberMonthlyPayment;

public class MemberMonthlyPaymentDaoMYSQL {

	private Connection connection;

	public MemberMonthlyPaymentDaoMYSQL(Connection connection)
			throws SQLException {
		this.connection = connection;
		this.connection.setSchema("library");
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SHOW TABLES");

		boolean isExist = false;
		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("MemberMonthlyPayment")) {
				isExist = true;
			}
		}

		resultSet.close();

		if (!isExist) {
			this.connection
					.createStatement()
					.execute(
							"CREATE TABLE MemberMonthlyPayment ( "
									+ "id BIGINT(20) AUTO_INCREMENT, "
									+ "dateOfPayment timestamp NULL DEFAULT CURRENT_TIMESTAMP, "
									+ "memberId BIGINT(20) NOT NULL, "
									+ "amount BIGINT NOT NULL, "
									+ "paymentMode ENUM ('monthly' , 'penalty'), "
									+ "PRIMARY KEY (id), "
									+ "FOREIGN KEY (memberId) REFERENCES Member (id) )");
		}

	}

	public long write(MemberMonthlyPayment memberMonthlyPayment)
			throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("INSERT INTO MemberMonthlyPayment (memberId, amount, paymentMode) "
						+ "VALUES (?, ?, ?)");
		prepareStatement.setLong(1, memberMonthlyPayment.getMemberId());
		prepareStatement.setLong(2, memberMonthlyPayment.getAmount());
		prepareStatement.setString(3, memberMonthlyPayment.getPaymentMode());
		prepareStatement.execute();

		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT LAST_INSERT_ID()");
		resultSet.next();
		memberMonthlyPayment.setId(resultSet.getLong(1));
		resultSet.close();
		return memberMonthlyPayment.getId();
	}

	public long sumMonthlyBasedOnDate(LocalDate localDate) throws SQLException {
		return sumPayment(localDate, MemberMonthlyPayment.MONTHLY);
	}

	private long sumPayment(LocalDate localDate, String paymentMode)
			throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("SELECT SUM(amount) "
						+ "FROM MemberMonthlyPayment "
						+ "WHERE DATE(dateOfPayment) = ? "
						+ "AND paymentMode = ?");
		prepareStatement.setDate(1, Date.valueOf(localDate));
		prepareStatement.setString(2, paymentMode);
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		long amount = resultSet.getLong(1);
		resultSet.close();
		return amount;
	}

	public long sumPenaltyBasedOnDate(LocalDate localDate) throws SQLException {
		return sumPayment(localDate, MemberMonthlyPayment.PENALTY);
	}

	public void deleteBasedOnMemberId(long memberId) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("DELETE FROM MemberMonthlyPayment "
						+ "WHERE memberId = ? ");
		prepareStatement.setLong(1, memberId);
		prepareStatement.execute();
	}

	public void deleteAll() throws SQLException {
		this.connection.createStatement().execute(
				"DELETE FROM MemberMonthlyPayment");
	}
}
