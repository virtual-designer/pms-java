package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.scene.control.ButtonType;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.nsu.cse215.labgroup3.pms.ui.components.Alert;

public class UpdateController extends BaseAddUpdateController {
    private static UpdateController instance;
    private Parcel previousParcel = null;

    public UpdateController() {
        if (instance != null) {
            throw new IllegalStateException("Multiple instances of singleton: " + UpdateController.class);
        }

        instance = this;
    }

    public static UpdateController getInstance() {
        return instance;
    }

    public void setPreviousTrackingID(String previousTrackingID) {
        this.previousParcel = application.database.getParcel(previousTrackingID).orElseThrow();
        update();
    }

    private void update() {
        trackingID.textProperty().setValue(previousParcel.getId());
        description.textProperty().setValue(previousParcel.getDescription());
        status.setValue(previousParcel.getStatus());
        weight.textProperty().setValue(previousParcel.getWeight().toString());
        estimatedTimeOfArrival.setValue(previousParcel.getExpectedDateOfArrival());
        receiverAddress.textProperty().setValue(previousParcel.getTo().getFullAddress());
        receiverName.textProperty().setValue(previousParcel.getTo().getUser().getName());
        receiverUsername.textProperty().setValue(previousParcel.getTo().getUser().getUsername());
        senderAddress.textProperty().setValue(previousParcel.getFrom().getFullAddress());
        senderName.textProperty().setValue(previousParcel.getFrom().getUser().getName());
        senderUsername.textProperty().setValue(previousParcel.getFrom().getUser().getUsername());
    }

    @Override
    protected void onPreFormSubmitValid(User existingFromUser, User existingToUser) {
        if (previousParcel != null) {
            application.database.deleteParcel(previousParcel.getId());
        }
    }

    @Override
    public void onFormSubmitValid(User sender, User receiver, Parcel parcel) {
        application.database.insertParcel(parcel);

        try {
            application.database.save();
            ListController.getInstance().update();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The changes to the parcel have been saved.", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving parcel: " + e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
}
