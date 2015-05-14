package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.main.model.Borrowing;
import library.main.model.BorrowingHistory;
import library.main.util.BookDaoMYSQL;
import library.main.util.BookPieChartUtil;
import library.main.util.BorrowingDaoMYSQL;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.IncomingMemberLineChartUtil;
import library.main.util.IndividualBookDaoMYSQL;
import library.main.util.LibraryReporter;
import library.main.util.MemberDaoMYSQL;
import library.main.util.WindowLoader;
import library.main.controller.PasswordWindowController;

public class MainWindowController implements Initializable {

	@FXML
	private PieChart bookPieChart;

	@FXML
	private LineChart<String, Number> incomingMemberByMonthLineChart;

	@FXML
	private TextField memberIdTextField;

	@FXML
	private TextField bookIsbnTextField;

	@FXML
	private ImageView libararyImageView;

	@FXML
	private TextField borrowingBookIdTextField;

	private MemberDaoMYSQL memberDaoMYSQL;

	private BookDaoMYSQL bookDaoMYSQL;

	private BorrowingDaoMYSQL borrowingDaoMYSQL;

	private IndividualBookDaoMYSQL individualBookDaoMYSQL;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.libararyImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/library_assistant.png")));

	}

	public void init() throws SQLException, IOException {
		updateIncomingMemberLineChart();

		updateBookPieChart();

	}

	private void updateIncomingMemberLineChart() throws SQLException {

		this.incomingMemberByMonthLineChart
				.setTitle("Ogive anggota yang mendaftar, "
						+ LocalDate.now().getYear());
		new IncomingMemberLineChartUtil(this.incomingMemberByMonthLineChart,
				this.memberDaoMYSQL).reloadData();
	}

	private void updateBookPieChart() throws SQLException {
		new BookPieChartUtil(this.individualBookDaoMYSQL, this.bookPieChart)
				.reloadData();
	}

	@FXML
	public void handleAddMemberButton() {
		try {
			new WindowLoader(
					"library/main/view/NewMemberForm.fxml",
					"Form Anggota",
					(fxmlLoader, stage) -> {
						NewMemberFormController newMemberFormController = (NewMemberFormController) fxmlLoader
								.getController();
						newMemberFormController
								.setMemberDaoMYSQL(this.memberDaoMYSQL);
						newMemberFormController.setStage(stage);

					}).show(WindowLoader.SHOW_AND_WAITING);

			updateIncomingMemberLineChart();
		} catch (IOException e) {
			showError(e);
		} catch (SQLException e) {
			showError(e);
		}
	}

	private void showError(Exception e) {
		new ErrorMessageWindowLoader(e.getMessage()).show();
	}

	@FXML
	public void handleAddBookButton() {
		try {
			new WindowLoader(
					"library/main/view/NewBookForm.fxml",
					"Form Buku",
					(fxmlLoader, stage) -> {
						try {
							NewBookFormController newBookFormController = (NewBookFormController) fxmlLoader
									.getController();
							newBookFormController
									.setBookDaoMYSQL(this.bookDaoMYSQL);
							newBookFormController.setStage(stage);
							newBookFormController.init();
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage());
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
			updateBookPieChart();
		} catch (IOException e) {
			showError(e);
		} catch (SQLException e) {
			showError(e);
		}

	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
	}

	@FXML
	public void handleDeleteMenuItem() {
		String id = this.memberIdTextField.getText();
		try {
			this.memberDaoMYSQL.delete(Long.parseLong(id));
			this.memberIdTextField.clear();
			updateIncomingMemberLineChart();
		} catch (NumberFormatException | SQLException e) {
			showError(e);
		}
	}

	@FXML
	public void handleUpdateMenuItem() {
		try {
			long memberId = Long.parseLong(this.memberIdTextField.getText());

			new WindowLoader(
					"library/main/view/NewMemberForm.fxml",
					"Perbarui Anggota",
					(fxmlLoader, stage) -> {

						try {
							NewMemberFormController newMemberFormController = (NewMemberFormController) fxmlLoader
									.getController();
							newMemberFormController.setMemberId(memberId);
							newMemberFormController
									.setMemberDaoMYSQL(this.memberDaoMYSQL);
							newMemberFormController.setStage(stage);
							newMemberFormController
									.writeToForm(this.memberDaoMYSQL
											.read(memberId));
						} catch (Exception e) {
							showError(e);
						}
					}).show(WindowLoader.SHOW_AND_WAITING);

		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handleDisplayAllMembers() {
		try {
			new WindowLoader(
					"library/main/view/MemberTableForm.fxml",
					"Daftar Semua Anggota",
					(fxmlLoader, stage) -> {
						try {
							MemberTableFormController memberTableFormController = (MemberTableFormController) fxmlLoader
									.getController();
							memberTableFormController
									.setIncomingMemberLineChartUtil(new IncomingMemberLineChartUtil(
											incomingMemberByMonthLineChart,
											memberDaoMYSQL));
							memberTableFormController
									.setMemberDaoMYSQL(this.memberDaoMYSQL);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handleCloseMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/PasswordWindow.fxml",
					"Masukan Password",
					(fxmlLoader, stage) -> {
						PasswordWindowController passwordWindowController = (PasswordWindowController) fxmlLoader
								.getController();
						passwordWindowController.setStage(stage);
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handlePaymentMenuItem() {

		try {
			new WindowLoader(
					"library/main/view/PaymentWindow.fxml",
					"Iuran Anggota",
					(fxmlLoader, stage) -> {
						PaymentWindowController paymentWindowController = (PaymentWindowController) fxmlLoader
								.getController();
						paymentWindowController.setMemberId(Long
								.parseLong(this.memberIdTextField.getText()));
						paymentWindowController
								.setMemberDaoMYSQL(memberDaoMYSQL);

						this.memberIdTextField.clear();

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (NumberFormatException e) {
			showError(e);
		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handleDisplayAllBooks() {
		try {
			new WindowLoader(
					"library/main/view/BookTableForm.fxml",
					"Daftar Semua Buku",
					(fxmlLoader, stage) -> {
						try {
							BookTableFormController bookTableFormController = (BookTableFormController) fxmlLoader
									.getController();
							bookTableFormController
									.setBookDaoMYSQL(this.bookDaoMYSQL);
							bookTableFormController
									.setIndividualBookDaoMYSQL(this.individualBookDaoMYSQL);
							bookTableFormController
									.setBookPieChartUtil(new BookPieChartUtil(
											this.individualBookDaoMYSQL,
											this.bookPieChart));
							bookTableFormController.init();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}

	}

	@FXML
	public void handleSeeDetailOfBookMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/BookDetail.fxml",
					"Detail Buku",
					(fxmlLoader, stage) -> {
						try {
							BookDetailController bookDetailController = (BookDetailController) fxmlLoader
									.getController();
							bookDetailController
									.setBookIsbn(this.bookIsbnTextField
											.getText());
							bookDetailController
									.setBookDaoMYSQL(this.bookDaoMYSQL);
							bookDetailController.init();
							this.bookIsbnTextField.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}

	}

	@FXML
	public void handleDeleteBookMenuItem() {
		try {
			this.bookDaoMYSQL.delete(this.bookIsbnTextField.getText());
			this.bookIsbnTextField.clear();
			updateBookPieChart();
		} catch (SQLException e) {
			showError(e);
		}
	}

	@FXML
	public void handleBookUpdatingMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/NewBookForm.fxml",
					"Perbarui Buku",
					(fxmlLoader, stage) -> {
						try {
							NewBookFormController newBookFormController = (NewBookFormController) fxmlLoader
									.getController();
							newBookFormController.writeToForm(this.bookDaoMYSQL
									.read(this.bookIsbnTextField.getText()));
							newBookFormController
									.setBookIsbn(this.bookIsbnTextField
											.getText());
							newBookFormController
									.setBookDaoMYSQL(this.bookDaoMYSQL);
							newBookFormController.setStage(stage);
							newBookFormController.init();
							updateBookPieChart();
							this.bookIsbnTextField.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}

	}

	@FXML
	public void handleAddNewBorrowingBook() {
		try {
			new WindowLoader(
					"library/main/view/NewBorrowingBook.fxml",
					"Form Peminjaman Buku",
					(fxmlLoader, stage) -> {
						try {
							NewBorrowingBookController newBorrowingBookController = (NewBorrowingBookController) fxmlLoader
									.getController();
							newBorrowingBookController
									.setBorrowingDaoMYSQL(this.borrowingDaoMYSQL);
							newBorrowingBookController.setStage(stage);
							updateBookPieChart();
						} catch (Exception e) {
							showError(e);
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
			updateBookPieChart();
		} catch (IOException e) {
			showError(e);
		} catch (SQLException e) {
			showError(e);
		}
	}

	@FXML
	public void handleDeleteBorrowingBookMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/BorrowingBookDetail.fxml",
					"Denda",
					(fxmlLoader, stage) -> {
						try {
							BorrowingBookDetailController borrowingBookDetailController = (BorrowingBookDetailController) fxmlLoader
									.getController();
							long idOfBorrowing = Long
									.parseLong(this.borrowingBookIdTextField
											.getText());
							borrowingBookDetailController.setStage(stage);
							borrowingBookDetailController
									.setBorrowingId(idOfBorrowing);
							borrowingBookDetailController
									.setBorrowingDaoMYSQL(this.borrowingDaoMYSQL);
							this.borrowingDaoMYSQL.delete(idOfBorrowing);
							this.borrowingBookIdTextField.clear();
							updateBookPieChart();
						} catch (Exception e) {
							showError(e);
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handleSeeStatisticMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/StatisticDetail.fxml",
					"Rangkuman Statistik",
					(fxmlLoaderParam, stage) -> {
						try {
							StatisticDetailController statisticDetailController = (StatisticDetailController) fxmlLoaderParam
									.getController();
							statisticDetailController
									.setAvailableBook(this.individualBookDaoMYSQL
											.countAvailable());
							statisticDetailController
									.setBorrowingBook(this.borrowingDaoMYSQL
											.count());
							statisticDetailController
									.setAmountMemberToday(this.memberDaoMYSQL
											.countMemberBasedOnDate(LocalDate
													.now().toString() + "%"));
							statisticDetailController
									.setAmountAllMember(this.memberDaoMYSQL
											.countMember());
							statisticDetailController.writeStatistic();

						} catch (Exception e) {
							showError(e);
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSeeBorrowingBookByMember() {
		try {
			new WindowLoader(
					"library/main/view/BorrowingHistory.fxml",
					"Riwayat Peminjaman",
					(fxmlLoader, stage) -> {
						try {
							List<BorrowingHistory> borrowingHistoryList = borrowingDaoMYSQL
									.readBasedOnMemberId(Long
											.parseLong(this.memberIdTextField
													.getText()));
							BorrowingHistoryController borrowingHistoryController = (BorrowingHistoryController) fxmlLoader
									.getController();
							borrowingHistoryController
									.setBorrowingHistoryList(borrowingHistoryList);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);

			this.memberIdTextField.clear();
		} catch (NumberFormatException e) {
			showError(e);
		} catch (IOException e) {
			showError(e);
		}
	}

	@FXML
	public void handleDisplayingAllBorrowing() {
		try {
			new WindowLoader(
					"library/main/view/BorrowingTableForm.fxml",
					"Daftar Seluruh Peminjaman",
					(fxmlLoader, stage) -> {
						try {
							BorrowingTableController borrowingTableController = (BorrowingTableController) fxmlLoader
									.getController();
							List<Borrowing> borrowingList;
							borrowingList = this.borrowingDaoMYSQL.readAll();
							borrowingTableController
									.setBorrowingList(borrowingList);
							this.memberIdTextField.clear();
						} catch (Exception e) {
							showError(e);
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		} catch (NumberFormatException e) {
			showError(e);
		}
	}

	@FXML
	public void handleAboutMenuItem() {
		try {
			new WindowLoader("library/main/view/AboutPage.fxml", "About", null)
					.show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			showError(e);
		}

	}

	@FXML
	public void handleReportAvailableBookMenuItem() {
		try {

			TextColumnBuilder<String> titleColumn = Columns.column("judul",
					"title", DynamicReports.type.stringType());
			setToCenter(titleColumn);
			TextColumnBuilder<String> authorsColumn = Columns.column(
					"pengarang", "authors", DynamicReports.type.stringType());
			setToCenter(authorsColumn);
			TextColumnBuilder<String> isbnColumn = Columns.column("isbn",
					"isbn", DynamicReports.type.stringType());
			setToCenter(isbnColumn);
			TextColumnBuilder<String> categoryColumn = Columns.column(
					"kategori", "category", DynamicReports.type.stringType());
			TextColumnBuilder<String> publisherColumn = Columns.column(
					"penerbit", "publisher", DynamicReports.type.stringType());
			setToCenter(publisherColumn);
			TextColumnBuilder<Double> availableAmountColumn = Columns.column(
					"jumlah di perpustakaan", "availableAmount",
					DynamicReports.type.doubleType());
			availableAmountColumn
					.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			TextColumnBuilder<Double> notAvailableAmountColumn = Columns
					.column("jumlah yang dipinjam", "notAvailableAmount",
							DynamicReports.type.doubleType());
			notAvailableAmountColumn
					.setHorizontalAlignment(HorizontalAlignment.RIGHT);

			TextColumnBuilder<Integer> amountColumn = Columns.column("jumlah",
					"amount", DynamicReports.type.integerType());
			amountColumn.setHorizontalAlignment(HorizontalAlignment.RIGHT);

			LibraryReporter libraryReporter;
			libraryReporter = new LibraryReporter("Daftar Stock Buku",
					this.bookDaoMYSQL.readAll(), "Laporan Buku");
			libraryReporter.addColumns(titleColumn, authorsColumn, isbnColumn,
					categoryColumn, publisherColumn, availableAmountColumn,
					notAvailableAmountColumn, amountColumn);
			libraryReporter.show();
		} catch (DRException | SQLException e) {
			showError(e);
		}
	}

	public void setToCenter(TextColumnBuilder<?> column) {
		column.setHorizontalAlignment(HorizontalAlignment.CENTER);
	}

	public void setBookDaoMYSQL(BookDaoMYSQL bookDaoMYSQL) {
		this.bookDaoMYSQL = bookDaoMYSQL;
	}

	public void setIndividualBookDaoMYSQL(
			IndividualBookDaoMYSQL individualBookDaoMYSQL) {
		this.individualBookDaoMYSQL = individualBookDaoMYSQL;
	}

	public void setBorrowingDaoMYSQL(BorrowingDaoMYSQL borrowingDaoMYSQL) {
		this.borrowingDaoMYSQL = borrowingDaoMYSQL;
	}
}