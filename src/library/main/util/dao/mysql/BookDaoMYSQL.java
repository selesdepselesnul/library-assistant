package library.main.util.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import simpleui.util.ErrorMessageWindowLoader;
import library.main.model.Book;
import library.main.model.IndividualBook;

public class BookDaoMYSQL {

	private Connection connection;

	public BookDaoMYSQL(Connection connection) throws SQLException {

		this.connection = connection;
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SHOW TABLES");

		boolean isTableExist = false;

		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("Book")) {
				isTableExist = true;
			}
		}

		if (!isTableExist) {
			this.connection.createStatement().execute(
					"CREATE TABLE `Book` ( " + "`isbn` char(13) NOT NULL, "
							+ "`title` text NOT NULL, "
							+ "`authors` text NOT NULL, "
							+ "`publisher` text NOT NULL, "
							+ "`dateOfPublishing` DATE NOT NULL, "
							+ "`category` text, " + "PRIMARY KEY (`isbn`) ) ");

		}

	}

	public String write(Book book) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("INSERT INTO Book "
						+ "VALUES (?, ?, ?, ?, ?, ?)");
		prep.setString(1, book.getIsbn());
		prep.setString(2, book.getTitle());
		prep.setString(3, book.getAuthors());
		prep.setString(4, book.getPublisher());
		prep.setDate(5, Date.valueOf(book.getDateOfPublishing()));
		prep.setString(6, book.getCategory());
		prep.execute();

		new IndividualBookDaoMYSQL(connection).add(
				new IndividualBook(book.getIsbn()), book.getAmount());
		return book.getIsbn();
	}

	public Book read(String isbn) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("SELECT * FROM Book WHERE isbn = ?");
		prep.setString(1, isbn);
		ResultSet resultSet = prep.executeQuery();
		resultSet.next();

		Book book = new Book(resultSet.getString("title"), resultSet.getDate(
				"dateOfPublishing").toLocalDate(),
				resultSet.getString("authors"), resultSet.getString("isbn"),
				resultSet.getString("publisher"),
				resultSet.getString("category"), new IndividualBookDaoMYSQL(
						connection).readAllBasedOnIsbn(isbn).size());
		prep = this.connection
				.prepareStatement("SELECT COUNT(*) FROM IndividualBook "
						+ "WHERE isbn = ? AND isAvailable = 1");
		prep.setString(1, book.getIsbn());
		resultSet = prep.executeQuery();
		resultSet.next();
		book.setAmount(resultSet.getInt(1));
		resultSet.close();
		return book;
	}

	public List<Book> readAll() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM Book");
		List<Book> bookList = new ArrayList<>();

		while (resultSet.next()) {
			IndividualBookDaoMYSQL individualBookDaoMYSQL = new IndividualBookDaoMYSQL(
					connection);
			String isbnNumber = resultSet.getString("isbn");
			Book book = new Book(resultSet.getString("title"), resultSet
					.getDate("dateOfPublishing").toLocalDate(),
					resultSet.getString("authors"), isbnNumber,
					resultSet.getString("publisher"),
					resultSet.getString("category"), individualBookDaoMYSQL
							.readAllBasedOnIsbn(isbnNumber).size());
			book.setAvailableAmount(individualBookDaoMYSQL
					.countAvailable(isbnNumber));
			book.setNotAvailableAmount(individualBookDaoMYSQL
					.countNotAvailable(isbnNumber));
			bookList.add(book);
		}

		resultSet.close();
		return bookList;
	}

	public String update(Book book) {
		try {
			PreparedStatement prep = this.connection
					.prepareStatement("UPDATE Book SET title = ?, "
							+ "dateOfPublishing = ?, authors = ?, publisher = ?, "
							+ "category = ? WHERE isbn = ?");
			prep.setString(1, book.getTitle());
			prep.setDate(2, Date.valueOf(book.getDateOfPublishing()));
			prep.setString(3, book.getAuthors());
			prep.setString(4, book.getPublisher());
			prep.setString(5, book.getCategory());
			prep.setString(6, book.getIsbn());
			prep.execute();
			prep.close();
			new IndividualBookDaoMYSQL(this.connection).deleteBasedOnIsbn(book
					.getIsbn());
			new IndividualBookDaoMYSQL(connection).add(
					new IndividualBook(book.getIsbn()), book.getAmount());
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage());
		}
		return book.getIsbn();
	}

	public String delete(String isbn) throws SQLException {
		IndividualBookDaoMYSQL individualBookDaoMYSQL = new IndividualBookDaoMYSQL(
				this.connection);
		individualBookDaoMYSQL.deleteBasedOnIsbn(isbn);

		PreparedStatement prep = this.connection
				.prepareStatement("DELETE FROM Book WHERE isbn = ?");
		prep.setString(1, isbn);
		prep.execute();

		return isbn;
	}

	public boolean isEmpty() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM Book");
		boolean isEmpty = !resultSet.first();
		resultSet.close();
		return isEmpty;
	}

	public void deleteAll() throws SQLException {
		IndividualBookDaoMYSQL individualBookDaoMYSQL = new IndividualBookDaoMYSQL(
				this.connection);
		individualBookDaoMYSQL.deleteAll();

		this.connection.createStatement().execute("DELETE FROM Book");
	}

}
