package edu.mum.lms.view;

import java.time.LocalDate;
import java.util.List;

import edu.mum.lms.controller.BookCopyDto;
import edu.mum.lms.controller.BookDto;
import edu.mum.lms.controller.CheckInOutDto;
import edu.mum.lms.controller.MemberDto;
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
    
    public void checkMember(ActionEvent event) {
        try {
            int memberId = Integer.parseInt(txtMemberId.getText());
            
            MemberDto cioDto = new MemberDto();     
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
    
    public void checkBook(ActionEvent event) {
        String isbn = txtIsbn.getText();
        
        BookDto bookDto = new BookDto();
        Book book = bookDto.getBook(isbn);

        if(book == null) {
            txtTitle.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Book not found", ButtonType.OK);
            alert.showAndWait();
        }
        else {
            txtTitle.setText(book.getTitle());
            BookCopyDto bookCopyDto = new BookCopyDto();
            List<BookCopy> bookCopies = bookCopyDto.getBookCopies(book.getIsbn());
            
            ObservableList<BookCopy> data = FXCollections.observableArrayList(bookCopies);
            cbxCopy.setItems(data);
            
            this.book = book;
        }
    }
    
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
            CheckInOutDto checkInOutDto = new CheckInOutDto();
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
    

    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        Navigator.loadScene(Navigator.CHECKOUT_FORM);
    }
    

}