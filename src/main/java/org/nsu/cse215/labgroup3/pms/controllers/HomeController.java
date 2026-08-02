package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import org.nsu.cse215.labgroup3.pms.Application;

public class HomeController {
    private static final String LICENSE_LINK = "https://www.gnu.org/licenses/gpl-3.0";
    private final Application application = Application.getInstance();

    @FXML
    public Button findParcelButton;

    @FXML
    public Button addParcelButton;

    @FXML
    public Button updateParcelButton;

    @FXML
    public Hyperlink licenseLink;

    @FXML
    public void onFindParcelButtonClick(ActionEvent event) {
        application.pushView(application.findView);
    }

    @FXML
    public void onAddParcelButtonClick(ActionEvent event) {
        application.pushView(application.addView);
    }

    @FXML
    public void onUpdateParcelButtonClick(ActionEvent actionEvent) {
        application.pushView(application.updateView);
    }

    @FXML
    public void onLicenseLinkClick(ActionEvent actionEvent) {
        application.getHostServices().showDocument(LICENSE_LINK);
    }
}
