package library.main.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;

public class Borrowing {

	private SimpleObjectProperty<Long> id;
	private SimpleObjectProperty<Long> memberId;
	private SimpleObjectProperty<Long> bookId;
	private SimpleObjectProperty<LocalDate> timeOfBorrowing;

	public Borrowing(long memberId, long bookId) {
		this.setMemberId(memberId);
		this.setBookId(bookId);
	}

	public Borrowing(long memberId, long bookId, LocalDate timeOfBorrowing) {
		this.setMemberId(memberId);
		this.setBookId(bookId);
		this.setTimeOfBorrowing(timeOfBorrowing);
	}

	private void setTimeOfBorrowing(LocalDate timeOfBorrowing) {
		this.timeOfBorrowing = new SimpleObjectProperty<>(timeOfBorrowing);
	}

	public void setBookId(long bookId) {
		this.bookId = new SimpleObjectProperty<>(bookId);
	}

	public void setMemberId(long memberId) {
		this.memberId = new SimpleObjectProperty<>(memberId);
	}

	public long getMemberId() {
		return memberId.get();
	}

	public long getBookId() {
		return bookId.get();
	}

	public long getId() {
		return id.get();
	}

	public void setId(long id) {
		this.id = new SimpleObjectProperty<>(id);
	}

	public LocalDate getTimeOfBorrowing() {
		return timeOfBorrowing.get();
	}

}
