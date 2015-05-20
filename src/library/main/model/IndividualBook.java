package library.main.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class IndividualBook {

	private SimpleStringProperty isbn;
	private SimpleObjectProperty<Long> id;
	private boolean isAvailable;

	public IndividualBook(long id, String isbn) {
		this.setId(id);
		this.setIsbn(isbn);
	}

	public IndividualBook() {
	}

	public IndividualBook(String isbn) {
		this.setIsbn(isbn); 
	}

	public void setIsbn(String isbn) {
		this.isbn = new SimpleStringProperty(isbn);
	}

	public void setId(long id) {
		this.id = new SimpleObjectProperty<>(id);
	}
	
	public long getId() {
		return this.id.get();
	}
	
	public String getIsbn() {
		return this.isbn.get();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id.get() ^ (id.get() >>> 32));
		result = prime * result
				+ ((isbn.get() == null) ? 0 : isbn.get().hashCode());
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
		IndividualBook other = (IndividualBook) obj;
		if (id.get() != other.id.get())
			return false;
		if (isbn.get() == null) {
			if (other.isbn.get() != null)
				return false;
		} else if (!isbn.get().equals(other.isbn.get()))
			return false;
		return true;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isAvailable() {
		return this.isAvailable;
	}

}
