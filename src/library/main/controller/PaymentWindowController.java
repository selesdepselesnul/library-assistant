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
import library.main.model.MemberPayment;
import library.main.util.Calculation;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.MemberDaoMYSQL;
import library.main.util.MemberPaymentDaoMYSQL;
import library.main.util.PaymentCalculator;

public class PaymentWindowController implements Initializable {

	private MemberDaoMYSQL memberDaoMYSQL;
	private long memberId;
	private Member member;
	private TableView<Member> memberTableView;
	private MemberPaymentDaoMYSQL memberPaymentDaoMYSQL;

	@FXML
	private ImageView paymentWindowImageView;

	@FXML
	private TextField idTextField;

	@FXML
	private TextField daysSinceLastMemberPaymentTextField;

	@FXML
	private TextField totalRoutinePaymentTextField;

	@FXML
	private TextField totalPenaltyPaymentTextField;

	@FXML
	private TextField totalPaymentTextField;

	@FXML
	private Button paymentButton;
	private PaymentCalculator paymentCalculator;

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
		paymentCalculator = new PaymentCalculator(member);

		if (paymentCalculator.getDaysSinceLastPayment() == 0) {
			this.paymentButton.setDisable(true);
		}

		this.idTextField.setText(String.valueOf(this.memberId));
		this.idTextField.setDisable(true);

		this.totalPenaltyPaymentTextField.setText(String
				.valueOf(paymentCalculator.getTotalPenaltyPayment()));
		this.totalPenaltyPaymentTextField.setDisable(true);

		this.totalRoutinePaymentTextField.setText(String
				.valueOf(paymentCalculator.getTotalRoutinePayment()));
		this.totalRoutinePaymentTextField.setDisable(true);

		this.totalPaymentTextField.setText(String.valueOf(paymentCalculator
				.getTotalRoutinePayment()
				+ paymentCalculator.getTotalPenaltyPayment()));
		this.totalPaymentTextField.setDisable(true);

		this.daysSinceLastMemberPaymentTextField.setText(String
				.valueOf(paymentCalculator.getDaysSinceLastPayment()));
		this.daysSinceLastMemberPaymentTextField.setDisable(true);
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

			// pay monthly
			this.memberPaymentDaoMYSQL.write(new MemberPayment(this.memberId,
					this.paymentCalculator.getTotalRoutinePayment(),
					MemberPayment.MONTHLY));

			// if also pay the penalty
			if (this.paymentCalculator.getTotalRoutinePayment() > Calculation
					.getMemberMonthlyPayment()) {
				this.memberPaymentDaoMYSQL.write(new MemberPayment(
						this.memberId, this.paymentCalculator
								.getTotalRoutinePayment(),
						MemberPayment.PENALTY));
			}

			clearPayment();
		} catch (SQLException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private void clearPayment() {
		this.daysSinceLastMemberPaymentTextField.setText("0");
		this.totalRoutinePaymentTextField.setText("0");
		this.totalPenaltyPaymentTextField.setText("0");
		this.totalPaymentTextField.setText("0");
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public void setMemberTableView(TableView<Member> memberTableView) {
		this.memberTableView = memberTableView;
	}

	public void setMemberMonthlyPaymentDaoMYSQL(
			MemberPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL) {
		this.memberPaymentDaoMYSQL = memberMonthlyPaymentDaoMYSQL;
	}

}
