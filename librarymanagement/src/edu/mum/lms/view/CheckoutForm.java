package edu.mum.lms.view;

import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller class for the checkout form
 */
public class CheckoutForm extends ControllerBase {

    private Member member;
    private Book book;

    @FXML
    private TextField txtMemberId, txtMemberName, txtIsbn, txtTitle;
    @FXML
    private ComboBox<String> cbxCopy;
    @FXML
    private DatePicker dpkDueDate;
    @FXML
    private Text alertMessage;

    @FXML
    public void checkMember() {
        try {
            int memberId = Integer.parseInt(txtMemberId.getText());
            MemberDao cioDto = new MemberDao();
            Member member = cioDto.getMember(memberId);
            if (member == null) {
                txtMemberName.setText("");
                Alert alert = new Alert(AlertType.ERROR, "Member not found", ButtonType.OK);
                alert.showAndWait();
            } else {
                txtMemberName.setText(member.getFirstName() + " " + member.getLastName());
                this.member = member;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "Member ID should be a number", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void checkBook() {
        String isbn = txtIsbn.getText();

        BookDao bookDto = new BookDao();
        Book book = bookDto.getBook(Integer.parseInt(isbn));

        if (book == null) {
            txtTitle.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Book not found", ButtonType.OK);
            alert.showAndWait();
        } else {
            txtTitle.setText(book.getTitle());
            BookCopyDao bookCopyDao = new BookCopyDao();
            CheckInOutDao checkInOutDao = new CheckInOutDao();
            List<BookCopy> bookCopies = bookCopyDao.getBookCopies(book.getIsbn(), true);
            List<String> bookCopiesNumber = new ArrayList<String>();
            for (BookCopy copy : bookCopies) {
                boolean isCheckedOut = checkInOutDao.isBookCheckedOut(copy.getCopyId());
                if (!isCheckedOut) {
                    bookCopiesNumber.add(String.valueOf(copy.getCopyNumber()));
                }
            }
            if (bookCopiesNumber.size() < 1) {
                txtTitle.setText("");
                Alert alert = new Alert(AlertType.ERROR, "No Copies of this book are available for checkout",
                        ButtonType.OK);
                alert.showAndWait();
            } else {
                ObservableList<String> data = FXCollections.observableArrayList(bookCopiesNumber);
                cbxCopy.setItems(data);
            }
        }
    }

    @FXML
    public void checkOut() {
        if (member == null) {
            Alert alert = new Alert(AlertType.WARNING, "Member ID is required!", ButtonType.OK);
            alert.showAndWait();
        } else if (book == null) {
            Alert alert = new Alert(AlertType.WARNING, "Book ISBN is required!", ButtonType.OK);
            alert.showAndWait();
        } else if (dpkDueDate.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Due Date is required!", ButtonType.OK);
            alert.showAndWait();
        } else {
            CheckInOutDao checkInOutDao = new CheckInOutDao();
            CheckInOut checkInOut = new CheckInOut();
            BookDao bookDao = new BookDao();

            checkInOut.setCheckOutDate(LocalDate.now());
            checkInOut.setCopyId(Integer.parseInt(cbxCopy.getValue()));
            checkInOut.setDueDate(dpkDueDate.getValue());
            checkInOut.setMember(member);
            checkInOut.setReturnDate(bookDao.getBookReturnDate(book));

            checkInOutDao.addCheckInOut(checkInOut);

            cbxCopy.setValue(null);

            // clear form fields
            TextField[] textFields = new TextField[] { txtIsbn, txtTitle, txtMemberId, txtMemberName };
            clearFormTextField(textFields);

            showMessage("Checkout Successful!", alertMessage);
        }
    }

}