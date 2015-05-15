package library.main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import library.main.model.Book;
import library.main.model.Borrowing;
import library.main.model.IndividualBook;
import library.main.util.BookDaoMYSQL;
import library.main.util.BorrowingDaoMYSQL;
import library.main.util.IndividualBookDaoMYSQL;
import library.main.util.MYSQLConnector;
import library.main.util.MemberDaoMYSQL;

public class DatabaseFiller {
	private static List<LocalDate> timeOfLastPaymentList;

	public static void main(String[] args) throws SQLException, IOException,
			InterruptedException {
		Properties properties = new Properties();
		properties
				.load(ClassLoader
						.getSystemResourceAsStream("library/main/resources/sql.properties"));
		System.out.println("isi atau bersihkan ?(0/1) ");
		Scanner scanner = new Scanner(System.in);
		int choosenOption = scanner.nextInt();
		if (choosenOption == 0) {
			System.out.println("Masukan data bodong tahun terakhir kali bayar");
			System.out.print("Masukan tahun terendah (inclusive) :  ");
			int lower = scanner.nextInt();
			System.out.print("Musukan tahun tertinggi (exclusive) : ");
			int upper = scanner.nextInt();

			timeOfLastPaymentList = new ArrayList<>();
			String nameTemplate = "orang ke - ";
			String birthPlaceTemplate = "tempat ke - ";
			String addressTemplate = "jalan ke - ";
			String isbnLowerTemplate = "139783161";
			properties
					.load(ClassLoader
							.getSystemResourceAsStream("library/main/resources/sql.properties"));
			Connection connectionWithoutDatabase = DriverManager.getConnection(
					"jdbc:mysql://" + properties.getProperty("hostname") + ":"
							+ properties.getProperty("port"),
					properties.getProperty("username"),
					properties.getProperty("password"));
			new MemberDaoMYSQL(connectionWithoutDatabase);

			System.out.print("Berapa data bodong yang ingin dimasukan ? ");
			int count = scanner.nextInt();
			scanner.close();
			Random rand = new Random();
			int[] randomYears = rand.ints(lower, upper).limit(count).toArray();
			int[] randomMonths = rand.ints(1, 13).limit(count).toArray();
			int[] randomDates = rand.ints(1, 29).limit(count).toArray();
			int[] randomYearsSubtractersForJoining = rand.ints(1, 4)
					.limit(count).toArray();
			int[] randomYearsSubtractersForBirthDate = rand.ints(16, 40)
					.limit(count).toArray();
			int[] randomGenders = rand.ints(0, 2).limit(count).toArray();
			int[] randomOfBookAmount = rand.ints(1, 100).limit(count).toArray();
			Connection connectionWithDatabase = new MYSQLConnector(properties,
					"library").getConnection();
			BookDaoMYSQL bookDaoMYSQL = new BookDaoMYSQL(connectionWithDatabase);
			List<Long> memberIdList = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				PreparedStatement prep = connectionWithoutDatabase
						.prepareStatement("INSERT INTO Member "
								+ "(name, gender, birthDate, birthPlace, address, "
								+ " timeOfRegistering, timeOfLastPayment, photo) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				timeOfLastPaymentList.add(LocalDate.of(randomYears[i],
						randomMonths[i], randomDates[i]));
				String name = nameTemplate + " " + i;
				prep.setString(1, name);
				System.out
						.println("                 Data Anggota                ");
				System.out
						.println("===============================================");
				System.out.println("nama : " + name);
				String gender = getRandomGender(randomGenders[i]);
				prep.setString(2, gender);
				System.out.println("kelamin : " + gender);
				Date birthDate = Date.valueOf(timeOfLastPaymentList.get(i)
						.minusYears(randomYearsSubtractersForBirthDate[i])
						.plusDays(randomDates[i]));
				prep.setDate(3, birthDate);
				System.out.println("Tgl. lahir : " + birthDate.toString());
				String birthPlace = birthPlaceTemplate + " " + i;
				prep.setString(4, birthPlace);
				System.out.println("Tempat lahir : " + birthPlace);
				String address = addressTemplate + i;
				prep.setString(5, address);
				System.out.println("alamat :" + address);
				Date timeOfRegistering = Date.valueOf(timeOfLastPaymentList
						.get(i).minusYears(randomYearsSubtractersForJoining[i])
						.minusDays(randomDates[i]));
				prep.setDate(6, timeOfRegistering);
				System.out.println("tgl. bergabung : " + timeOfRegistering);
				prep.setDate(7, Date.valueOf(timeOfLastPaymentList.get(i)));
				System.out.println("Tanggal terakhir bayar : "
						+ timeOfLastPaymentList.get(i));
				String photoPath = "NONE";
				prep.setString(8, photoPath);
				System.out
						.println("===============================================");
				System.out.println("\n\n");
				prep.execute();
				ResultSet resultSet = connectionWithoutDatabase
						.createStatement().executeQuery(
								"SELECT LAST_INSERT_ID()");
				resultSet.next();
				memberIdList.add(resultSet.getLong(1));
				resultSet.close();
				prep.close();
				Book book = new Book("judul - " + i, birthDate.toLocalDate()
						.minusWeeks(i).plusDays(randomDates[i]), "pengarang - "
						+ randomOfBookAmount[i], isbnLowerTemplate + i,
						"publisher - " + randomMonths[i], "category - "
								+ randomOfBookAmount[i] + randomDates[i],
						randomOfBookAmount[i]);

				System.out
						.println("                  Data Buku                  ");
				System.out
						.println("=============================================");
				System.out.println("isbn        :  " + book.getIsbn());
				System.out.println("judul       :  " + book.getTitle());
				System.out.println("pengarang   :  " + book.getAuthors());
				System.out.println("penerbit    :  " + book.getPublisher());
				System.out.println("category    :  " + book.getCategory());
				System.out.println("tgl. terbit :  "
						+ book.getDateOfPublishing());
				System.out.println("jumlah      :  " + book.getAmount());
				System.out
						.println("=============================================");
				System.out.println("\n\n\n\n\n");
				bookDaoMYSQL.write(book);

			}
			List<IndividualBook> individualBookList = new IndividualBookDaoMYSQL(
					connectionWithDatabase).readAll();
			System.out.println("Data bodong selesai diisikan");
			int[] randomMember = rand.ints(0, count).limit(count).toArray();
			for (int i = 0; i < count; i++) {
				long memberId = memberIdList.get(randomMember[i]);
				long bookId = individualBookList.get(i).getId();
				try {
					long borrowingId = new BorrowingDaoMYSQL(
							connectionWithDatabase, new IndividualBookDaoMYSQL(
									connectionWithDatabase))
							.write(new Borrowing(memberId, bookId));
					PreparedStatement prep = connectionWithDatabase
							.prepareStatement("UPDATE Borrowing "
									+ "SET timeOfBorrowing = ? WHERE id = ? ");
					prep.setDate(1, Date.valueOf(timeOfLastPaymentList.get(i)));
					prep.setLong(2, borrowingId);
					prep.execute();
					PreparedStatement preparedStatement = prep = connectionWithDatabase
							.prepareStatement("UPDATE IndividualBook "
									+ "SET isAvailable = ? WHERE id = ?");
					preparedStatement.setBoolean(1, false);
					preparedStatement.setLong(2, bookId);
					preparedStatement.execute();


					System.out
							.println("              Data Peminjaman            ");
					System.out
							.println("=========================================");
					System.out.println("Anggota nomor       = " + memberId);
					System.out.println("Meminjam buku nomor = " + bookId);
					System.out
							.println("==========================================");
					System.out.println("\n\n");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("Alhamudillah selesai...........");
		} else {
			new MYSQLConnector(properties).getConnection().createStatement()
					.execute(" DROP DATABASE IF EXISTS library");

			Files.walkFileTree(Paths.get("library"), new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file,
						IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			System.out.println("Database sudah di bersihkan !");
			scanner.close();

		}
	}

	public static String getRandomGender(int genderInt) {
		if (genderInt == 0) {
			return "M";
		} else {
			return "F";
		}
	}
}
