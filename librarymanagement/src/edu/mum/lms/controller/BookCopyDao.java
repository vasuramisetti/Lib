package edu.mum.lms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.DbClient.LogicalOperator;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Book;
import edu.mum.lms.entity.BookCopy;

public class BookCopyDao {
    
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "BookCopy";
    
    public BookCopy getBookCopy(int copyId) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("copy_id", DbClient.EQUALS, copyId);
        
        Map<String, Object> rawCopy = db.get(TABLE_NAME, null, condition).get(0);
        
    	BookCopy copy = new BookCopy();
    	copy.setCopyId(copyId);
    	copy.setIsbn((int) rawCopy.get("isbn"));
    	copy.setCopyNumber((int)rawCopy.get("copyNumber"));
    	
    	return copy;
    }
    
    public List<BookCopy> getBookCopies(int isbn, boolean availableOnly) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, isbn);
        
        List<Map<String, Object>> rawCopies = db.get(TABLE_NAME, null, condition);
        
        List<BookCopy> bookCopies = new ArrayList<BookCopy>();
        
        for(Map<String, Object> rawCopy : rawCopies) {
	    	BookCopy copy = new BookCopy();
	    	int copyId = (int) rawCopy.get("copy_id");
	    	copy.setCopyId(copyId);
	    	copy.setIsbn((int) rawCopy.get("isbn"));
	    	copy.setCopyNumber((int)rawCopy.get("copyNumber"));
	    	
	    	if(availableOnly) {
	    		FilterCondition condition2 = new DbClient.FilterCondition(LogicalOperator.AND);
	            condition2.addCondition("copy_id", DbClient.EQUALS, copyId);
	            condition2.addCondition("returnDate", DbClient.EQUALS, null);
	            
	            List<Map<String, Object>> checkInOuts = db.get("CheckInOut", null, condition2);
	            
	            if(checkInOuts.size() == 0)
		    		bookCopies.add(copy);
	    	} else {
	    		bookCopies.add(copy);
	    	}
        }
    	
    	return bookCopies;
    }

    public String getBookName(int copyId) {
        
        BookCopy bookCopy = getBookCopy(copyId);
        int isbn = bookCopy.getIsbn();
        
        BookDao bookDao = new BookDao();
        Book book = bookDao.getBook(isbn);
        
        return book.getTitle();
    }
}
