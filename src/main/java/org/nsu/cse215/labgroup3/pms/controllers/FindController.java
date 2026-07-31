package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nsu.cse215.labgroup3.pms.Application;

public class FindController {
    @FXML
    public VBox backIconWrapper;

    @FXML
    public TextField searchField;

    @FXML
    public void onBackIconWrapperClick(MouseEvent event) {
        Stage stage = (Stage) backIconWrapper.getScene().getWindow();
        Application application = Application.getInstance();
        application.popView();
    }
}
