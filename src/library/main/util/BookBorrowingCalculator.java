package library.main.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.main.util.configuration.Calculation;

public class BookBorrowingCalculator {

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
		dayOfBorrowing -= Calculation.getBookMaxDaysOfBorrowing();
		long totalAmountOfPinalty = 0;
		if (dayOfBorrowing > 0) {
			totalAmountOfPinalty = dayOfBorrowing
					* Calculation.getBookPenaltyPayment();
		}
		return totalAmountOfPinalty;

	}
}
