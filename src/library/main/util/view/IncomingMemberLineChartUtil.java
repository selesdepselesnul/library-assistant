package library.main.util.view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import library.main.util.dao.mysql.MemberDaoMYSQL;

public class IncomingMemberLineChartUtil {

	private MemberDaoMYSQL memberDaoMYSQL;
	private LineChart<String, Integer> incomingMemberByMonthLineChart;

	public IncomingMemberLineChartUtil(
			LineChart<String, Integer> incomingMemberByMonthLineChart,
			MemberDaoMYSQL memberDaoMYSQL) throws SQLException {

		this.memberDaoMYSQL = memberDaoMYSQL;
		this.incomingMemberByMonthLineChart = incomingMemberByMonthLineChart;

	}

	public void reloadData() throws SQLException {
		int monthInt = 1;
		List<String> monthList = Arrays.asList("Jan", "Feb", "Mar", "Apr",
				"Mei", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des");
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Bulan");

		Series<String, Integer> incomingMemberSeries = new XYChart.Series<String, Integer>();
		incomingMemberSeries.setName("jumlah anggota masuk");

		for (String monthString : monthList) {

			String monthGlob = "0" + monthInt;
			if (monthInt > 9) {
				monthGlob = monthInt + "";
			}
			incomingMemberSeries.getData().add(
					new XYChart.Data<String, Integer>(monthString,
							this.memberDaoMYSQL.countIncomingMember(monthGlob,
									LocalDate.now().getYear() + "")));
			monthInt++;
		}
		
		this.incomingMemberByMonthLineChart.getData().setAll(
				incomingMemberSeries);
	}

}
