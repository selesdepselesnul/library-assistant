package library.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.main.model.Member;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.IncomingMemberLineChartUtil;
import library.main.util.MemberDaoMYSQL;
import library.main.util.MemberPhotoDaoFS;
import library.main.util.PaymentCalculator;
import library.main.util.WindowLoader;

public class MemberTableFormController implements Initializable {
	@FXML
	TableView<Member> memberTableView;

	@FXML
	TableColumn<Member, Long> idMemberTableColumn;

	@FXML
	TableColumn<Member, String> nameMemberTableColumn;

	@FXML
	TableColumn<Member, String> emailMemberTableColumn;

	@FXML
	TableColumn<Member, String> phoneMemberTableColumn;

	@FXML
	TableColumn<Member, LocalDate> birthDateMemberTableColumn;

	@FXML
	TableColumn<Member, String> birthPlaceMemberTableColumn;

	@FXML
	TableColumn<Member, String> addressMemberTableColumn;

	@FXML
	TableColumn<Member, String> genderMemberTableColumn;

	@FXML
	TableColumn<Member, LocalDate> timeOfRegistertingTableColumn;

	@FXML
	TableColumn<Member, LocalDate> timeOfLastPaymentTableColumn;

	@FXML
	TextField filterTextField;

	@FXML
	private ComboBox<String> genderComboBox;

	@FXML
	private ComboBox<String> paymentStatusComboBox;

	@FXML
	ImageView photoImageView;

	@FXML
	private MenuItem deleteAllMenuItem;
	
	@FXML
	private MenuItem deleteMenuItem;

	@FXML
	private MenuItem seePhotoMenuItem;

	@FXML
	private MenuItem updateMenuItem;

	@FXML
	private MenuItem paymentMenuItem;

	private MemberDaoMYSQL memberDaoMYSQL;

