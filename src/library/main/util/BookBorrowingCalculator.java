package library.main.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookBorrowingCalculator {

	private static final int MAX_AMOUNT_OF_BORROWING = 7;
	private static final long AMOUNT_OF_PINALTY = 500;

	public static long calculatePinaltyPayment(
			BorrowingDaoMYSQL borrowingDaoMYSQL, long borrowingId)
			throws SQLException {
		LocalDate timeOfBorrowing = borrowingDaoMYSQL.read(borrowingId)
				.getTimeOfBorrowing();
		return calculatePinaltyPayment(timeOfBorrowing);
	}

	public static long calculatePinaltyPayment(LocalDate timeOfBorrowing) {
		long dayOfBorrowing = ChronoUnit.DAYS.between(timeOfBorrowing,
				LocalDate.now());
		dayOfBorrowing -= MAX_AMOUNT_OF_BORROWING;
		long totalAmountOfPinalty = 0;
		if (dayOfBorrowing > 0) {
			totalAmountOfPinalty = dayOfBorrowing * AMOUNT_OF_PINALTY;
		}
		return totalAmountOfPinalty;

	}
}
