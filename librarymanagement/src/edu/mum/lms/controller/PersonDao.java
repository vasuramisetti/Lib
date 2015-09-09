package edu.mum.lms.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Person;

public class PersonDao {
    
    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Person";
    
    public static void main(String[] args) {
        Person person = new Person();
        person.setFirstName("Bidhut");
        person.setLastName("KARKI");
        person.setPhoneNo("655-333-768");
        person.setStreet("1000 N");
        person.setCity("Farifield");
        person.setState("IA");
        person.setZip(54657);
        
        PersonDao dao = new PersonDao();
        int personId = dao.addPerson(person);
        System.out.println("Inserted Row sucessuflly !" + personId);
    }
    
    public int addPerson(Person person) {

        LinkedHashMap<String, Object> peopleData = new LinkedHashMap<String, Object>();
        peopleData.put("firstName", person.getFirstName());
        peopleData.put("lastName", person.getLastName());
        peopleData.put("phoneNo", person.getPhoneNo());
        peopleData.put("street", person.getStreet());
        peopleData.put("city", person.getCity());
        peopleData.put("state", person.getState());
        peopleData.put("zip", person.getZip());

        int personId = db.insertRow(TABLE_NAME, peopleData, true);
        return personId;
    }
    
    public boolean deletePerson(int personId) {
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("person_id", DbClient.EQUALS, personId);
        
        return db.delete(TABLE_NAME, condition);
    }
    
    public List<Map<String, Object>> getPerson(int personId) {
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("person_id", DbClient.EQUALS, personId);
        
        return db.get(TABLE_NAME, null, condition);
    }

}
