package edu.mum.lms.view;

import edu.mum.lms.entity.Person;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddUser {

    @FXML
    private TextField firstName, lastName, phoneNo, street, city, zip, state, username, password;

    @FXML
    public void addUser() {
        int zipAddress = -1;
        // validate
        try {
            zipAddress = Integer.parseInt(zip.getText());
        } catch (NumberFormatException e) {
            // TODO - handle exception
        }

        Person person = new Person();
        person.setFirstName(firstName.getText());
        person.setLastName(lastName.getText());
        person.setPhoneNo(phoneNo.getText());
        person.setStreet(street.getText());
        person.setCity(city.getText());
        person.setZip(zipAddress);
        person.setState(city.getText());
        
        
    }

}
