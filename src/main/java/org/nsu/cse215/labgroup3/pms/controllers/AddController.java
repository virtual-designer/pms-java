package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.scene.control.ButtonType;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.nsu.cse215.labgroup3.pms.ui.components.Alert;

public class AddController extends BaseAddUpdateController {
    @Override
    public void onFormSubmitValid(User sender, User receiver, Parcel parcel) {
        boolean added = application.database.insertParcel(parcel);

        if (!added) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Parcel with tracking ID \"%s\" already exists.".formatted(parcel.getId()), ButtonType.OK);
            alert.show();
            return;
        }

        try {
            application.database.save();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The new parcel has been saved.", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving parcel: " + e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
}
