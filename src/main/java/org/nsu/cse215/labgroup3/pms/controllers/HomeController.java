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
    public Button addParcelButton;

    @FXML
    public Hyperlink licenseLink;

    @FXML
    public Button listParcelsButton;

    @FXML
    public void onAddParcelButtonClick(ActionEvent ignored) {
        application.pushView(application.addView);
    }

    @FXML
    public void onListParcelsButtonClick(ActionEvent ignored) {
        application.pushView(application.manageView);
        ListController.getInstance().update();
    }

    @FXML
    public void onLicenseLinkClick(ActionEvent ignored) {
        application.getHostServices().showDocument(LICENSE_LINK);
    }
}
