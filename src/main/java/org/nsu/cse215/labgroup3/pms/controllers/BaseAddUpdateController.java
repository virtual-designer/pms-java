package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Address;
import org.nsu.cse215.labgroup3.pms.database.models.DeliveryStatus;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.nsu.cse215.labgroup3.pms.forms.converters.DateConverter;
import org.nsu.cse215.labgroup3.pms.forms.converters.DeliveryStatusConverter;
import org.nsu.cse215.labgroup3.pms.forms.validators.AddFormValidator;

import java.time.Instant;
import java.util.Optional;

public abstract class BaseAddUpdateController {
    protected final Application application = Application.getInstance();

    @FXML
    public TextField trackingID;

    @FXML
    public TextArea description;

    @FXML
    public ChoiceBox<DeliveryStatus> status;

    @FXML
    public TextField weight;

    @FXML
    public DatePicker estimatedTimeOfArrival;

    @FXML
    public TextArea receiverAddress;

    @FXML
    public TextField receiverUsername;

    @FXML
    public TextField receiverName;

    @FXML
    public TextArea senderAddress;

    @FXML
    public TextField senderUsername;

    @FXML
    public TextField senderName;

    @FXML
    public Button submitButton;

    protected final AddFormValidator validator = new AddFormValidator();

    @FXML
    public void initialize() {
        estimatedTimeOfArrival.setConverter(new DateConverter());

        status.setConverter(new DeliveryStatusConverter());
        status.setValue(DeliveryStatus.PROCESSING);
        status.getItems().addAll(DeliveryStatus.values());

        validator.attachValidatorOnTextInput(trackingID);
        validator.attachValidatorOnTextInput(description);
        validator.attachValidatorOnTextInput(weight);
        validator.attachValidatorOnDatePicker(estimatedTimeOfArrival);
        validator.attachValidatorOnTextInput(receiverAddress);
        validator.attachValidatorOnTextInput(receiverName);
        validator.attachValidatorOnTextInput(receiverUsername);
        validator.attachValidatorOnTextInput(senderAddress);
        validator.attachValidatorOnTextInput(senderName);
        validator.attachValidatorOnTextInput(senderUsername);
    }

    @FXML
    public void onFormSubmit(ActionEvent ignored) {
        boolean result = validator.validateAll(
            trackingID,
            description,
            weight,
            estimatedTimeOfArrival,
            receiverAddress,
            receiverName,
            receiverUsername,
            senderAddress,
            senderName,
            senderUsername
        );

        if (!result) {
            submitButton.setTooltip(new Tooltip("There are validation errors, please correct them first."));

            Platform.runLater(() -> {
                Bounds bounds = submitButton.localToScreen(submitButton.getBoundsInLocal());
                submitButton.getTooltip().show(submitButton, bounds.getMinX(), bounds.getMaxY());
                submitButton.getTooltip().setShowDuration(Duration.millis(5000));
                submitButton.getTooltip().setAutoHide(true);
            });
        }
        else {
            submitButton.setTooltip(null);

            Optional<User> existingFromUser = application.database.findUserByUsername(senderUsername.getText());
            Optional<User> existingToUser = application.database.findUserByUsername(receiverUsername.getText());

            onPreFormSubmitValid(existingFromUser.orElse(null), existingToUser.orElse(null));

            User sender = existingFromUser.orElseGet(() -> {
                User user = new User(application.database.nextUserId(), senderName.getText(), senderUsername.getText(), Instant.now());
                application.database.insertUser(user);
                return user;
            });

            User receiver = existingToUser.orElseGet(() -> {
                User user = new User(application.database.nextUserId(), receiverName.getText(), receiverUsername.getText(), Instant.now());
                application.database.insertUser(user);
                return user;
            });

            Parcel parcel = new Parcel();

            parcel.setFrom(new Address(sender, senderAddress.getText()));
            parcel.setTo(new Address(receiver, receiverAddress.getText()));
            parcel.setId(trackingID.getText());
            parcel.setDescription(description.getText());
            parcel.setWeight(Double.parseDouble(weight.getText()));
            parcel.setExpectedDateOfArrival(estimatedTimeOfArrival.getValue());
            parcel.setStatus(status.getValue());

            onFormSubmitValid(sender, receiver, parcel);
        }
    }

    protected void onPreFormSubmitValid(User existingFromUser, User existingToUser) {

    }

    protected abstract void onFormSubmitValid(User sender, User receiver, Parcel parcel);
}
