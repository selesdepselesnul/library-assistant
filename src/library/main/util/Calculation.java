package library.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Calculation {

	private static long memberMonthlyPayment;
	private static long memberPenaltyPayment;
	private static long memberMaxDaysOfPayment;
	private static long bookPenaltyPayment;
	private static long bookMaxDaysOfBorrowing;
	private static Connection connection;

	public static void initConnection(Connection connection)
			throws SQLException {
		Calculation.connection = connection;
		ResultSet resultSet = connection.createStatement().executeQuery(
				"SHOW TABLES");
		boolean isTableExist = false;
		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("LibraryCalculation")) {
				isTableExist = true;
			}

		}

		if (!isTableExist) {
			Calculation.connection.createStatement().execute(
					"CREATE TABLE LibraryCalculation ( " + "id TINYINT, "
							+ "memberMonthlyPayment BIGINT NOT NULL, "
							+ "memberPenaltyPayment BIGINT NOT NULL, "
							+ "memberMaxDaysOfPayment BIGINT NOT NULL, "
							+ "bookPenaltyPayment BIGINT NOT NULL, "
							+ "bookMaxDaysOfBorrowing BIGINT NOT NULL )");
			Calculation.connection.createStatement().execute(
					"INSERT INTO LibraryCalculation "
							+ "VALUES (1, 10000, 5000, 365, 500, 7)");

		}

		resultSet = Calculation.connection.createStatement().executeQuery(
				"SELECT * FROM LibraryCalculation");
		resultSet.next();

		memberMonthlyPayment = resultSet.getLong("memberMonthlyPayment");
		memberPenaltyPayment = resultSet.getLong("memberPenaltyPayment");
		memberMaxDaysOfPayment = resultSet.getLong("memberMaxDaysOfPayment");
		bookPenaltyPayment = resultSet.getLong("bookPenaltyPayment");
		bookMaxDaysOfBorrowing = resultSet.getLong("bookMaxDaysOfBorrowing");
	}

	public static void setMemberMonthlyPayment(long memberMonthlyPayment)
			throws SQLException {
		doUpdate("memberMonthlyPayment", memberMonthlyPayment);
		Calculation.memberMonthlyPayment = memberMonthlyPayment;
	}

	public static long getMemberPenaltyPayment() {
		return memberPenaltyPayment;
	}

	public static void setMemberPenaltyPayment(long memberPenaltyPayment)
			throws SQLException {
		doUpdate("memberPenaltyPayment", memberPenaltyPayment);
		Calculation.memberPenaltyPayment = memberPenaltyPayment;
	}

	public static long getBookPenaltyPayment() {
		return bookPenaltyPayment;
	}

	public static void setBookPenaltyPayment(long bookPenaltyPayment)
			throws SQLException {
		doUpdate("bookPenaltyPayment", bookPenaltyPayment);
		Calculation.bookPenaltyPayment = bookPenaltyPayment;
	}

	public static void setMemberMaxDaysOfPayment(long memberMaxDaysOfPayment)
			throws SQLException {
		doUpdate("memberMaxDaysOfPayment", memberMaxDaysOfPayment);
		Calculation.memberMaxDaysOfPayment = memberMaxDaysOfPayment;
	}

	public static long getBookMaxDaysOfBorrowing() {
		return bookMaxDaysOfBorrowing;
	}

	public static void setBookMaxDaysOfBorrowing(long bookMaxDaysOfBorrowing)
			throws SQLException {
		doUpdate("bookMaxDaysOfBorrowing", bookMaxDaysOfBorrowing);
		Calculation.bookMaxDaysOfBorrowing = bookMaxDaysOfBorrowing;
	}

	public static long getMemberMonthlyPayment() {
		return memberMonthlyPayment;
	}

	private static void doUpdate(String columnName, long value)
			throws SQLException {
		PreparedStatement prepareStatement = Calculation.connection
				.prepareStatement("UPDATE LibraryCalculation " + "SET "
						+ columnName + " = ? WHERE id = 1");
		prepareStatement.setLong(1, value);
		prepareStatement.execute();
	}

	public static long getMemberMaxDaysOfPayment() {
		return memberMaxDaysOfPayment;
	}

}
