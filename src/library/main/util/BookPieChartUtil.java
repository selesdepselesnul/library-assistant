package library.main.util;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class BookPieChartUtil {

	private IndividualBookDaoMYSQL  individualBookDaoMYSQL;
	private PieChart bookPieChart;

	public BookPieChartUtil(IndividualBookDaoMYSQL individualBookDaoMYSQL, PieChart bookPieChart)
			throws SQLException {
		this.individualBookDaoMYSQL = individualBookDaoMYSQL;
		this.bookPieChart = bookPieChart;
	}
	
	public void reloadData() throws SQLException {
		ObservableList<PieChart.Data> pieChartData = FXCollections
				.observableArrayList(new PieChart.Data("Buku yang dipinjam",
						this.individualBookDaoMYSQL.countNotAvailable()),
						new PieChart.Data("Buku di perpustakaan",
								this.individualBookDaoMYSQL.countAvailable()));
		
		this.bookPieChart.setData(pieChartData);
	}
}
