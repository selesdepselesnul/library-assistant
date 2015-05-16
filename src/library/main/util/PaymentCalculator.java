package library.main.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import library.main.model.Member;

public class PaymentCalculator {
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
						* Calculation.getMemberPenaltyPayment();
			} else {
				this.totalPenaltyPayment = 0L;
			}
			this.totalMonthlypayment = monthsSinceLastPayment
					* Calculation.getMemberMonthlyPayment();
			this.totalPayment = this.totalMonthlypayment
					+ this.totalPenaltyPayment;
		}

	}

}
