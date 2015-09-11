package edu.mum.lms.controller;

import java.util.LinkedHashMap;

import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Author;
import edu.mum.lms.entity.Person;

public class AuthorDao {
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Author";

    public int addAuthor(Author a) {
        PersonDao pdao = new PersonDao();
        Person p = (Person) a;
        int personId = pdao.addPerson(p);
        // int authorId = adao.addAuthor(personId, a);

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("credentials", a.getCredentials());
        map.put("shortBio", a.getShortBio());
        map.put("person_id", personId);

        int authorId = db.insertRow(TABLE_NAME, map, true);

        // addBookAuthor(b.getIsbn(), authorId);

        return authorId;
    }

}