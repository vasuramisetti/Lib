package edu.mum.lms.view;

import java.time.LocalDate;
import java.util.List;

import edu.mum.lms.controller.BookCopyDao;
import edu.mum.lms.controller.BookDao;
import edu.mum.lms.controller.CheckInOutDao;
import edu.mum.lms.controller.MemberDao;
import edu.mum.lms.entity.Book;
import edu.mum.lms.entity.BookCopy;
import edu.mum.lms.entity.CheckInOut;
import edu.mum.lms.entity.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Controller class for the checkout form
 */
public class CheckoutForm {
    
    private Member member;
    private Book book;
  
    @FXML private TextField txtMemberId;
    @FXML private TextField txtMemberName;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitle;
    @FXML private ComboBox<BookCopy> cbxCopy;
    @FXML private DatePicker dpkDueDate;
    
    @FXML
    public void checkMember(ActionEvent event) {
        try {
            int memberId = Integer.parseInt(txtMemberId.getText());
            
            MemberDao cioDto = new MemberDao();     
            Member member = cioDto.getMember(memberId);
            if(member == null) {
                txtMemberName.setText("");
                Alert alert = new Alert(AlertType.ERROR, "Member not found", ButtonType.OK);
                alert.showAndWait();
            }
            else {
                txtMemberName.setText(member.getFirstName() + " " + member.getLastName());
                this.member = member;
            }
        } 
        catch(NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Member ID should be a number", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    @FXML
    public void checkBook(ActionEvent event) {
        String isbn = txtIsbn.getText();
        
        BookDao bookDto = new BookDao();
        Book book = bookDto.getBook(isbn);

        if(book == null) {
            txtTitle.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Book not found", ButtonType.OK);
            alert.showAndWait();
        }
        else {
            txtTitle.setText(book.getTitle());
            BookCopyDao bookCopyDao = new BookCopyDao();
            List<BookCopy> bookCopies = bookCopyDao.getBookCopies(book.getIsbn(), true);
            
            ObservableList<BookCopy> data = FXCollections.observableArrayList(bookCopies);
            cbxCopy.setItems(data);
            
            this.book = book;
        }
    }
    
    @FXML
    public void checkOut(ActionEvent event) { 
        if(member == null) {
            Alert alert = new Alert(AlertType.WARNING, "Member ID is required!", ButtonType.OK);
            alert.showAndWait();
        }
        else if(book == null) {
            Alert alert = new Alert(AlertType.WARNING, "Book ISBN is required!", ButtonType.OK);
            alert.showAndWait();
        }
        else if(dpkDueDate.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Due Date is required!", ButtonType.OK);
            alert.showAndWait();
        }
        else {
            CheckInOutDao checkInOutDto = new CheckInOutDao();
            CheckInOut checkInOut = new CheckInOut();
            checkInOut.setCheckOutDate(LocalDate.now());
            checkInOut.setCopy(cbxCopy.getValue());
            checkInOut.setDueDate(dpkDueDate.getValue());
            checkInOut.setMember(member);
            checkInOutDto.addCheckInOut(checkInOut);
            
            cbxCopy.setValue(null);
            txtIsbn.setText(null);
            txtTitle.setText(null);
            
            Alert alert = new Alert(AlertType.INFORMATION, "Checkout Successful!", ButtonType.OK);
            alert.showAndWait();
        }
    }
    

}