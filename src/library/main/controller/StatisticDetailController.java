package library.main.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class StatisticDetailController implements Initializable {

	@FXML
	private Text availableBookText;

	@FXML
	private Text borrowingBookText;

	@FXML
	private Text amountMemberTodayText;

	@FXML
	private Text amountAllMemberText;

	@FXML
	private ImageView statisticImageView;

	private double availableBook;
	private int amountMemberToday;
	private int amountAllMember;
	private double borrowingBook;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.statisticImageView
				.setImage(new Image(
						ClassLoader
								.getSystemResourceAsStream("library/main/resources/images/statistic.png")));
	}

	public void setAvailableBook(double availableBook) {
		this.availableBook = availableBook;
	}

	public void setAmountMemberToday(int amountMemberToday) {
		this.amountMemberToday = amountMemberToday;
	}

	public void setAmountAllMember(int amountAllMember) {
		this.amountAllMember = amountAllMember;
	}

	public void setBorrowingBook(double borrowingBook) {
		this.borrowingBook = borrowingBook;
	}

	public void writeStatistic() {
		this.amountAllMemberText.setText(this.amountAllMember + "");
		this.amountMemberTodayText.setText(this.amountMemberToday + "");
		this.availableBookText.setText((int) this.availableBook + "");
		this.borrowingBookText.setText((int) this.borrowingBook + "");
	}

}
