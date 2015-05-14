package library.main.util;

import java.awt.Color;
import java.util.Collection;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.CurrentDateBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

public class LibraryReporter {
	private JasperReportBuilder reportBuilder;
	private JasperViewer reportViewer;
	private String windowTitle;

	public LibraryReporter(String reportTitle, Collection<?> dataSource,
			String windowTitle) throws DRException {

		this.windowTitle = windowTitle;
		// style
		StyleBuilder boldStyle = DynamicReports.stl.style().bold();
		StyleBuilder boldCentered = DynamicReports.stl.style(boldStyle)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder columnHeaderStyle = DynamicReports.stl.style(boldCentered)
				.setBorder(DynamicReports.stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY);

		// title
		TextFieldBuilder<String> titleTextFieldBuilder = DynamicReports.cmp
				.text(reportTitle);
		titleTextFieldBuilder
				.setPositionType(ComponentPositionType.FIX_RELATIVE_TO_BOTTOM);
		titleTextFieldBuilder.setStyle(boldCentered).setHorizontalAlignment(
				HorizontalAlignment.CENTER);
		ImageBuilder logoImageBuilder = DynamicReports.cmp
				.image(ClassLoader
						.getSystemResourceAsStream(
								"library/main/resources/images/library_assistant.png"))
				.setFixedDimension(80, 80)
				.setStyle(
						DynamicReports.stl.style().setHorizontalAlignment(
								HorizontalAlignment.LEFT));

		// time of reporting
		CurrentDateBuilder currentDateBuilder = DynamicReports.cmp
				.currentDate();
		currentDateBuilder.setHorizontalAlignment(HorizontalAlignment.RIGHT);

		// row number
		TextColumnBuilder<Integer> numberColumn = Columns
				.reportRowNumberColumn("No").setFixedColumns(2)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);

		// make report builder and configure
		this.reportBuilder = DynamicReports.report();
		this.reportBuilder.title(DynamicReports.cmp.horizontalFlowList(
				logoImageBuilder, titleTextFieldBuilder, currentDateBuilder));
		this.reportBuilder.pageFooter(DynamicReports.cmp.pageXofY());

		this.reportBuilder.setColumnTitleStyle(columnHeaderStyle);
		this.reportBuilder.highlightDetailEvenRows();
		this.reportBuilder.setDataSource(dataSource);
		this.reportBuilder.addColumn(numberColumn);

	}

	public void addColumns(ColumnBuilder<?, ?>... columns) throws DRException {
		this.reportBuilder.columns(columns);
		reportViewer = new JasperViewer(reportBuilder.toJasperPrint(), false);
		reportViewer.setTitle(windowTitle);
	}

	public void show() {
		reportViewer.setVisible(true);
	}

}
