package edu.mum.lms.entity;

public class BookCopy {

	public BookCopy() {
	}

	private String isbn;

	private int copyNumber;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getCopyNumber() {
		return copyNumber;
	}

	public void setCopyNumber(int copyNumber) {
		this.copyNumber = copyNumber;
	}

}