package library.main.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import library.main.model.Member;

public class PaymentCalculator {
	public static final long AMOUNT_OF_PINALTY = 5000;
	public static final long AMOUNT_OF_MONTHLY_PAYMENT = 10000;
	private long totalPenaltyPayment;
	private long totalMonthlypayment;
	private long totalPayment;
	private long monthsSinceLastPayment;

	public long getTotalPenaltyPayment() {
		return totalPenaltyPayment;
	}

	public long getTotalMonthlypayment() {
		return totalMonthlypayment;
	}

	public long getTotalPayment() {
		return totalPayment;
	}
	
	public long getMonthsSinceLastPayment() {
		return monthsSinceLastPayment;
	}

	public PaymentCalculator(Member member) {
		LocalDate lastPayment = member.getTimeOfLastPayment();
		monthsSinceLastPayment = ChronoUnit.MONTHS.between(lastPayment,
				LocalDate.now());
		Period period = Period.between(lastPayment, LocalDate.now());
		if (monthsSinceLastPayment >= 1) {
			if (period.getDays() > 0) {
				this.totalPenaltyPayment = monthsSinceLastPayment
						* AMOUNT_OF_PINALTY;
			} else {
				this.totalPenaltyPayment = 0L;
			}
			this.totalMonthlypayment = monthsSinceLastPayment
					* AMOUNT_OF_MONTHLY_PAYMENT;
			this.totalPayment = this.totalMonthlypayment
					+ this.totalPenaltyPayment;
		}

	}

}
