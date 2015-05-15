package library.main.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import library.main.model.Member;
import library.main.model.MemberMonthlyPayment;
import library.main.util.ErrorMessageWindowLoader;
import library.main.util.MemberDaoMYSQL;
import library.main.util.MemberMonthlyPaymentDaoMYSQL;
import library.main.util.MemberPhotoDaoFS;

public class NewMemberFormController implements Initializable {

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField emailTextField;

	@FXML
	private TextField phoneTextField;

	@FXML
	private TextField birthPlaceTextField;

	@FXML
	private DatePicker birthDatePicker;

	@FXML
	private TextField addressTextField;

	@FXML
	private RadioButton maleRadioButton;

	@FXML
	private RadioButton femaleRadioButton;

	@FXML
	private TextField photoTextField;

	@FXML
	private ImageView photoImageView;

	private ToggleGroup genderToggleGroup;

	private MemberDaoMYSQL memberDaoMYSQL;

	private Stage stage;

	private long memberId;

	private Path sourcePhotoPath;

	private Member member;

	private TableView<Member> memberTableView;

	private int selectedIndex;

	private MemberMonthlyPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			if (!Files.exists(MemberPhotoDaoFS.MEMBER_PHOTO_PATH)) {
				Files.createDirectories(MemberPhotoDaoFS.MEMBER_PHOTO_PATH);
			}
			this.genderToggleGroup = new ToggleGroup();
			this.maleRadioButton.setToggleGroup(this.genderToggleGroup);
			this.femaleRadioButton.setToggleGroup(this.genderToggleGroup);
			this.memberId = 0L;
			this.photoImageView.setImage(MemberPhotoDaoFS.DEFAULT_IMAGE);
			this.sourcePhotoPath = null;
			this.memberTableView = null;
			this.photoTextField.setDisable(true);
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	@FXML
	public void handleSubmitButton() {
		try {
			RadioButton selectedRadioButton = (RadioButton) this.genderToggleGroup
					.getSelectedToggle();
			Member member = new Member(this.nameTextField.getText(),
					toGender(selectedRadioButton.getText()),
					this.emailTextField.getText(),
					this.phoneTextField.getText(),
					this.birthDatePicker.getValue(),
					this.birthPlaceTextField.getText(),
					this.addressTextField.getText());

			MemberPhotoDaoFS memberPhotoDaoFS = new MemberPhotoDaoFS();

			if (this.memberId == 0L) { // insert mode
				member.setId(this.memberDaoMYSQL.write(member));
				memberPhotoDaoFS.setMember(member);
				member.setPhoto(memberPhotoDaoFS
						.insertPhotoMember(this.sourcePhotoPath));
				this.memberDaoMYSQL.update(member);
				this.memberMonthlyPaymentDaoMYSQL
				.write(new MemberMonthlyPayment(member.getId()));
			} else {
				Member oldMember = this.memberDaoMYSQL.read(this.memberId);
				member.setId(oldMember.getId());
				member.setPhoto(oldMember.getPhoto());
				member.setTimeOfRegistering(oldMember.getTimeOfRegistering());
				member.setTimeOfLastPayment(oldMember.getTimeOfLastPayment());
				memberPhotoDaoFS.setMember(member);
				if (sourcePhotoPath != null) { // if photo also being updated
					memberPhotoDaoFS.updatePhotoMember(this.sourcePhotoPath);
				}
				if (this.memberTableView != null) {
					this.memberTableView.getItems().set(this.selectedIndex,
							member);
				}
				this.memberDaoMYSQL.update(member);
			}
			this.member = member;
			this.stage.close();
		} catch (Exception e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	public void writeToForm(Member member) {
		try {
			this.nameTextField.setText(member.getName());
			this.addressTextField.setText(member.getAddress());
			this.birthDatePicker.setValue(member.getBirthDate());
			this.birthPlaceTextField.setText(member.getBirthPlace());
			this.emailTextField.setText(member.getEmail());
			this.phoneTextField.setText(member.getPhone());
			writeGenderToRadioButton(member);
			this.photoImageView.setImage(new Image(new MemberPhotoDaoFS(member)
					.readPhotoMember()));
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private void writeGenderToRadioButton(Member member) {
		if (member.getGender().equals("M")) {
			this.genderToggleGroup.selectToggle(this.maleRadioButton);
		} else {
			this.genderToggleGroup.selectToggle(this.femaleRadioButton);
		}
	}

	@FXML
	public void handleCancelButton() {
		this.stage.close();
	}

	@FXML
	public void handlePhotoButton() {
		try {
			FileChooser fileChooser = new FileChooser();
			File photoFile = fileChooser.showOpenDialog(this.stage);
			if (photoFile != null) {
				sourcePhotoPath = photoFile.toPath();
				this.photoImageView.setImage(new Image(Files
						.newInputStream(sourcePhotoPath)));
				this.photoTextField.setText(sourcePhotoPath.toString());
			}
		} catch (IOException e) {
			new ErrorMessageWindowLoader(e.getMessage()).show();
		}
	}

	private String toGender(String gender) {
		switch (gender.toLowerCase()) {
		case "laki-laki":
			return "M";
		default:
			return "F";
		}
	}

	public void setMemberDaoMYSQL(MemberDaoMYSQL memberDaoMYSQL) {
		this.memberDaoMYSQL = memberDaoMYSQL;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setMemberId(long id) {
		this.memberId = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMemberTable(TableView<Member> memberTableView,
			int selectedIndex) {
		this.memberTableView = memberTableView;
		this.selectedIndex = selectedIndex;
	}

	public void setMemberMonthlyPaymentDaoMYSQL(
			MemberMonthlyPaymentDaoMYSQL memberMonthlyPaymentDaoMYSQL) {
		this.memberMonthlyPaymentDaoMYSQL = memberMonthlyPaymentDaoMYSQL;
	}

}
