package edu.mum.lms.view;

import java.io.IOException;

import edu.mum.lms.controller.CheckInOutDao;
import edu.mum.lms.entity.CheckInOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    
    @FXML private Button btnReturnBook;

    @FXML
    private TableView<CheckInOut> tblResult;
    @FXML
    private TableColumn<CheckInOut, String> title;
    @FXML
    private TableColumn<CheckInOut, String> copy;
    @FXML
    private TableColumn<CheckInOut, String> dueDate;
    @FXML
    private TableColumn<CheckInOut, String> checkOutDate;
    @FXML
    private TableColumn<CheckInOut, String> returnDate;

    @FXML
    public void retrieveCheckouts() {
        try {
            int memberId = Integer.parseInt(txtSearch.getText());
System.out.println(new PropertyValueFactory<CheckInOut, String>("bookName"));
            title.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("bookName"));
            copy.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("copyId"));
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
    public void returnBook(MouseEvent mouseEvent) {
    	if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() >= 2){
            	TableView<CheckInOut> tblResult = (TableView<CheckInOut>) mouseEvent.getSource();
            	CheckInOut checkInOut = tblResult.getSelectionModel().getSelectedItem();
            	if(checkInOut != null) {
            		ReturnBook.checkInOut = checkInOut;
            		Navigator.loadScene(Navigator.RETURN_BOOK_FORM);
            	}
            }
        }
    }

}
