package edu.mum.lms.view;

import java.util.ArrayList;
import java.util.List;

import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.controller.AuthorDao;
import edu.mum.lms.controller.BookDao;
import edu.mum.lms.controller.PersonDao;
import edu.mum.lms.entity.Author;
import edu.mum.lms.entity.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class AddBook extends ControllerBase {

    @FXML
    private TextField title;
    @FXML
    private TextField isbn;
    @FXML
    private TextField maxCheckout;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField street;
    @FXML
    private TextField city;
    @FXML
    private TextField state;
    @FXML
    private TextField zip;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField credentials;
    @FXML
    private TextField shortBio;
    @FXML
    private TableView<Author> authorTable;
    @FXML
    private TableColumn<Author, String> firstNameColumn;
    @FXML
    private TableColumn<Author, String> lastNameColumn;
    @FXML
    private TableColumn<Author, String> streetColumn;
    @FXML
    private TableColumn<Author, String> cityColumn;
    @FXML
    private TableColumn<Author, String> stateColumn;
    @FXML
    private TableColumn<Author, String> zipColumn;
    @FXML
    private TableColumn<Author, String> phoneNumberColumn;
    @FXML
    private TableColumn<Author, String> credentialsColumn;
    @FXML
    private TableColumn<Author, String> shortBioColumn;

    @FXML
    private TableView<Book> searchTable;
    @FXML
    private TableColumn<Book, String> bookISBN;
    @FXML
    private TableColumn<Book, String> bookTitle;
    @FXML
    private TableColumn<Book, String> bookCopies;
    @FXML
    private TableColumn<Book, String> bookCheckoutDays;
    @FXML
    private Text alertMessage;

    PersonDao pdao = new PersonDao();
    AuthorDao adao = new AuthorDao();
    BookDao bdao = new BookDao();
    JDBCUtil db = new JDBCUtil();

    private List<Author> authors = new ArrayList<Author>();

    @FXML
    public void addBook() {
        Book book = new Book();
        book.setTitle(title.getText());
        book.setIsbn(isbn.getText());
        book.setMaxCheckout(Integer.parseInt(maxCheckout.getText()));
        book.setAuthors(authors);
        bdao.addBook(book);

        // clear form fields
        TextField[] textFields = new TextField[] { isbn, title, maxCheckout, firstName, lastName, street, city, state,
                zip, phoneNumber, credentials, shortBio };
        clearFormTextField(textFields);
        authorTable.setItems(null);
        
        showMessage("Book added successfully !", alertMessage);

    }

    @FXML
    public void addAuthor() {
        Author author = new Author(firstName.getText(), lastName.getText(), street.getText(), city.getText(),
                state.getText(), Integer.parseInt(zip.getText()), phoneNumberColumn.getText(), credentials.getText(),
                shortBio.getText());
        System.out.println(author.getFirstName());

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("lastName"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("street"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("city"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("state"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("zip"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("phoneNo"));
        credentialsColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("credentials"));
        shortBioColumn.setCellValueFactory(new PropertyValueFactory<Author, String>("shortBio"));

        authors.add(author);

        ObservableList<Author> data = FXCollections.observableArrayList(authors);
        authorTable.setItems(data);

        // clear form fields
        TextField[] textFields = new TextField[] { firstName, lastName, street, city, state, zip, phoneNumber,
                credentials, shortBio };
        clearFormTextField(textFields);
    }

}
