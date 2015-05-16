package library.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import library.main.model.Borrowing;
import library.main.model.BorrowingHistory;
import library.main.model.IndividualBook;

public class BorrowingDaoMYSQL {

	private Connection connection;
	private IndividualBookDaoMYSQL individualBookDaoMysql;

	public BorrowingDaoMYSQL(Connection conn,
			IndividualBookDaoMYSQL individualBookDaoMYSQL) throws SQLException {
		this.connection = conn;
		this.individualBookDaoMysql = individualBookDaoMYSQL;

		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SHOW TABLES");

		boolean isTableExist = false;
		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("Borrowing")) {

				isTableExist = true;
			}
		}
		resultSet.close();

		if (!isTableExist) {
			this.connection
					.createStatement()
					.execute(
							"CREATE TABLE Borrowing ( "
									+ "id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, "
									+ "memberId BIGINT NOT NULL, "
									+ "bookId BIGINT NOT NULL, "
									+ "timeOfBorrowing timestamp NULL DEFAULT CURRENT_TIMESTAMP, "
									+ "timeOfReturning timestamp NULL DEFAULT NULL, "
									+ "FOREIGN KEY (memberId) REFERENCES Member(id), "
									+ "FOREIGN KEY (bookId) REFERENCES IndividualBook(id) )");
		}
	}

	public Borrowing read(long id) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("SELECT * FROM Borrowing WHERE id = ?");
		prepareStatement.setLong(1, id);
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		Borrowing borrowing = new Borrowing(resultSet.getLong("memberId"),
				resultSet.getLong("bookId"), resultSet.getDate(
						"timeOfBorrowing").toLocalDate());
		borrowing.setId(resultSet.getLong("id"));
		System.out.println("book id inside read borrowing = "
				+ resultSet.getLong("bookId"));
		resultSet.close();
		return borrowing;
	}

	public long write(Borrowing borrowing) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("INSERT INTO Borrowing (memberId, bookId) "
						+ "VALUES (?, ?)");
		IndividualBook individualBook = this.individualBookDaoMysql
				.read(borrowing.getBookId());
		if (individualBook.isAvailable()) {
			prep.setLong(1, borrowing.getMemberId());
			prep.setLong(2, borrowing.getBookId());
			prep.execute();
			ResultSet resultSet = this.connection.createStatement()
					.executeQuery("SELECT LAST_INSERT_ID()");
			resultSet.next();
			borrowing.setId(resultSet.getLong(1));
			individualBook.setAvailable(false);
			this.individualBookDaoMysql.update(individualBook);
			resultSet.close();
			prep.close();
		} else {
			throw new SQLException("The book is already borrowed by other id");
		}

		return borrowing.getId();
	}

	public List<Borrowing> readAll() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT b.id, b.memberId, b.bookId, b.timeOfBorrowing "
						+ "FROM Borrowing AS b "
						+ "INNER JOIN IndividualBook AS i "
						+ "ON b.bookId = i.id " 
						+ "WHERE i.isAvailable = 0 "
						+ "AND b.timeOfReturning IS NULL");
		List<Borrowing> borrowingList = new ArrayList<>();
		while (resultSet.next()) {
			Borrowing borrowing = new Borrowing(resultSet.getLong("memberId"),
					resultSet.getLong("bookId"), resultSet.getDate(
							"timeOfBorrowing").toLocalDate());
			borrowing.setId(resultSet.getLong("id"));
			borrowingList.add(borrowing);
		}
		resultSet.close();
		return borrowingList;
	}

	// public long delete(long borrowingId) throws SQLException {
	// Borrowing borrowing = read(borrowingId);
	// long bookId = borrowing.getBookId();
	// // PreparedStatement prep = this.connection
	// // .prepareStatement("UPDATE Borrowing " + "SET isReturned = 1 "
	// // + " WHERE id = ?");
	// // prep.setLong(1, borrowingId);
	// // prep.execute();
	//
	// IndividualBook individualBook = this.individualBookDaoMysql
	// .read(bookId);
	// individualBook.setAvailable(true);
	// this.individualBookDaoMysql.update(individualBook);
	//
	// return borrowingId;
	// }

	public List<BorrowingHistory> readBasedOnMemberId(long memberId)
			throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("SELECT b.bookId, b.timeOfBorrowing "
						+ "FROM Borrowing AS b "
						+ "INNER JOIN IndividualBook AS i "
						+ "ON b.bookId = i.id " 
						+ "WHERE i.isAvailable = 0 "
						+ "AND b.memberId = ? "
						+ "AND b.timeOfReturning IS NULL");
		prep.setLong(1, memberId);
		ResultSet resultSet = prep.executeQuery();

		List<BorrowingHistory> borrowingHistoryList = new ArrayList<>();
		while (resultSet.next()) {
			LocalDate timeOfBorrowing = resultSet.getDate(2).toLocalDate();
			BorrowingHistory borrowingHistory = new BorrowingHistory(
					resultSet.getLong(1), timeOfBorrowing,
					BookBorrowingCalculator
							.calculatePinaltyPayment(timeOfBorrowing));
			borrowingHistoryList.add(borrowingHistory);
		}
		resultSet.close();
		return borrowingHistoryList;
	}

	public int countBorrowed() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT COUNT(*) FROM Borrowing AS b "
						+ "INNER JOIN IndividualBook AS i "
						+ "ON b.bookId = i.id " 
						+ "WHERE i.isAvailable = 0");
		resultSet.next();
		int count = resultSet.getInt(1);
		return count;
	}

	public long updateTimeOfReturning(long idOfBorrowing) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("UPDATE Borrowing "
						+ "SET timeOfReturning = NOW()" + "WHERE id = ?");
		prepareStatement.setLong(1, idOfBorrowing);
		prepareStatement.execute();
		return idOfBorrowing;

	}


}
