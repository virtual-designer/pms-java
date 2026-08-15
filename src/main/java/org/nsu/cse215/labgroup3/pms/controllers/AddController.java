package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Address;
import org.nsu.cse215.labgroup3.pms.database.models.DeliveryStatus;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.nsu.cse215.labgroup3.pms.forms.converters.DateConverter;
import org.nsu.cse215.labgroup3.pms.forms.converters.validators.AddFormValidator;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

public class AddController {
    private final Application application = Application.getInstance();

    @FXML
    public TextField trackingID;

    @FXML
    public TextArea description;

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

    private final AddFormValidator validator = new AddFormValidator();

    @FXML
    public void initialize() {
        estimatedTimeOfArrival.setConverter(new DateConverter());

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
    public void onFormSubmit(ActionEvent actionEvent) {
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

            Optional<User> existingUser = application.database.findUser(senderUsername.getText());
            User sender = existingUser.orElseGet(() -> {
                User user = new User(application.database.nextUserId(), senderName.getText(), senderUsername.getText(), Instant.now());
                application.database.insertUser(user);
                return user;
            });

            existingUser = application.database.findUser(receiverUsername.getText());
            User receiver = existingUser.orElseGet(() -> {
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
            parcel.setExpectedTimeOfArrival(estimatedTimeOfArrival.getValue());
            parcel.setStatus(DeliveryStatus.PROCESSING);

            application.database.insertParcel(parcel);

            try {
                application.database.save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
