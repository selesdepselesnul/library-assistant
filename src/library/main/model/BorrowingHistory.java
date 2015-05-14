package library.main.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;

public class BorrowingHistory {

	private SimpleObjectProperty<Long> pinalty;
	private SimpleObjectProperty<Long> bookId;
	private SimpleObjectProperty<LocalDate> timeOfBorrowing;

	public BorrowingHistory(long bookId, LocalDate timeOfBorrowing, long pinalty) {
		this.setBookId(bookId);
		this.setTimeOfBorrowing(timeOfBorrowing);
		this.setPinalty(pinalty);
	}

	private void setPinalty(long pinalty) {
		this.pinalty = new SimpleObjectProperty<Long>(pinalty);
	}

	public void setTimeOfBorrowing(LocalDate timeOfBorrowing) {
		this.timeOfBorrowing = new SimpleObjectProperty<LocalDate>(
				timeOfBorrowing);
	}

	public void setBookId(long bookId) {
		this.bookId = new SimpleObjectProperty<Long>(bookId);
	}

	public long getPinalty() {
		return pinalty.get();
	}

	public LocalDate getTimeOfBorrowing() {
		return timeOfBorrowing.get();
	}

	public long getBookId() {
		return this.bookId.get();
	}

}
