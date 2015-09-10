package edu.mum.lms.view;

import edu.mum.lms.controller.CheckInOutDao;
import edu.mum.lms.entity.CheckInOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller class for the second Checkout Record List.
 */
public class CheckoutRecordList {

    @FXML
    private Button btnRetrieve;

    @FXML
    private TextField txtSearch;

    @FXML
    private CheckBox chkIncludeReturned;

    @FXML
    private TableView<CheckInOut> tblResult;
    @FXML
    private TableColumn<CheckInOut, String> copy;
    @FXML
    private TableColumn<CheckInOut, String> dueDate;
    @FXML
    private TableColumn<CheckInOut, String> checkOutDate;
    @FXML
    private TableColumn<CheckInOut, String> returnDate;

    @FXML
    public void retrieveCheckouts(ActionEvent event) {
        try {
            int memberId = Integer.parseInt(txtSearch.getText());

            copy.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("copyNumber"));
            dueDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("dueDate"));
            checkOutDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("checkOutDate"));
            returnDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("returnDate"));

            CheckInOutDao cioDto = new CheckInOutDao();

            ObservableList<CheckInOut> data = FXCollections
                    .observableArrayList(cioDto.getCheckInOuts(memberId, chkIncludeReturned.isSelected()));
            tblResult.setItems(data);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Member ID should be a number", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void close(ActionEvent event) {

    }

    @FXML
    public void checkout(ActionEvent event) {
       Navigator.loadScene(Navigator.CHECKOUT_FORM);
    }

    /**
     * Event handler fired when the user requests a previous vista.
     *
     * @param event
     *            the event that triggered the handler.
     */
    @FXML
    void previousPane(ActionEvent event) {
        Navigator.loadScene(Navigator.CHECKOUT_LIST);
    }

}
