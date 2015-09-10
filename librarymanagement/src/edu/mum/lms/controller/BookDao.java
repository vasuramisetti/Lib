package edu.mum.lms.controller;

import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.entity.Book;

public class BookDao {
    
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Book";
        
    public Book getBook(String isbn) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, isbn);
        
        List<Map<String, Object>> rawBooks = db.get(TABLE_NAME, null, condition);
        
        if(rawBooks.size() > 0) {
            Map<String, Object> rawBook = rawBooks.get(0);
            
	    	Book book = new Book();
	    	book.setIsbn(isbn);
	    	book.setTitle((String)rawBook.get("title"));
	    	
	    	return book;
        }
        else
        	return null;
    }

}
