package library.main.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;

/**
 * class that is used to calculate penalty payment of borrowing book
 * 
 * @since 1.0.0
 * @author moch deden r s
 *
 */
public class BookBorrowingCalculator {

	/**
	 * method to calculate penalty payment for borrowing book, for the other
	 * overloaded method
	 * {@link #calculatePinaltyPayment(CalculationConfigurationDaoMYSQL, LocalDate)}
	 * can be used
	 * 
	 * @param borrowingDaoMYSQL
	 *            {@link BorrowingDaoMYSQL}
	 * @param calculationConfigurationDaoMYSQL
	 *            {@link CalculationConfigurationDaoMYSQL}
	 * @param borrowingId
	 *            idOfBorrowing book
	 * @return penaltyPayment penalty payment of borrowing book
	 * @throws SQLException
	 */
	public static long calculatePenaltyPayment(
			BorrowingDaoMYSQL borrowingDaoMYSQL,
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL,
			long borrowingId) throws SQLException {
		LocalDate timeOfBorrowing = borrowingDaoMYSQL.read(borrowingId)
				.getTimeOfBorrowing();
		return calculatePinaltyPayment(calculationConfigurationDaoMYSQL,
				timeOfBorrowing);
	}

	/**
	 * @see #calculatePenaltyPayment(BorrowingDaoMYSQL,
	 *      CalculationConfigurationDaoMYSQL, long)
	 * 
	 * @param calculationConfigurationDaoMYSQL
	 *            {@link CalculationConfigurationDaoMYSQL}
	 * @param timeOfBorrowing
	 *            time of borrowing the book
	 * @return penaltyPayment penalty payment of borrowing book
	 * @throws SQLException
	 */
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
