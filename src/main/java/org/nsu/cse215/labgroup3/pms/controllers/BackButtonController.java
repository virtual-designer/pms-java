package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.nsu.cse215.labgroup3.pms.Application;

public class BackButtonController {
    private final Application application = Application.getInstance();

    @FXML
    public VBox backIconWrapper;

    @FXML
    public void onBackIconWrapperClick(MouseEvent event) {
        application.popView();
    }
}
