package edu.mum.lms.controller;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.mum.lms.commonUtil.EnvironmentUtil;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Author;
import edu.mum.lms.entity.Person;

public class AuthorDao {
	
	@Autowired
    private static JDBCUtil jdbcUtil;
    private static final String TABLE_NAME = "Author";
    
    private static final Logger log = EnvironmentUtil.loggerForThisClass();
    
    public static void main(String[] args) {
    	System.out.println(jdbcUtil);
    }

    public int addAuthor(Author a) {
        PersonDao pdao = new PersonDao();
        Person p = (Person) a;
        int personId = pdao.addPerson(p);
        // int authorId = adao.addAuthor(personId, a);

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("credentials", a.getCredentials());
        map.put("shortBio", a.getShortBio());
        map.put("person_id", personId);

        int authorId = jdbcUtil.insertRow(TABLE_NAME, map, true);
        log.info("Instead author in db, author id = " + authorId);
        // addBookAuthor(b.getIsbn(), authorId);

        return authorId;
    }

}