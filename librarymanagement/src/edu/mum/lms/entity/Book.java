package edu.mum.lms.entity;

import java.util.List;

public class Book {

	public Book() {
	}

	private int isbn;
	private String title;
	private int maxCheckout;
	private List<Author> authors;

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

    public int getMaxCheckout() {
        return maxCheckout;
    }

    public void setMaxCheckout(int maxCheckout) {
        this.maxCheckout = maxCheckout;
    }

}