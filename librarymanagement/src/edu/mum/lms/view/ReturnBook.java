package edu.mum.lms.view;

import edu.mum.lms.controller.CheckInOutDao;
import edu.mum.lms.entity.CheckInOut;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ReturnBook {
	
	public ReturnBook() {
		//lblTitle.setText(CheckoutRecordList.checkInOut.getbookName());
	}
	
	@FXML private Label lblTitle;
	@FXML private Label lblCheckOutDate;
	@FXML private Label lblDueDate;
	@FXML private DatePicker dpkReturnDate;
	
	@FXML
	public void returnBook() {
		CheckInOutDao checkInOutDao = new CheckInOutDao();
		checkInOutDao.returnBook(CheckoutRecordList.checkInOut.getCopyId(), dpkReturnDate.getValue());
		//Close Window, Refresh Parent
	}
}