	private IncomingMemberLineChartUtil incomingMemberLineChartUtil;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.idMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.nameMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.genderMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("gender"));
		this.emailMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("email"));
		this.phoneMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("phone"));
		this.birthDateMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		this.birthPlaceMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("birthPlace"));
		this.addressMemberTableColumn
				.setCellValueFactory(new PropertyValueFactory<>("address"));
		this.timeOfRegistertingTableColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"timeOfRegistering"));
		this.timeOfLastPaymentTableColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"timeOfLastPayment"));

		this.genderComboBox.getItems().addAll("laki-laki", "perempuan");
		this.paymentStatusComboBox.getItems().addAll("sudah", "belum");
	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
		try {
			this.memberTableView.getItems().setAll(
					this.memberDaoMYSQL.readAll());
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleContextMenu() {
		if (!this.memberTableView.getItems().isEmpty()) {
			setContextMenuItem(false);
		} else {
			setContextMenuItem(true);
		}
	}

	private void setContextMenuItem(boolean isDisable) {
		this.deleteAllMenuItem.setDisable(isDisable);
		this.deleteMenuItem.setDisable(isDisable);
		this.seePhotoMenuItem.setDisable(isDisable);
		this.paymentMenuItem.setDisable(isDisable);
		this.updateMenuItem.setDisable(isDisable);
	}
	
	@FXML
	public void handleDeleteAllMenuItem() {
		try {
			this.memberDaoMYSQL.deleteAll();
			this.memberTableView.getItems().clear();
			this.incomingMemberLineChartUtil.reloadData();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage());
		}
	}

	@FXML
	public void handleDeleteMenuItem() {

		try {
			Member selectedMember = this.memberTableView.getSelectionModel()
					.getSelectedItem();
			this.memberDaoMYSQL.delete(selectedMember.getId());
			this.memberTableView.getItems().remove(selectedMember);
			this.incomingMemberLineChartUtil.reloadData();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	@FXML
	public void handleUpdateMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/NewMemberForm.fxml",
					"Perbarui Anggota",
					(fxmlLoader, stage) -> {
						try {
							TableViewSelectionModel<Member> selectionModel = this.memberTableView
									.getSelectionModel();
							long id = selectionModel.getSelectedItem().getId();
							NewMemberFormController newMemberFormController = (NewMemberFormController) fxmlLoader
									.getController();
							newMemberFormController.setMemberTable(
									this.memberTableView,
									selectionModel.getSelectedIndex());
							newMemberFormController.setStage(stage);
							newMemberFormController.setMemberId(id);
							newMemberFormController
									.setMemberDaoMYSQL(this.memberDaoMYSQL);
							newMemberFormController.writeToForm(selectionModel
									.getSelectedItem());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleFilterBasedOnBirthPlace() {
		clearThenPut(filterBasedOn(p -> p.getBirthPlace().matches(
				this.filterTextField.getText())));
	}

	@FXML
	public void handleFilterBasedOnEmail() {
		clearThenPut(filterBasedOn(p -> p.getEmail().matches(
				this.filterTextField.getText())));
	}

	@FXML
	public void handleFilterBasedOnAddress() {
		clearThenPut(filterBasedOn(p -> p.getAddress().matches(
				this.filterTextField.getText())));
	}

	@FXML
	public void handleFilterBasedOnNumber() {
		clearThenPut(filterBasedOn(p -> p.getPhone().matches(
				this.filterTextField.getText())));
	}

	@FXML
	public void handleFilterBasedOnName() {
		clearThenPut(filterBasedOn(p -> p.getName().matches(
				this.filterTextField.getText())));
	}

	private List<Member> filterBasedOn(Predicate<Member> predicate) {
		return this.memberTableView.getItems().stream().filter(predicate)
				.collect(Collectors.toList());
	}

	@FXML
	public void handleFilterBasedOnGender(ActionEvent e) {
		try {
			if (this.paymentStatusComboBox.getSelectionModel()
					.getSelectedItem() != null) {
				this.paymentStatusComboBox.getSelectionModel().clearSelection();
			}
			this.memberTableView.getItems().setAll(
					this.memberDaoMYSQL.readAll());
			String gender;
			if (this.genderComboBox.getSelectionModel().getSelectedItem()
					.equalsIgnoreCase("laki-laki")) {
				gender = "M";
			} else {
				gender = "F";
			}
			clearThenPut(filterBasedOn(p -> p.getGender().matches(gender)));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void clearThenPut(List<Member> filteredMmbers) {
		this.memberTableView.getItems().clear();
		this.memberTableView.getItems().setAll(filteredMmbers);
	}

	@FXML
	public void handleNoFilterButton() {
		this.genderComboBox.getSelectionModel().clearSelection();
		this.paymentStatusComboBox.getSelectionModel().clearSelection();
		setMemberDaoMYSQL(this.memberDaoMYSQL);
	}

	@FXML
	public void handleShowPhotoMenuItem() {
		try {
			this.photoImageView.setImage(new Image(new MemberPhotoDaoFS(
					memberTableView.getSelectionModel().getSelectedItem())
					.readPhotoMember()));
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handlePaymentMenuItem() {
		try {
			new WindowLoader(
					"library/main/view/PaymentWindow.fxml",
					"Iuran Anggota",
					(fxmlLoader, stage) -> {
						try {
							PaymentWindowController paymentWindowController = (PaymentWindowController) fxmlLoader
									.getController();
							paymentWindowController
									.setMemberId(this.memberTableView
											.getSelectionModel()
											.getSelectedItem().getId());
							paymentWindowController
									.setMemberDaoMYSQL(memberDaoMYSQL);
							paymentWindowController
									.setMemberTableView(this.memberTableView);
						} catch (Exception e) {
							new ErrorMessageWindowLoader(e.getMessage());
						}

					}).show(WindowLoader.SHOW_AND_WAITING);
		} catch (Exception e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleFilterBasedOnPayment(ActionEvent e) {
		try {
			if (this.genderComboBox.getSelectionModel().getSelectedItem() != null) {
				this.genderComboBox.getSelectionModel().clearSelection();
			}
			this.memberTableView.getItems().setAll(
					this.memberDaoMYSQL.readAll());
			clearThenPut(filterBasedOn(decidePaymentStatusPredicate(this.paymentStatusComboBox
					.getSelectionModel().getSelectedItem())));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	private Predicate<Member> decidePaymentStatusPredicate(String paymentStatus) {
		Predicate<Member> predicate = member -> new PaymentCalculator(member)
				.getMonthsSinceLastPayment() > 0;
		if (paymentStatus.equalsIgnoreCase("sudah")) {
			predicate = member -> new PaymentCalculator(member)
					.getMonthsSinceLastPayment() == 0;
		}
		return predicate;
	}

	public void setIncomingMemberLineChartUtil(
			IncomingMemberLineChartUtil incomingMemberLineChartUtil) {
		this.incomingMemberLineChartUtil = incomingMemberLineChartUtil;
	}

}
