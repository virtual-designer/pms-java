package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.nsu.cse215.labgroup3.pms.Application;

public class HomeController {

    @FXML
    public Button findParcelButton;

    @FXML
    public void onFindParcelButtonClick(ActionEvent event) {
        Stage stage = (Stage) findParcelButton.getScene().getWindow();
        Application application = Application.getInstance();
        application.pushView(application.findScene);
    }
}
