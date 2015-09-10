package edu.mum.lms.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.entity.BookCopy;
import edu.mum.lms.entity.CheckInOut;

public class BookCopyDto {
    
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "BookCopy";
    
    public BookCopy getBookCopy(int copyId) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("copy_id", DbClient.EQUALS, copyId);
        
        Map<String, Object> rawCopy = db.get(TABLE_NAME, null, condition).get(0);
        
    	BookCopy copy = new BookCopy();
    	copy.setCopyId(copyId);
    	copy.setIsbn((String)rawCopy.get("isbn"));
    	copy.setCopyNumber((int)rawCopy.get("copyNumber"));
    	
    	return copy;
    }
    
    public List<BookCopy> getBookCopies(String isbn) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("isbn", DbClient.EQUALS, isbn);
        
        List<Map<String, Object>> rawCopies = db.get(TABLE_NAME, null, condition);
        
        List<BookCopy> bookCopies = new ArrayList<BookCopy>();
        
        for(Map<String, Object> rawCopy : rawCopies) {
	    	BookCopy copy = new BookCopy();
	    	copy.setCopyId((int)rawCopy.get("copy_id"));
	    	copy.setIsbn((String)rawCopy.get("isbn"));
	    	copy.setCopyNumber((int)rawCopy.get("copyNumber"));
	    	
	    	bookCopies.add(copy);
        }
    	
    	return bookCopies;
    }
}
