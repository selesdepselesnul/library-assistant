package library.main.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

	private SimpleObjectProperty<LocalDate> dateOfPublishing;
	private SimpleStringProperty title;
	private SimpleStringProperty authors;
	private SimpleStringProperty publisher;
	private SimpleStringProperty category;
	private SimpleStringProperty isbn;
	private int amount;
	private double availableAmount;
	private double notAvailableAmount;

	public Book() {
	}

	public Book(String title, LocalDate dateOfPublishing, String authors,
			String isbn, String publisher, String category, int amount) {
		this.setTitle(title);
		this.setDateOfPublishing(dateOfPublishing);
		this.setIsbn(isbn);
		this.setCategory(category);
		this.setAuthors(authors);
		this.setPublisher(publisher);
		this.amount = amount;
	}

	public String getTitle() {
		return title.get();
	}

	public LocalDate getDateOfPublishing() {
		return dateOfPublishing.get();
	}

	public void setTitle(String title) {
		this.title = new SimpleStringProperty(title.toLowerCase());
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return this.amount;
	}

	public String getAuthors() {
		return authors.get();
	}

	public void setAuthors(String authors) {
		this.authors = new SimpleStringProperty(authors);
	}

	public String getPublisher() {
		return publisher.get();
	}

	public void setPublisher(String publisher) {
		this.publisher = new SimpleStringProperty(publisher.toLowerCase());
	}

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category = new SimpleStringProperty(category.toLowerCase());
	}

	public String getIsbn() {
		return isbn.get();
	}

	public void setIsbn(String isbn) {
		this.isbn = new SimpleStringProperty(isbn);
	}

	public void setDateOfPublishing(LocalDate dateOfPublishing) {
		this.dateOfPublishing = new SimpleObjectProperty<>(dateOfPublishing);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authors.get() == null) ? 0 : authors.get().hashCode());
		result = prime * result
				+ ((category.get() == null) ? 0 : category.get().hashCode());
		result = prime
				* result
				+ ((dateOfPublishing.get() == null) ? 0 : dateOfPublishing
						.get().hashCode());
		result = prime * result
				+ ((isbn.get() == null) ? 0 : isbn.get().hashCode());
		result = prime * result
				+ ((publisher.get() == null) ? 0 : publisher.get().hashCode());
		result = prime * result
				+ ((title.get() == null) ? 0 : title.get().hashCode());
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
		Book other = (Book) obj;
		if (authors.get() == null) {
			if (other.authors.get() != null)
				return false;
		} else if (!authors.get().equals(other.authors.get()))
			return false;
		if (category.get() == null) {
			if (other.category.get() != null)
				return false;
		} else if (!category.get().equals(other.category.get()))
			return false;
		if (dateOfPublishing.get() == null) {
			if (other.dateOfPublishing.get() != null)
				return false;
		} else if (!dateOfPublishing.get().equals(other.dateOfPublishing.get()))
			return false;
		if (isbn.get() == null) {
			if (other.isbn.get() != null)
				return false;
		} else if (!isbn.get().equals(other.isbn.get()))
			return false;
		if (publisher.get() == null) {
			if (other.publisher.get() != null)
				return false;
		} else if (!publisher.get().equals(other.publisher.get()))
			return false;
		if (title.get() == null) {
			if (other.title.get() != null)
				return false;
		} else if (!title.get().equals(other.title.get()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Book [dateOfPublishing=" + dateOfPublishing + ", title="
				+ title + ", authors=" + authors + ", publisher=" + publisher
				+ ", category=" + category + ", isbn=" + isbn + "]";
	}

	public double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(double availableAmount) {
		this.availableAmount = availableAmount;
	}

	public void setNotAvailableAmount(double notAvailableAmount) {
		this.notAvailableAmount = notAvailableAmount;
	}

	public double getNotAvailableAmount() {
		return notAvailableAmount;
	}

	

}
