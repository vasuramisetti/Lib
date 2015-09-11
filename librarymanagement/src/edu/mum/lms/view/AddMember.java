package edu.mum.lms.view;

import org.apache.log4j.Logger;

import edu.mum.lms.commonUtil.EnvironmentUtil;
import edu.mum.lms.controller.MemberDao;
import edu.mum.lms.controller.PersonDao;
import edu.mum.lms.entity.Person;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddMember extends ControllerBase {

    Logger log = EnvironmentUtil.loggerForThisClass();

    @FXML
    private TextField firstName, lastName, phoneNo, street, city, zip, state;

    @FXML
    private Text alertMessage;

    @FXML
    public void addMember() {
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

            PersonDao personDao = new PersonDao();
            int personId = personDao.addPerson(person);

            MemberDao memeberDao = new MemberDao();
            memeberDao.addMember(personId);

            // clear form fields
            TextField[] textFields = new TextField[] { firstName, lastName, phoneNo, street, city, zip, state };
            clearFormTextField(textFields);

            // show success message
            showMessage("Member added Successfully !!", alertMessage);
        } catch (Exception e) {
            log.error("Error while adding employee - " + e.getMessage());
        }

    }

}
