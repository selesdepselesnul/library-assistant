package library.main.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member {

	private SimpleObjectProperty<Long> id;
	private SimpleStringProperty name;
	private SimpleStringProperty email;
	private SimpleStringProperty phone;
	private SimpleObjectProperty<LocalDate> birthDate;
	private SimpleStringProperty birthPlace;
	private SimpleStringProperty address;
	private SimpleStringProperty gender;
	private SimpleObjectProperty<LocalDate> timeOfRegistering;
	private SimpleObjectProperty<LocalDate> timeOfLastPayment;
	private String photo;

	public Member(String name, String gender, String email, String phone,
			LocalDate birthDate, String birthPlace, String address) {
		this.name = new SimpleStringProperty(name);
		this.gender = new SimpleStringProperty(gender);
		this.email = new SimpleStringProperty(email);
		this.phone = new SimpleStringProperty(phone);
		this.birthDate = new SimpleObjectProperty<>(birthDate);
		this.birthPlace = new SimpleStringProperty(birthPlace);
		this.address = new SimpleStringProperty(address);
		this.photo = "NONE";
		this.setId(0L);
		this.setTimeOfRegistering(null);
		this.setTimeOfLastPayment(null);
	}

	public Member() {
		this.photo = "NONE";
		this.setId(0L);
		this.setTimeOfRegistering(null);
		this.setTimeOfLastPayment(null);
	}

	public String getGender() {
		return gender.get();
	}

	public String getName() {
		return name.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getPhone() {
		return phone.get();
	}

	public LocalDate getBirthDate() {
		return birthDate.get();
	}

	public String getBirthPlace() {
		return birthPlace.get();
	}

	public String getAddress() {
		return address.get();
	}

	public long getId() {
		return id.get();
	}

	public void setId(long id) {
		this.id = new SimpleObjectProperty<>(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((address.get() == null) ? 0 : address.get().hashCode());
		result = prime
				* result
				+ ((birthPlace.get() == null) ? 0 : birthPlace.get().hashCode());
		result = prime * result
				+ ((birthDate.get() == null) ? 0 : birthDate.get().hashCode());
		result = prime * result
				+ ((email.get() == null) ? 0 : email.get().hashCode());
		result = prime * result
				+ ((gender.get() == null) ? 0 : gender.get().hashCode());
		result = prime * result + (int) (id.get() ^ (id.get() >>> 32));
		result = prime * result
				+ ((name.get() == null) ? 0 : name.get().hashCode());
		result = prime * result
				+ ((phone.get() == null) ? 0 : phone.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (address.get() == null) {
			if (other.address.get() != null)
				return false;
		} else if (!address.get().equals(other.address.get()))
			return false;
		if (birthPlace.get() == null) {
			if (other.birthPlace.get() != null)
				return false;
		} else if (!birthPlace.get().equals(other.birthPlace.get()))
			return false;
		if (birthDate.get() == null) {
			if (other.birthDate.get() != null)
				return false;
		} else if (!birthDate.get().equals(other.birthDate.get()))
			return false;
		if (email.get() == null) {
			if (other.email.get() != null)
				return false;
		} else if (!email.get().equals(other.email.get()))
			return false;
		if (gender.get() == null) {
			if (other.gender.get() != null)
				return false;
		} else if (!gender.get().equals(other.gender.get()))
			return false;
		if (id.get() != other.id.get())
			return false;
		if (name.get() == null) {
			if (other.name.get() != null)
				return false;
		} else if (!name.get().equals(other.name.get()))
			return false;
		if (phone.get() == null) {
			if (other.phone.get() != null)
				return false;
		} else if (!phone.get().equals(other.phone.get()))
			return false;
		return true;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String newMemberPhotoPath) {
		this.photo = newMemberPhotoPath;
	}

	public void setTimeOfRegistering(LocalDate timeOfRegistering) {
		this.timeOfRegistering = new SimpleObjectProperty<>(timeOfRegistering);
	}

	public LocalDate getTimeOfLastPayment() {
		return this.timeOfLastPayment.get();
	}

	public LocalDate getTimeOfRegistering() {
		return timeOfRegistering.get();
	}

	public void setTimeOfLastPayment(LocalDate timeOfLastPayment) {
		this.timeOfLastPayment = new SimpleObjectProperty<>(timeOfLastPayment);
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + ", email=" + email
				+ ", phone=" + phone + ", birthDate=" + birthDate
				+ ", birthPlace=" + birthPlace + ", address=" + address
				+ ", gender=" + gender + ", timeOfRegistering="
				+ timeOfRegistering + ", timeOfLastPayment="
				+ timeOfLastPayment + ", photo=" + photo + "]";
	}

}
