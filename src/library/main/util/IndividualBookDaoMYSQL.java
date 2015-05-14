package library.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import library.main.model.IndividualBook;

public class IndividualBookDaoMYSQL {
	private Connection connection;

	public IndividualBookDaoMYSQL(Connection connection) throws SQLException {

		this.connection = connection;
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SHOW TABLES");

		boolean isTableExist = false;

		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("IndividualBook")) {
				isTableExist = true;
			}
		}

		if (!isTableExist) {
			this.connection.createStatement().execute(
					"CREATE TABLE IndividualBook ( "
							+ "id BIGINT NOT NULL AUTO_INCREMENT, "
							+ "isbn CHAR(13) NOT NULL, "
							+ "isAvailable boolean NOT NULL DEFAULT TRUE, "
							+ "PRIMARY KEY (`id`), "
							+ "FOREIGN KEY (isbn) REFERENCES Book(isbn) )");
		}

	}

	public long write(IndividualBook individualBook) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("INSERT INTO IndividualBook (isbn) "
						+ "VALUES (?)");
		prep.setString(1, individualBook.getIsbn());
		prep.execute();
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT LAST_INSERT_ID()");
		resultSet.next();
		individualBook.setId(resultSet.getLong(1));
		resultSet.close();
		return individualBook.getId();
	}

	public List<IndividualBook> readAll() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM IndividualBook WHERE isAvailable = 1");

		List<IndividualBook> individualBooks = new ArrayList<>();
		while (resultSet.next()) {
			IndividualBook individualBook = new IndividualBook(
					resultSet.getLong("id"), resultSet.getString("isbn"));
			individualBook.setAvailable(resultSet.getBoolean("isAvailable"));
			individualBooks.add(individualBook);
		}
		resultSet.close();
		return individualBooks;
	}

	public List<IndividualBook> readAllBasedOnIsbn(String isbn)
			throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("SELECT * FROM IndividualBook "
						+ "WHERE isbn = ?");
		prep.setString(1, isbn);
		ResultSet resultSet = prep.executeQuery();

		List<IndividualBook> individualBooks = new ArrayList<>();

		while (resultSet.next()) {
			individualBooks.add(new IndividualBook(resultSet.getLong("id"),
					resultSet.getString("isbn")));
		}
		return individualBooks;

	}

	public String deleteBasedOnIsbn(String isbn) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("DELETE FROM IndividualBook WHERE isbn = ?");
		prep.setString(1, isbn);
		prep.execute();
		return isbn;
	}

	public long delete(IndividualBook individualBook) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("DELETE FROM IndividualBook WHERE id = ?");
		prep.setLong(1, individualBook.getId());
		prep.execute();
		return individualBook.getId();
	}

	public IndividualBook read(long bookId) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("SELECT * FROM IndividualBook WHERE id = ?");
		prep.setLong(1, bookId);
		ResultSet resultSet = prep.executeQuery();
		resultSet.next();
		IndividualBook individualBook = new IndividualBook(
				resultSet.getLong("id"), resultSet.getString("isbn"));
		individualBook.setAvailable(resultSet.getBoolean(3));
		resultSet.close();

		return individualBook;
	}

	public long update(IndividualBook individualBook) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("UPDATE IndividualBook "
						+ "SET isAvailable = ? WHERE id = ?");
		prep.setBoolean(1, individualBook.isAvailable());
		prep.setLong(2, individualBook.getId());
		prep.execute();
		return individualBook.getId();
	}

	public double countAvailable() throws SQLException {
		return count("SELECT COUNT(*) FROM IndividualBook WHERE isAvailable = true");
	}

	public double countNotAvailable() throws SQLException {
		return count("SELECT COUNT(*) FROM IndividualBook WHERE isAvailable = false");
	}

	private double count(String statement) throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				statement);
		resultSet.next();
		int count = resultSet.getInt(1);
		resultSet.close();
		return count;

	}

	public void add(IndividualBook individualBook, int amount)
			throws SQLException {
		for (int i = 0; i < amount; i++) {
			write(individualBook);
		}
	}

	public void deleteAll() throws SQLException {
		this.connection.createStatement().execute("DELETE FROM IndividualBook");
	}

	public double countAvailable(String isbn) throws SQLException {
		return count(isbn, true);
	}

	public double countNotAvailable(String isbn) throws SQLException {
		return count(isbn, false);
	}

	private double count(String isbn, boolean isAvailable) throws SQLException {
		PreparedStatement prepareStatement = this.connection
				.prepareStatement("SELECT COUNT(*) FROM IndividualBook "
						+ "WHERE isAvailable = ? AND isbn = ?");
		prepareStatement.setBoolean(1, isAvailable);
		prepareStatement.setString(2, isbn);
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		int amount = resultSet.getInt(1);
		resultSet.close();
		return amount;

	}

}
