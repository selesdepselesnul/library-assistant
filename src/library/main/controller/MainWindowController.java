package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import simpleui.util.ErrorMessageWindowLoader;
import simpleui.util.LibraryReporter;
import simpleui.util.PasswordAskerWindow;
import simpleui.util.WindowLoader;
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
import library.main.model.PaymentReportSummary;
import library.main.util.dao.filesystem.AdminDaoFS;
import library.main.util.dao.mysql.BookDaoMYSQL;
import library.main.util.dao.mysql.BookPenaltyDaoMYSQL;
import library.main.util.dao.mysql.BorrowingDaoMYSQL;
import library.main.util.dao.mysql.CalculationConfigurationDaoMYSQL;
import library.main.util.dao.mysql.IndividualBookDaoMYSQL;
import library.main.util.dao.mysql.MemberDaoMYSQL;
import library.main.util.dao.mysql.MemberPaymentDaoMYSQL;
import library.main.util.window.BookPieChartUtil;
import library.main.util.window.IncomingMemberLineChartUtil;

public class MainWindowController implements Initializable {

	@FXML
	private PieChart bookPieChart;

	@FXML
	private LineChart<String, Integer> incomingMemberByMonthLineChart;

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

	private MemberPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL;

	private BookPenaltyDaoMYSQL bookPenaltyPaymentDaoMYSQL;

	private AdminDaoFS adminDaoMYSQL;

