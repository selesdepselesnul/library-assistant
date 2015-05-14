package library.main.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.main.model.Member;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.MemberDaoMYSQL;
import library.main.util.PaymentCalculator;

public class PaymentWindowController implements Initializable {

	private MemberDaoMYSQL memberDaoMYSQL;
	private long memberId;

	@FXML
	private ImageView paymentWindowImageView;

	@FXML
	private TextField idTextField;

	@FXML
	private TextField monthsSinceLastPayTextField;

	@FXML
	private TextField monthlyPaymentTextField;

	@FXML
	private TextField penaltyPaymentTextField;

	@FXML
	private TextField totalPaymentTextField;

	@FXML
	private Button paymentButton;
	private Member member;
	private TableView<Member> memberTableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.paymentWindowImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/card.png")));
	}

	private void writeToForm() {
		try {
			member = this.memberDaoMYSQL.read(this.memberId);
			if (member != null) {
				decidePayment();
			}
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}

	}

	private void decidePayment() {
		PaymentCalculator paymentCalculator = new PaymentCalculator(member);

		if (paymentCalculator.getMonthsSinceLastPayment() == 0) {
			this.paymentButton.setDisable(true);
		}

		this.idTextField.setText(String.valueOf(this.memberId));
		this.idTextField.setDisable(true);
		this.monthsSinceLastPayTextField.setText(String
				.valueOf(paymentCalculator.getMonthsSinceLastPayment()));
		this.monthsSinceLastPayTextField.setDisable(true);
		this.penaltyPaymentTextField.setText(String.valueOf(paymentCalculator
				.getTotalPenaltyPayment()));
		this.penaltyPaymentTextField.setDisable(true);
		this.monthlyPaymentTextField.setText(String.valueOf(paymentCalculator
				.getTotalMonthlypayment()));
		this.monthlyPaymentTextField.setDisable(true);
		this.totalPaymentTextField.setText(String.valueOf(paymentCalculator
				.getTotalPayment()));
		this.totalPaymentTextField.setDisable(true);
	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
		writeToForm();
	}

	public void setMember(Member member) {
		setMemberDaoMYSQL(this.memberDaoMYSQL);
	}

	@FXML
	public void handlePaymentButton() {
		try {
			this.member.setTimeOfLastPayment(LocalDate.now());
			this.memberDaoMYSQL.updatePayment(this.member);
			this.paymentButton.setDisable(true);
			if (this.memberTableView != null) {
				this.memberTableView.getItems().setAll(
						this.memberDaoMYSQL.readAll());
			}
			clearPayment();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private void clearPayment() {
		this.monthsSinceLastPayTextField.setText("0");
		this.monthlyPaymentTextField.setText("0");
		this.penaltyPaymentTextField.setText("0");
		this.totalPaymentTextField.setText("0");
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public void setMemberTableView(TableView<Member> memberTableView) {
		this.memberTableView = memberTableView;
	}

}