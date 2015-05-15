package library.main.model;

import java.util.Date;

public class PaymentReportSummary {

	private Date timeOfPayment;
	private long memberMonthlyPayment;
	private long memberPenalty;
	private long bookPenalty;

	public PaymentReportSummary(Date timeOfPayment,
			long memberMonthlyPayment, long memberPenalty,
			long bookPenalty) {
		this.timeOfPayment = timeOfPayment;
		this.memberMonthlyPayment = memberMonthlyPayment;
		this.memberPenalty = memberPenalty;
		this.bookPenalty = bookPenalty;
	}

	public Date getTimeOfPayment() {
		return timeOfPayment;
	}

	public void setTimeOfPayment(Date timeOfPayment) {
		this.timeOfPayment = timeOfPayment;
	}

	public long getMemberMonthlyPayment() {
		return memberMonthlyPayment;
	}

	public void setMemberMonthlyPayment(long memberMonthlyPayment) {
		this.memberMonthlyPayment = memberMonthlyPayment;
	}

	public long getMemberPenalty() {
		return memberPenalty;
	}

	public void setMemberPenalty(long memberPenalty) {
		this.memberPenalty = memberPenalty;
	}

	public long getBookPenalty() {
		return bookPenalty;
	}

	public void setBookPenalty(long bookPenalty) {
		this.bookPenalty = bookPenalty;
	}

	
	

}
