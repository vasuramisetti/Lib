package edu.mum.lms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.DbClient.LogicalOperator;
import edu.mum.lms.commonUtil.JDBCUtil;

public class LoginDao {

    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Employee";
    UserSession session = null;

    public UserSession validateLogin(String username, String password) {
        // check with database whether user is logged in or not
        FilterCondition condition = new DbClient.FilterCondition(LogicalOperator.AND);
        condition.addCondition("username", DbClient.EQUALS, username);
        condition.addCondition("password", DbClient.EQUALS, password);
        Map<String, Object> member = db.getFirstOne(TABLE_NAME, new String[] { "emp_id" }, condition, null);

        // initialize session
        if(member != null) {
            int emp_id = (int) (member.get("emp_id"));
            
            session = new UserSession();
            session.setMemberId(emp_id);
            session.setUsername(username);
            
            FilterCondition condition2 = new DbClient.FilterCondition(LogicalOperator.AND);
            condition2.addCondition("emp_id", DbClient.EQUALS, emp_id);
            List<Map<String, Object>> employeeRoles = db.get("EmployeeRole", new String[] { "emp_id", "roleName" }, condition2);
            List<String> roles = new ArrayList<String>();
            for(Map<String, Object> emp : employeeRoles) {
                System.out.println(emp);
                roles.add((String) emp.get("roleName"));
            }
            session.setRole(roles);
        }
        return session;
    }
    
}
