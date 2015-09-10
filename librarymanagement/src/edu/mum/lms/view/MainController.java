package edu.mum.lms.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the entire layout.
 */
public class MainController {

    /** Holder of a switchable vista. */
    @FXML
    private StackPane libraryHolder;

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setLibraryInternalScene(Node node) {
        libraryHolder.getChildren().setAll(node);
    }
    
    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        Navigator.loadScene(Navigator.CHECKOUT_LIST);
    }
    
    @FXML
    public void goToCheckoutForm() {
        Navigator.loadScene(Navigator.CHECKOUT_FORM);
    }
    
    @FXML
    public void goToSearchBook() {
        Navigator.loadScene(Navigator.CHECKOUT_LIST);
    }
    
    @FXML
    public void goToAddBook() {
        //Navigator.loadScene(Navigator.CHECKOUT_FORM);
    }

}