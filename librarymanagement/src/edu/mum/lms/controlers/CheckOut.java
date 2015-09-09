package edu.mum.lms.controlers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.lbs.entity.CheckInOut;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CheckOut implements Initializable {
	public static void main(String[] args) {
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML private Button btnRetrieve;
	
	@FXML private TextField txtSearch;
	
	@FXML private TableView<CheckInOut> tblResult;
    @FXML private TableColumn<CheckInOut, String> copy;
    @FXML private TableColumn<CheckInOut, String> dueDate;
    @FXML private TableColumn<CheckInOut, String> checkOutDate;
    @FXML private TableColumn<CheckInOut, String> returnDate;
	
	public void retrieveCheckouts(ActionEvent event) {

		copy.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("copy"));
		dueDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("dueDate"));
		checkOutDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("checkOutDate"));
		returnDate.setCellValueFactory(new PropertyValueFactory<CheckInOut, String>("returnDate"));
        
		List<CheckInOut> cios = new ArrayList<CheckInOut>();
		CheckInOut cio = new CheckInOut();
		cio.setReturnDate(LocalDate.of(2011, 1, 14));
		cio.setCheckOutDate(LocalDate.of(2011, 1, 14));
		cios.add(cio);
		ObservableList<CheckInOut> data = FXCollections.observableArrayList(cios);
		tblResult.setItems(data);
		
		JDBCUtil db = new JDBCUtil();
        FilterCondition conditon = new DbClient.FilterCondition();
        conditon.addCondition("memeberId", DbClient.EQUALS, "1");
        
        List<Map<String, Object>> elm = db.get("Person", new String[]{"firstName", "lastName"}, conditon);
        System.out.println(elm);
	}
}
