package edu.mum.lms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Author;
import edu.mum.lms.entity.Book;

public class BookDao {

    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Book";

    public Book getBook(int isbn) {
        Book book = null;
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, isbn);

        List<Map<String, Object>> rawBooks = db.get(TABLE_NAME, null, condition);

        if (rawBooks.size() > 0) {
            Map<String, Object> rawBook = rawBooks.get(0);

            book = new Book();
            book.setIsbn(isbn);
            book.setTitle((String) rawBook.get("title"));
            book.setMaxCheckout((int) rawBook.get("max_checkout"));
        }

        return book;
    }

    // adds Book to the Book table along with Author details
    public void addBook(Book b) {
        // PersonDao pdao = new PersonDao();
        AuthorDao adao = new AuthorDao();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isbn", b.getIsbn());
        map.put("title", b.getTitle());
        map.put("max_checkout", b.getMaxCheckout());

        db.insertRow(TABLE_NAME, map, false);

        for (Author a : b.getAuthors()) {
            int authorId = adao.addAuthor(a);
            addBookAuthor(b.getIsbn(), authorId);
        }

        addBookCopy(b, 1);
    }

    // add BookCopy with copy no
    public int addBookCopy(Book b, int copyNumber) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isbn", b.getIsbn());
        map.put("copyNumber", copyNumber);
        return db.insertRow("BookCopy", map, true);
    }

    // add copy of a Book in the BookCopy table.Increase the copy Number of the
    // book .
    public int addBookCopy(Book b) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isbn", b.getIsbn());
        String[] columns = new String[] { "copyNumber" };

        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, b.getIsbn());

        List<Map<String, Object>> copyNumbers = db.get("BookCopy", columns, condition);
        List<Integer> cnos = new ArrayList<>();
        if (copyNumbers.isEmpty()) {
            return 0;
        }
        for (Map<String, Object> hmap : copyNumbers) {

            for (Map.Entry<String, Object> e : hmap.entrySet()) {
                Object value = e.getValue();
                cnos.add((Integer) value);
            }

        }

        int nextcopyNumber = Collections.max(cnos);
        System.out.println("max copy number is " + nextcopyNumber);
        map.put("copyNumber", ++nextcopyNumber);

        return db.insertRow("BookCopy", map, true);

        // Also update the noOfcopies column of the book

    }

    // add book id and author id to bookauthor table .Maintains mutiple relation
    // between book and author
    public void addBookAuthor(int bookId, int authorId) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("book_id", bookId);
        map.put("author_id", authorId);

        db.insertRow("book_author", map, false);
    }

    public Map<String, Object> searchISBNWithQuery(String isbn) {
        Book b = new Book();
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, Integer.parseInt(isbn));

        String sql = "select Book.max_checkout, Book.isbn, Book.title, count(Book.isbn) as book_copy  from Book join BookCopy on Book.isbn=BookCopy.isbn where Book.isbn=\""
                + isbn + "\"group by Book.isbn";
        List<Map<String, Object>> bookprops = db.runQuery(sql);

        return bookprops.get(0);
    }

    public static void main(String[] args) {
        System.out.println("search book::::");
        System.out.println(new BookDao().searchISBNWithQuery("1234"));
    }

    public LocalDate getBookReturnDate(Book book) {
        int isbn = book.getIsbn();
        Book foundBook = this.getBook(isbn);
        int checkoutLength  = foundBook.getMaxCheckout();
        LocalDate now = LocalDate.now();
        return now.plusDays(checkoutLength);
    }

}
