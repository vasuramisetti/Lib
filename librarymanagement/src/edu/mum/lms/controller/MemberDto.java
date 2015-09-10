package edu.mum.lms.controller;

import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.entity.BookCopy;
import edu.mum.lms.entity.Member;

public class MemberDto {
    
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Member";
        
    public Member getMember(int memberId) {
    	
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("member_id", DbClient.EQUALS, memberId);
        
        List<Map<String, Object>> rawMembers = db.get(TABLE_NAME, null, condition);
        
        if(rawMembers.size() > 0) {
        	FilterCondition condition2 = new DbClient.FilterCondition();
            condition2.addCondition("person_id", DbClient.EQUALS, rawMembers.get(0).get("person_id"));
            
            Map<String, Object> rawPerson = db.get("Person", null, condition2).get(0);
            
	    	Member member = new Member();
	    	member.setMemberId(memberId);
	    	member.setFirstName((String)rawPerson.get("firstName"));
	    	member.setLastName((String)rawPerson.get("lastName"));
	    	member.setPhoneNo((String)rawPerson.get("phoneNo"));
	    	member.setStreet((String)rawPerson.get("street"));
	    	member.setCity((String)rawPerson.get("city"));
	    	member.setState((String)rawPerson.get("state"));
	    	member.setZip((int)rawPerson.get("zip"));
	    	
	    	return member;
        }
        else
        	return null;
    }
}
