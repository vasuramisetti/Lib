package edu.mum.lms.entity;

import java.util.List;

public class Author extends Person {

	public Author() {
	}

	private String credentials;

	private String shortBio;
	private List<Book> books;

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getShortBio() {
		return shortBio;
	}

	public void setShortBio(String shortBio) {
		this.shortBio = shortBio;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}