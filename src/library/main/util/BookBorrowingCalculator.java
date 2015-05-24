package library.main.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;


public class BookBorrowingCalculator {

	
	public static long calculatePinaltyPayment(
			BorrowingDaoMYSQL borrowingDaoMYSQL,
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL,
			long borrowingId) throws SQLException {
		LocalDate timeOfBorrowing = borrowingDaoMYSQL.read(borrowingId)
				.getTimeOfBorrowing();
		return calculatePinaltyPayment(calculationConfigurationDaoMYSQL,
				timeOfBorrowing);
	}

	public static long calculatePinaltyPayment(
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL,
			LocalDate timeOfBorrowing) throws SQLException {

		long dayOfBorrowing = ChronoUnit.DAYS.between(timeOfBorrowing,
				LocalDate.now());
		dayOfBorrowing -= calculationConfigurationDaoMYSQL.readLastConfig()
				.getBookMaxDaysOfBorrowing();
		long totalAmountOfPinalty = 0;
		if (dayOfBorrowing > 0) {
			totalAmountOfPinalty = dayOfBorrowing
					* calculationConfigurationDaoMYSQL.readLastConfig()
							.getBookPenaltyPayment();
		}
		return totalAmountOfPinalty;

	}
}
