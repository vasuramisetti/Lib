package edu.mum.lms.entity;

import java.time.LocalDate;

public class CheckInOut {

	public CheckInOut() {
	}

	private Member member;
	private int copyId;
	private String bookName;
	private LocalDate dueDate;
	private LocalDate checkOutDate;
	private LocalDate returnDate;
	

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate checkInDate) {
		this.returnDate = checkInDate;
	}

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
       
    }
    

    public String getbookName() {
        return bookName;
    }
    
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "CheckInOut [member=" + member + ", copyId=" + copyId + ", bookName=" + bookName + ", dueDate=" + dueDate
                + ", checkOutDate=" + checkOutDate + ", returnDate=" + returnDate + "]";
    }
    
    
}