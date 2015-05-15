package library.main.model;

import library.main.util.configuration.Calculation;

public class MemberMonthlyPayment {

	private long memberId;
	private long amount;
	private long id;
	private String paymentMode;
	public static final String MONTHLY = "monthly";
	public static final String PENALTY = "penalty";

	public MemberMonthlyPayment(long memberId) {
		this.memberId = memberId;
		this.amount = Calculation.getMemberMonthlyPayment();
		this.paymentMode = MemberMonthlyPayment.MONTHLY;
	}

	public MemberMonthlyPayment(long memberId, long amount, String paymentMode) {
		this.memberId = memberId;
		this.amount = amount;
		this.paymentMode = paymentMode;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return this.amount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public String getPaymentMode() {
		return this.paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

}
