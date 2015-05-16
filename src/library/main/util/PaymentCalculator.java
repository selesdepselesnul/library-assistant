package library.main.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.main.model.Member;

public class PaymentCalculator {
	private long totalPenaltyPayment;
	private long routinePayment;
	private long totalPayment;
	private long daysSinceLastPayment;

	public long getTotalPenaltyPayment() {
		return totalPenaltyPayment;
	}

	public long getTotalRoutinePayment() {
		return routinePayment;
	}

	public long getTotalPayment() {
		return totalPayment;
	}

	public PaymentCalculator(Member member) {
		LocalDate lastPayment = member.getTimeOfLastPayment();
		daysSinceLastPayment = ChronoUnit.DAYS.between(lastPayment,
				LocalDate.now());
		System.out.println(daysSinceLastPayment);

		if (daysSinceLastPayment >= Calculation.getMemberMaxDaysOfPayment()) {
			if (daysSinceLastPayment > Calculation.getMemberMaxDaysOfPayment()) {
				this.totalPenaltyPayment = (daysSinceLastPayment - Calculation
						.getMemberMaxDaysOfPayment())
						* Calculation.getMemberPenaltyPayment();
				System.out.println("total penalty payment = "
						+ this.totalPenaltyPayment);
			} else {
				this.totalPenaltyPayment = 0L;
			}
			System.out.println(daysSinceLastPayment
					/ Calculation.getMemberMaxDaysOfPayment());
			this.routinePayment = (daysSinceLastPayment / Calculation
					.getMemberMaxDaysOfPayment())
					* Calculation.getMemberMonthlyPayment();
			this.totalPayment = this.routinePayment + this.totalPenaltyPayment;
		}

	}

	public long getDaysSinceLastPayment() {
		return daysSinceLastPayment;
	}

}
