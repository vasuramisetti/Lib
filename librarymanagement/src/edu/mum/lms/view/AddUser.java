package edu.mum.lms.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.mum.lms.commonUtil.EnvironmentUtil;
import edu.mum.lms.controller.EmployeeDao;
import edu.mum.lms.controller.PersonDao;
import edu.mum.lms.entity.Employee;
import edu.mum.lms.entity.Employee.EmployeeRole;
import edu.mum.lms.entity.Person;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddUser extends ControllerBase {

    Logger log = EnvironmentUtil.loggerForThisClass();

    @FXML
    private TextField firstName, lastName, phoneNo, street, city, zip, state, username, password;
    
    @FXML
    private Text alertMessage;

    @FXML
    public void addUser() {
        int zipAddress = -1;
        // validate
        try {
            zipAddress = Integer.parseInt(zip.getText());
        } catch (NumberFormatException e) {
            
        }

        try {
            Person person = new Person();
            person.setFirstName(firstName.getText());
            person.setLastName(lastName.getText());
            person.setPhoneNo(phoneNo.getText());
            person.setStreet(street.getText());
            person.setCity(city.getText());
            person.setZip(zipAddress);
            person.setState(state.getText());

            Employee emp = new Employee();
            emp.setUsername(username.getText());
            emp.setPassword(password.getText());
            List<EmployeeRole> roles = new ArrayList<EmployeeRole>();
            roles.add(EmployeeRole.Librarian);
            emp.setRoles(roles);

            PersonDao personDao = new PersonDao();
            int personId = personDao.addPerson(person);

            EmployeeDao employeeDao = new EmployeeDao();
            int empId = employeeDao.addEmployee(personId, emp);
            boolean result = employeeDao.addRole(empId, roles);
            
            if(result) {
                // clear form fields
                TextField[] textFields = new TextField[] {
                        firstName, lastName, phoneNo, street, 
                        city, zip, state, username, password
                };
                clearFormTextField(textFields);
                
                // show success message
                showMessage("User added Successfully !!", alertMessage); 
            }
        } catch (Exception e) {
            log.error("Error while adding employee - " + e.getMessage());
        }

    }

}
