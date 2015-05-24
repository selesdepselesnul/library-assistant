package library.main.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.main.model.CalculationConfiguration;
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

	public PaymentCalculator(Member member,
			CalculationConfiguration calculationConfiguration) {
		LocalDate lastPayment = member.getTimeOfLastPayment();
		daysSinceLastPayment = ChronoUnit.DAYS.between(lastPayment,
				LocalDate.now());

		if (daysSinceLastPayment >= calculationConfiguration
				.getMemberMaxDaysOfPayment()) {
			if (daysSinceLastPayment > calculationConfiguration
					.getMemberMaxDaysOfPayment()) {
				this.totalPenaltyPayment = (daysSinceLastPayment - calculationConfiguration
						.getMemberMaxDaysOfPayment())
						* calculationConfiguration.getMemberPenaltyPayment();
			} else {
				this.totalPenaltyPayment = 0L;
			}
			this.routinePayment = (daysSinceLastPayment / calculationConfiguration
					.getMemberMaxDaysOfPayment())
					* calculationConfiguration.getMemberRoutinePayment();
			this.totalPayment = this.routinePayment + this.totalPenaltyPayment;
		}

	}

	public long getDaysSinceLastPayment() {
		return daysSinceLastPayment;
	}

}
