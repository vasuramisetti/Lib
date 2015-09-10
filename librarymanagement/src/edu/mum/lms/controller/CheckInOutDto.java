package edu.mum.lms.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.DbClient.LogicalOperator;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.CheckInOut;

public class CheckInOutDto {

    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "CheckInOut";
    
    public int addCheckInOut(CheckInOut checkInOut) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    	
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("dueDate", checkInOut.getDueDate().format(formatter));
        map.put("checkOutDate", checkInOut.getCheckOutDate().format(formatter));
        map.put("returnDate", checkInOut.getReturnDate().format(formatter));
        map.put("copy_id", checkInOut.getCopy().getCopyId());
        map.put("memeber_id", checkInOut.getMember().getMemberId());

        int empId = db.insertRow(TABLE_NAME, map, true);
        return empId;
    }
    
    public List<CheckInOut> getCheckInOuts(int memberId, boolean includeReturned) {
    	
        FilterCondition condition = new DbClient.FilterCondition(LogicalOperator.AND);
        condition.addCondition("memeber_id", DbClient.EQUALS, memberId);
        
        
        if(!includeReturned) {
            condition.addCondition("returnDate", DbClient.EQUALS, null);
        }
        
        List<Map<String, Object>> rawCheckInOuts = db.get(TABLE_NAME, null, condition);
        
        List<CheckInOut> checkInOuts = new ArrayList<CheckInOut>();
                
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        for(Map<String, Object> rawCheckInOut : rawCheckInOuts) {
        	CheckInOut cio = new CheckInOut();
        	cio.setDueDate(LocalDate.parse((CharSequence) rawCheckInOut.get("dueDate"), formatter));
        	cio.setCheckOutDate(LocalDate.parse((CharSequence) rawCheckInOut.get("checkOutDate"), formatter));
        	String returnDate = (String)rawCheckInOut.get("returnDate");
        	if(returnDate != null && !returnDate.equals(""))
        		cio.setReturnDate(LocalDate.parse((CharSequence) rawCheckInOut.get("returnDate"), formatter));
        	
        	BookCopyDto bookCopyDto = new BookCopyDto();
        	cio.setCopy(bookCopyDto.getBookCopy((int)rawCheckInOut.get("copy_id")));
        	
        	checkInOuts.add(cio);
        }
        
        return checkInOuts;
    }
    
}