	private CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL;

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
			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(

									"library/main/view/NewMemberForm.fxml",
									"Form Anggota",
									(fxmlLoader, stage) -> {
										try {
											NewMemberFormController newMemberFormController = (NewMemberFormController) fxmlLoader
													.getController();
											newMemberFormController
													.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
											newMemberFormController
													.setMemberMonthlyPaymentDaoMYSQL(this.memberMonthlyPaymentDaoMYSQL);
											newMemberFormController
													.doBeforeExit(() -> {
														try {
															updateIncomingMemberLineChart();
														} catch (Exception e) {
															new ErrorMessageWindowLoader(
																	e.getMessage())
																	.show();
														}

													});
											newMemberFormController
													.setMemberDaoMYSQL(this.memberDaoMYSQL);
											newMemberFormController
													.setStage(stage);
										} catch (Exception e) {
											new ErrorMessageWindowLoader(e
													.getMessage()).show();
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleAddBookButton() {

		try {
			new PasswordAskerWindow(
					false,
					() -> {
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
											newBookFormController
													.doActionBeforeExit(() -> {
														try {
															updateBookPieChart();
														} catch (Exception e) {
															e.printStackTrace();
														}

													});
											newBookFormController
													.setStage(stage);
											newBookFormController.init();
										} catch (Exception e) {
											new ErrorMessageWindowLoader(e
													.getMessage());
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
	}

	@FXML
	public void handleDeleteMenuItem() {
		try {
			String id = this.memberIdTextField.getText();
			new PasswordAskerWindow(false, () -> {
				try {
					this.memberDaoMYSQL.delete(Long.parseLong(id));
					this.memberIdTextField.clear();
					updateIncomingMemberLineChart();
				} catch (Exception e) {
					new ErrorMessageWindowLoader(e.getMessage()).show();
				}
			}, this.adminDaoMYSQL).showAndWait();
		} catch (NumberFormatException | IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleReportLibraryCalculation() {
		try {
			TextColumnBuilder<Long> idColumn = Columns.column("id", "id",
					DynamicReports.type.longType());
			TextColumnBuilder<String> timeStampOfConfiguringColumn = Columns
					.column("waktu konfigurasi", "timeStampOfConfiguration",
							DynamicReports.type.stringType());
			TextColumnBuilder<Long> memberRoutinePaymentColumn = Columns
					.column("iuran tetap anggota", "memberRoutinePayment",
							DynamicReports.type.longType());
			TextColumnBuilder<Long> memberPenaltyPaymentColumn = Columns
					.column("denda anggota", "memberPenaltyPayment",
							DynamicReports.type.longType());
			TextColumnBuilder<Long> memberMaxdaysOfPaymentColumn = Columns
					.column("maksimal hari pembayaran",
							"memberMaxDaysOfPayment",
							DynamicReports.type.longType());
			TextColumnBuilder<Long> bookPenaltyPaymentColumn = Columns.column(
					"denda buku", "bookPenaltyPayment",
					DynamicReports.type.longType());
			TextColumnBuilder<Long> maxDaysOfBorrowingColumn = Columns.column(
					"maksimal hari peminjaman", "bookMaxDaysOfBorrowing",
					DynamicReports.type.longType());
			LibraryReporter libraryReporter = new LibraryReporter(
					"laporan iuran & denda perpustakaan",
					calculationConfigurationDaoMYSQL.readAll(),
					"laporan iuran & denda", false);

			libraryReporter.addColumns(idColumn, timeStampOfConfiguringColumn,
					memberRoutinePaymentColumn, memberPenaltyPaymentColumn,
					memberMaxdaysOfPaymentColumn, bookPenaltyPaymentColumn,
					maxDaysOfBorrowingColumn);
			libraryReporter.show();
		} catch (DRException | SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleUpdateMenuItem() {
		try {
			long memberId = Long.parseLong(this.memberIdTextField.getText());
			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(
									"library/main/view/NewMemberForm.fxml",
									"Perbarui Anggota",
									(fxmlLoader, stage) -> {

										try {
											NewMemberFormController newMemberFormController = (NewMemberFormController) fxmlLoader
													.getController();
											newMemberFormController
													.setMemberId(memberId);
											newMemberFormController
													.setMemberDaoMYSQL(this.memberDaoMYSQL);
											newMemberFormController
													.setStage(stage);
											newMemberFormController
													.writeToForm(this.memberDaoMYSQL
															.read(memberId));
										} catch (Exception e) {
											new ErrorMessageWindowLoader(e
													.getMessage()).show();
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
									.setAdminDaoMYSQL(this.adminDaoMYSQL);
							memberTableFormController
									.setIncomingMemberLineChartUtil(new IncomingMemberLineChartUtil(
											incomingMemberByMonthLineChart,
											memberDaoMYSQL));
							memberTableFormController
									.setMemberDaoMYSQL(this.memberDaoMYSQL);
							memberTableFormController
									.setMemberMonthlyPaymentDaoMYSQL(this.memberMonthlyPaymentDaoMYSQL);
							memberTableFormController
									.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleCloseMenuItem() {
		try {
			new PasswordAskerWindow(true, null, this.adminDaoMYSQL)
					.showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage());
		}
	}

	@FXML
	public void handlePaymentMenuItem() {

		try {
			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(
									"library/main/view/PaymentWindow.fxml",
									"Iuran Anggota",
									(fxmlLoader, stage) -> {
										PaymentWindowController paymentWindowController = (PaymentWindowController) fxmlLoader
												.getController();
										paymentWindowController.setMemberId(Long
												.parseLong(this.memberIdTextField
														.getText()));
										paymentWindowController
												.setMemberDaoMYSQL(this.memberDaoMYSQL);
										paymentWindowController
												.setMemberMonthlyPaymentDaoMYSQL(this.memberMonthlyPaymentDaoMYSQL);
										paymentWindowController
												.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
										paymentWindowController.writeToForm();
										this.memberIdTextField.clear();

									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							e.printStackTrace();
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}
					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
									.setAdminDaoMYSQL(this.adminDaoMYSQL);
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
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleDeleteBookMenuItem() {
		try {
			new PasswordAskerWindow(false, () -> {
				try {
					this.bookDaoMYSQL.delete(this.bookIsbnTextField.getText());
					this.bookIsbnTextField.clear();
					updateBookPieChart();
				} catch (Exception e) {
					new ErrorMessageWindowLoader(e.getMessage()).show();
				}

			}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleBookUpdatingMenuItem() {
		try {
			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(
									"library/main/view/NewBookForm.fxml",
									"Perbarui Buku",
									(fxmlLoader, stage) -> {
										try {
											NewBookFormController newBookFormController = (NewBookFormController) fxmlLoader
													.getController();
											newBookFormController
													.writeToForm(this.bookDaoMYSQL
															.read(this.bookIsbnTextField
																	.getText()));
											newBookFormController
													.setBookIsbn(this.bookIsbnTextField
															.getText());
											newBookFormController
													.setBookDaoMYSQL(this.bookDaoMYSQL);
											newBookFormController
													.setStage(stage);
											newBookFormController.init();
											updateBookPieChart();
											this.bookIsbnTextField.clear();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleAddNewBorrowingBook() {
		try {
			new PasswordAskerWindow(
					false,
					() -> {
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
											newBorrowingBookController
													.doActionBeforeExit(() -> {

														try {
															updateBookPieChart();
														} catch (Exception e) {
															new ErrorMessageWindowLoader(
																	e.getMessage())
																	.show();
														}

													});
											newBorrowingBookController
													.setStage(stage);
											updateBookPieChart();
										} catch (Exception e) {
											new ErrorMessageWindowLoader(e
													.getMessage()).show();
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleDeleteBorrowingBookMenuItem() {
		try {
			new PasswordAskerWindow(
					false,
					() -> {
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
											borrowingBookDetailController
													.setStage(stage);
											borrowingBookDetailController
													.setBorrowingId(idOfBorrowing);
											borrowingBookDetailController
													.setBorrowingDaoMYSQL(this.borrowingDaoMYSQL);
											borrowingBookDetailController
													.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
											borrowingBookDetailController
													.setBookPenaltyPaymentDaoMYSQL(this.bookPenaltyPaymentDaoMYSQL);
											Borrowing borrowing = this.borrowingDaoMYSQL
													.read(idOfBorrowing);
											System.out.println("book id = "
													+ borrowing.getBookId());
											this.individualBookDaoMYSQL.updateAvailability(
													borrowing.getBookId(), true);
											this.borrowingDaoMYSQL
													.updateTimeOfReturning(idOfBorrowing);
											this.borrowingBookIdTextField
													.clear();
											updateBookPieChart();
										} catch (Exception e) {
											new ErrorMessageWindowLoader(e
													.getMessage()).show();
										}

									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
											.countBorrowed());
							statisticDetailController
									.setAmountMemberToday(this.memberDaoMYSQL
											.countMemberBasedOnDate(LocalDate
													.now().toString() + "%"));
							statisticDetailController
									.setAmountAllMember(this.memberDaoMYSQL
											.countMember());
							statisticDetailController.writeStatistic();

						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage()).show();
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
							List<BorrowingHistory> borrowingHistoryList = borrowingDaoMYSQL.readBasedOnMemberId(
									Long.parseLong(this.memberIdTextField
											.getText()),
									this.calculationConfigurationDaoMYSQL
											.readLastConfig()
											.getBookPenaltyPayment());
							BorrowingHistoryController borrowingHistoryController = (BorrowingHistoryController) fxmlLoader
									.getController();
							borrowingHistoryController
									.setBorrowingHistoryList(borrowingHistoryList);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);

			this.memberIdTextField.clear();
		} catch (NumberFormatException | IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException | NumberFormatException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleAboutMenuItem() {
		try {
			new WindowLoader("library/main/view/AboutPage.fxml", "About", null)
					.show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
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
					this.bookDaoMYSQL.readAll(), "Laporan Buku", true);
			libraryReporter.addColumns(titleColumn, authorsColumn, isbnColumn,
					categoryColumn, publisherColumn, availableAmountColumn,
					notAvailableAmountColumn, amountColumn);
			libraryReporter.show();
		} catch (DRException | SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handlePaymentReportMenuItem() {

		try {
			LocalDate initial = LocalDate.now();
			LocalDate start = initial.with(TemporalAdjusters.firstDayOfMonth());
			LocalDate end = initial.with(TemporalAdjusters.lastDayOfMonth());

			List<LocalDate> datesOfReporting = new ArrayList<>();
			LocalDate date = start;
			while (date.isBefore(end)) {
				datesOfReporting.add(date);
				date = date.plusDays(1);
			}
			datesOfReporting.add(end);

			List<PaymentReportSummary> paymentReportSummarieList = new ArrayList<>();
			datesOfReporting.forEach(localDate -> {
				try {
					paymentReportSummarieList.add(new PaymentReportSummary(
							java.sql.Date.valueOf(localDate),
							this.memberMonthlyPaymentDaoMYSQL
									.sumMonthlyBasedOnDate(localDate),
							this.memberMonthlyPaymentDaoMYSQL
									.sumPenaltyBasedOnDate(localDate),
							this.bookPenaltyPaymentDaoMYSQL
									.sumBasedOnDate(localDate)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			TextColumnBuilder<Date> timeOfPaymentColumn = Columns.column(
					"Tgl Pembayaran", "timeOfPayment",
					DynamicReports.type.dateType());
			timeOfPaymentColumn
					.setHorizontalAlignment(HorizontalAlignment.CENTER);
			TextColumnBuilder<Long> memberMonthlyPaymentColumn = Columns
					.column("iuran anggota", "memberMonthlyPayment",
							DynamicReports.type.longType());
			TextColumnBuilder<Long> memberPenaltyColumn = Columns.column(
					"denda anggota", "memberPenalty",
					DynamicReports.type.longType());
			TextColumnBuilder<Long> bookPenaltyColumn = Columns
					.column("denda buku", "bookPenalty",
							DynamicReports.type.longType());
			LibraryReporter libraryReporter = new LibraryReporter(
					"Daftar Pembayaran Anggota", paymentReportSummarieList,
					"Laporan Pembayaran Anggota", false);
			libraryReporter.addColumns(timeOfPaymentColumn,
					memberMonthlyPaymentColumn, memberPenaltyColumn,
					bookPenaltyColumn);
			libraryReporter.show();
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleConfigurationMenuItem() {
		try {
			new PasswordAskerWindow(
					false,
					() -> {
						try {
							new WindowLoader(
									"library/main/view/LibraryConfigurationWindow.fxml",
									"Konfigurasi",
									(fxmlLoader, stage) -> {
										try {
											LibraryConfigurationWindowController libraryConfigurationWindowController = (LibraryConfigurationWindowController) fxmlLoader
													.getController();
											libraryConfigurationWindowController
													.setAdminDaoMYSQL(this.adminDaoMYSQL);
											libraryConfigurationWindowController
													.setCalculationConfigurationDaoMYSQL(this.calculationConfigurationDaoMYSQL);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}).show(WindowLoader.SHOW_AND_WAITING);
						} catch (Exception e) {
							e.printStackTrace();
							new ErrorMessageWindowLoader(e.getMessage()).show();
						}

					}, this.adminDaoMYSQL).showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			new ErrorMessageWindowLoader(e.getMessage()).show();
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

	public void setMemberMonthlyPaymentDaoMYSQL(
			MemberPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL) {
		this.memberMonthlyPaymentDaoMYSQL = memberMonthlyPaymentDaoMYSQL;
	}

	public void setBookPenaltyPaymentDaoMYSQL(
			BookPenaltyDaoMYSQL bookPenaltyDaoMYSQL) {
		this.bookPenaltyPaymentDaoMYSQL = bookPenaltyDaoMYSQL;
	}

	public void setAdminDaoMYSQL(AdminDaoFS adminDaoMYSQL) {
		this.adminDaoMYSQL = adminDaoMYSQL;
	}

	public void setCalculationConfigurationDaoMYSQL(
			CalculationConfigurationDaoMYSQL calculationConfigurationDaoMYSQL) {
		this.calculationConfigurationDaoMYSQL = calculationConfigurationDaoMYSQL;
	}
	
}
