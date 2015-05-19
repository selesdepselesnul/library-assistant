package library.main.model;

public class CalculationConfiguration {

	private long id;
	private long memberRoutinePayment;
	private long memberPenaltyPayment;
	private long memberMaxDaysOfPayment;
	private long bookPenaltyPayment;
	private long bookMaxDaysOfBorrowing;
	private String timeStampOfConfiguration;

	public String getTimeStampOfConfiguration() {
		return timeStampOfConfiguration;
	}

	public void setTimeStampOfConfiguration(String timeStampOfConfiguration) {
		this.timeStampOfConfiguration = timeStampOfConfiguration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMemberRoutinePayment() {
		return memberRoutinePayment;
	}

	public void setMemberRoutinePayment(long memberRoutinePayment) {
		this.memberRoutinePayment = memberRoutinePayment;
	}

	public long getMemberPenaltyPayment() {
		return memberPenaltyPayment;
	}

	public void setMemberPenaltyPayment(long memberPenaltyPayment) {
		this.memberPenaltyPayment = memberPenaltyPayment;
	}

	public long getMemberMaxDaysOfPayment() {
		return memberMaxDaysOfPayment;
	}

	public void setMemberMaxDaysOfPayment(long memberMaxDaysOfPayment) {
		this.memberMaxDaysOfPayment = memberMaxDaysOfPayment;
	}

	public long getBookPenaltyPayment() {
		return bookPenaltyPayment;
	}

	public void setBookPenaltyPayment(long bookPenaltyPayment) {
		this.bookPenaltyPayment = bookPenaltyPayment;
	}

	public long getBookMaxDaysOfBorrowing() {
		return bookMaxDaysOfBorrowing;
	}

	public void setBookMaxDaysOfBorrowing(long bookMaxDaysOfBorrowing) {
		this.bookMaxDaysOfBorrowing = bookMaxDaysOfBorrowing;
	}

	public CalculationConfiguration(long id, String timeStampOfConfiguration,
			long memberMonthlyPayment, long memberPenaltyPayment,
			long memberMaxDaysOfPayment, long bookPenaltyPayment,
			long bookMaxDaysOfBorrowing) {
		this.id = id;
		this.timeStampOfConfiguration = removeAfterDot(timeStampOfConfiguration);
		this.memberRoutinePayment = memberMonthlyPayment;
		this.memberPenaltyPayment = memberPenaltyPayment;
		this.memberMaxDaysOfPayment = memberMaxDaysOfPayment;
		this.bookPenaltyPayment = bookPenaltyPayment;
		this.bookMaxDaysOfBorrowing = bookMaxDaysOfBorrowing;
	}

	public CalculationConfiguration() {
	}

	private String removeAfterDot(String timeStampOfConfiguration) {
		int indexOfDotCharacter = timeStampOfConfiguration.indexOf(".");
		if (indexOfDotCharacter != -1) {
			return timeStampOfConfiguration.substring(0, indexOfDotCharacter);
		} else {
			return timeStampOfConfiguration;
		}
	}

}
