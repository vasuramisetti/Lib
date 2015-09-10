package edu.mum.lms.view;

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
        System.out.println(node);
        libraryHolder.getChildren().setAll(node);
    }

}