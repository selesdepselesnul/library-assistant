package library.main.util.configuration;

import java.io.IOException;
import java.util.Properties;

public class Calculation {

	private static Properties calculationProperties;
	private static long memberMonthlyPayment;
	private static long memberPenaltyPayment;
	private static long bookPenaltyPayment;
	private static long bookMaxDaysOfBorrowing;

	static {
		calculationProperties = new Properties();
		try {
			calculationProperties
					.load(ClassLoader
							.getSystemResourceAsStream("library/main/resources/properties/calculation.properties"));
			memberMonthlyPayment = Long.parseLong(calculationProperties
					.getProperty("memberMonthlyPayment"));
			memberPenaltyPayment = Long.parseLong(calculationProperties
					.getProperty("memberPenaltyPayment"));
			bookPenaltyPayment = Long.parseLong(calculationProperties
					.getProperty("bookPenaltyPayment"));
			bookMaxDaysOfBorrowing = Long.parseLong(calculationProperties
					.getProperty("bookMaxDaysOfBorrowing"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setMemberMonthlyPayment(long memberMonthlyPayment) {
		calculationProperties.replace("memberMonthlyPayment",
				String.valueOf(memberMonthlyPayment));
		Calculation.memberMonthlyPayment = memberMonthlyPayment;
	}

	public static long getMemberPenaltyPayment() {
		return memberPenaltyPayment;
	}

	public static void setMemberPenaltyPayment(long memberPenaltyPayment) {
		calculationProperties.replace("memberPenaltyPayment",
				String.valueOf(memberPenaltyPayment));
		Calculation.memberPenaltyPayment = memberPenaltyPayment;
	}

	public static long getBookPenaltyPayment() {
		return bookPenaltyPayment;
	}

	public static void setBookPenaltyPayment(long bookPenaltyPayment) {
		calculationProperties.replace("bookPenaltyPayment",
				String.valueOf(bookPenaltyPayment));
		Calculation.bookPenaltyPayment = bookPenaltyPayment;
	}

	public static long getBookMaxDaysOfBorrowing() {
		return bookMaxDaysOfBorrowing;
	}

	public static void setBookMaxDaysOfBorrowing(long bookMaxDaysOfBorrowing) {
		calculationProperties.replace("bookMaxDaysOfBorrowing",
				String.valueOf(bookMaxDaysOfBorrowing));
		Calculation.bookMaxDaysOfBorrowing = bookMaxDaysOfBorrowing;
	}

	public static long getMemberMonthlyPayment() {
		return memberMonthlyPayment;
	}
	
	

}
