package library.main.util.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import library.main.model.Member;

public class MemberDaoMYSQL {
	private Connection connection;
	private PreparedStatement preparedStat;

	public MemberDaoMYSQL(Connection conn) throws SQLException {
		boolean isTableExist = false;
		this.connection = conn;
		Statement stat = this.connection.createStatement();
		ResultSet resultSet = stat.executeQuery("SHOW TABLES");

		while (resultSet.next()) {
			if (resultSet.getString(1).equalsIgnoreCase("Member")) {
				isTableExist = true;
			}
		}

		if (!isTableExist) {
			stat.execute("CREATE TABLE `Member` ("
					+ "`id` bigint(20) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(50) NOT NULL,"
					+ "`gender` char(1) NOT NULL,"
					+ "`email` varchar(50) DEFAULT NULL,"
					+ "`phone` varchar(12) DEFAULT NULL,"
					+ "`birthDate` date NOT NULL,"
					+ "`birthPlace` varchar(50) NOT NULL,"
					+ "`address` varchar(50) NOT NULL,"
					+ "`photo` TEXT,"
					+ "`timeOfRegistering` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "`timeOflastPayment` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (`id`) )");

		}

	}

	public long write(Member member) throws SQLException {
		String INSERTED_NEW_MEMBER = "INSERT INTO Member "
				+ "(name, gender, email, phone, birthDate, birthPlace, address, photo)"
				+ "VALUES (?, ?, ?, ? ,? ,? ,?, ?)";
		preparedStat = this.connection.prepareStatement(INSERTED_NEW_MEMBER);
		preparedStat.setString(1, member.getName());
		preparedStat.setString(2, member.getGender());
		preparedStat.setString(3, member.getEmail());
		preparedStat.setString(4, member.getPhone());
		preparedStat.setDate(5, Date.valueOf(member.getBirthDate()));
		preparedStat.setString(6, member.getBirthPlace());
		preparedStat.setString(7, member.getAddress());
		preparedStat.setString(8, member.getPhoto());
		preparedStat.execute();
		preparedStat.close();

		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT LAST_INSERT_ID()");
		resultSet.next();
		member.setId(resultSet.getLong(1));
		resultSet.close();
		return member.getId();
	}

	public long delete(long id) throws SQLException {

		final String DELETED_EXISTING_MEMBER = "DELETE FROM Member WHERE id = ? ";
		new MemberPaymentDaoMYSQL(connection).deleteBasedOnMemberId(id);
		this.preparedStat = this.connection
				.prepareStatement(DELETED_EXISTING_MEMBER);
		this.preparedStat.setLong(1, id);
		this.preparedStat.execute();
		return id;
	}

	public Member read(long id) throws SQLException {
		final String READ_MEMBER = "SELECT * FROM Member WHERE id = ?";
		this.preparedStat = this.connection.prepareStatement(READ_MEMBER);
		this.preparedStat.setLong(1, id);

		ResultSet resultSet = this.preparedStat.executeQuery();
		resultSet.next();

		Member member = new Member(resultSet.getString(2),
				resultSet.getString(3), resultSet.getString(4),
				resultSet.getString(5), resultSet.getDate(6).toLocalDate(),
				resultSet.getString(7), resultSet.getString(8));
		member.setId(resultSet.getLong(1));
		member.setPhoto(resultSet.getString(9));
		member.setTimeOfRegistering(resultSet.getDate(10).toLocalDate());
		member.setTimeOfLastPayment(resultSet.getDate(11).toLocalDate());
		resultSet.close();
		return member;

	}

	public long update(Member member) throws SQLException {
		final String UPDATED_MEMBER = "UPDATE Member "
				+ "SET name = ?, gender = ?, email = ?, phone = ?, "
				+ "birthDate = ?, birthPlace = ?, address = ?, photo = ? "
				+ "WHERE id = ?";

		this.preparedStat = this.connection.prepareStatement(UPDATED_MEMBER);

		this.preparedStat.setString(1, member.getName());
		this.preparedStat.setString(2, member.getGender());
		this.preparedStat.setString(3, member.getEmail());
		this.preparedStat.setString(4, member.getPhone());
		this.preparedStat.setDate(5, Date.valueOf(member.getBirthDate()));
		this.preparedStat.setString(6, member.getBirthPlace());
		this.preparedStat.setString(7, member.getAddress());
		this.preparedStat.setString(8, member.getPhoto());
		this.preparedStat.setLong(9, member.getId());
		this.preparedStat.execute();
		this.preparedStat.close();
		return member.getId();
	}

	public List<Member> readAll() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT * FROM Member");
		List<Member> memberList = new ArrayList<>();
		while (resultSet.next()) {
			Member member = new Member(resultSet.getString(2),
					resultSet.getString(3), resultSet.getString(4),
					resultSet.getString(5), resultSet.getDate(6).toLocalDate(),
					resultSet.getString(7), resultSet.getString(8));
			member.setId(resultSet.getLong(1));
			member.setPhoto(resultSet.getString(9));
			member.setTimeOfRegistering(resultSet.getDate(10).toLocalDate());
			member.setTimeOfLastPayment(resultSet.getDate(11).toLocalDate());
			memberList.add(member);
		}
		resultSet.close();
		return memberList;
	}

	public int countMember() throws SQLException {
		ResultSet resultSet = this.connection.createStatement().executeQuery(
				"SELECT COUNT(*) FROM Member");
		resultSet.next();
		int count = resultSet.getInt(1);
		resultSet.close();
		return count;
	}

	public int countIncomingMember(String month, String year)
			throws SQLException {
		String glob = year + "-" + month + "%";
		return countMemberBasedOnDate(glob);
	}

	public int countMemberBasedOnDate(String localDateGlob) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("SELECT COUNT(*) FROM Member WHERE timeOfRegistering LIKE ?");
		prep.setString(1, localDateGlob);
		ResultSet resultSet = prep.executeQuery();
		resultSet.next();
		int count = resultSet.getInt(1);
		resultSet.close();

		return count;

	}

	public void deleteAll() throws SQLException {
		new MemberPaymentDaoMYSQL(connection).deleteAll();
		this.connection.createStatement().execute("DELETE FROM Member");
	}

	public void updatePayment(Member member) throws SQLException {
		PreparedStatement prep = this.connection
				.prepareStatement("UPDATE Member SET timeOfLastPayment = ? WHERE id = ?");
		prep.setDate(1, Date.valueOf(member.getTimeOfLastPayment()));
		prep.setLong(2, member.getId());
		prep.execute();
		prep.close();
	}

}
