package org.nsu.cse215.labgroup3.pms.ui.components;

import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class Alert extends javafx.scene.control.Alert {
    public Alert(AlertType alertType) {
        super(alertType);
        initialize();
    }

    public Alert(AlertType alertType, String s, ButtonType... buttonTypes) {
        super(alertType, s, buttonTypes);
        initialize();
    }

    private void initialize() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        DialogPane dialogPane = getDialogPane();
        dialogPane.setHeader(new VBox());
        dialogPane.getChildren().removeFirst();
        dialogPane
                .getStylesheets()
                .add(Objects.requireNonNull(classLoader.getResource("ui/global.css")).toExternalForm());
        dialogPane
                .getStylesheets()
                .add(Objects.requireNonNull(classLoader.getResource("ui/dialog.css")).toExternalForm());
        dialogPane
            .getStyleClass().add("dialog");
    }
}
