package library.main.model;

public class BookPenalty {

	private long borrowingId;
	private long bookPenaltyAmount;
	private long id;

	public BookPenalty(long borrowingId, long bookPenaltyAmount) {
		this.borrowingId = borrowingId;
		this.bookPenaltyAmount = bookPenaltyAmount;
	}

	public long getBorrowingId() {
		return borrowingId;
	}

	public void setBorrowingId(long borrowingId) {
		this.borrowingId = borrowingId;
	}

	public long getBookPenaltyAmount() {
		return bookPenaltyAmount;
	}

	public void setBookPenaltyAmount(long bookPenaltyAmount) {
		this.bookPenaltyAmount = bookPenaltyAmount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	
	
	

}
