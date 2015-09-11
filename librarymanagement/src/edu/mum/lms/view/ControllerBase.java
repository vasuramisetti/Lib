package edu.mum.lms.view;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ControllerBase {
    
    public void clearFormTextField(TextField[] textFields) {
        for(TextField textField : textFields) {
            textField.setText("");
        }
    }
    
    public void showMessage(String message, Text alertId) {
        alertId.setText(message);
        createFader(alertId);
    }
    
    private FadeTransition createFader(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);

        return fade;
    }

}
