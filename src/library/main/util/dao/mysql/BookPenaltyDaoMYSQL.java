package library.main.util.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import library.main.model.BookPenalty;

public class BookPenaltyDaoMYSQL {

	private Connection connection;

	public BookPenaltyDaoMYSQL(Connection connection) throws SQLException {
		this.connection = connection;
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SHOW TABLES");

		boolean isAvailable = false;
		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("BookPenaltyPayment")) {
				isAvailable = true;
			}
		}
		resultSet.close();

		if (!isAvailable) {
			this.connection
					.createStatement()
					.execute(
							"CREATE TABLE BookPenaltyPayment ( "
									+ "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
									+ "amount BIGINT, "
									+ "borrowingId BIGINT, "
									+ "timeOfPayment TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
									+ "FOREIGN KEY (borrowingId) REFERENCES Borrowing (id) )");
		}
	}

	public long write(BookPenalty bookPenalty) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("INSERT INTO BookPenaltyPayment (borrowingId, amount) "
						+ "VALUES (?, ?)");
		prepareStatement.setLong(1, bookPenalty.getBorrowingId());
		prepareStatement.setLong(2, bookPenalty.getBookPenaltyAmount());
		prepareStatement.execute();

		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT LAST_INSERT_ID()");
		resultSet.next();
		bookPenalty.setId(resultSet.getLong(1));
		resultSet.close();
		return bookPenalty.getId();
	}

	public long sumBasedOnDate(LocalDate localDate) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("SELECT SUM(amount) "
						+ "FROM BookPenaltyPayment "
						+ "WHERE DATE(timeOfPayment) = ?");
		prepareStatement.setDate(1, Date.valueOf(localDate));
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		long count = resultSet.getLong(1);

		return count;
	}
}
