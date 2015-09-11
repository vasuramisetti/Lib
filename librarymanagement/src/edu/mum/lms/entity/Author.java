package edu.mum.lms.entity;

import java.util.List;

public class Author extends Person {

    private String credentials;

    private String shortBio;
    private List<Book> books;

    public Author() {
    }

    public Author(String firstName, String lastName, String street, String city, String state, int zip, String phoneNo,
            String credentials, String shortBio) {
        super(firstName, lastName, phoneNo, street, city, state, zip);
        this.credentials = credentials;
        this.shortBio = shortBio;
    }

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

    public String toString() {

        return firstName + ":" + lastName + ":" + street + ":" + city + ":" + state + ":" + zip + ":" + phoneNo + ":"
                + credentials + ":" + shortBio;
    }

}