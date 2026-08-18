package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.ui.components.Alert;
import org.nsu.cse215.labgroup3.pms.utils.DurationFormatter;
import org.nsu.cse215.labgroup3.pms.utils.WeightFormatter;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ParcelCardController {
    private final Application application = Application.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, y");

    @FXML
    public Label trackingID;

    @FXML
    public VBox expandIconWrapper;

    @FXML
    public Label parcelETA;

    @FXML
    public Label parcelStatus;

    @FXML
    public Circle parcelStatusCircle;

    @FXML
    public VBox collapsiblePane;

    @FXML
    public ImageView expandIcon;

    @FXML
    public Label description;

    @FXML
    public Label weight;

    @FXML
    public Label etaDate;

    @FXML
    public Label parcelSenderName;

    @FXML
    public Label parcelSenderAddress;

    @FXML
    public Label parcelReceiverName;

    @FXML
    public Label parcelReceiverAddress;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    @FXML
    public void initialize() {
        expandIconWrapper.setOnMouseClicked(new EventHandler<>() {
            private boolean expanded = false;

            @Override
            public void handle(MouseEvent mouseEvent) {
                expanded = !expanded;

                if (expanded) {
                    collapsiblePane.setManaged(true);
                    collapsiblePane.setMaxHeight(Double.MAX_VALUE);
                    expandIcon.getStyleClass().add("expanded");
                } else {
                    collapsiblePane.setManaged(false);
                    collapsiblePane.setMaxHeight(0);
                    expandIcon.getStyleClass().remove("expanded");
                }

                collapsiblePane.setVisible(expanded);
                collapsiblePane.applyCss();
                collapsiblePane.layout();
            }
        });
    }

    private void setActionHandlers(Parcel parcel) {
        editButton.setOnAction(_ -> {
            UpdateController.getInstance().setPreviousTrackingID(parcel.getId());
            application.pushView(application.updateView);
        });

        deleteButton.setOnAction(_ -> {
            Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the parcel %s?\nThis cannot be undone.".formatted(parcel.getId()),
                ButtonType.YES, ButtonType.NO
            );

            alert.setTitle("Delete Parcel");
            alert.showAndWait().ifPresent(action -> {
                if (action == ButtonType.YES) {
                    application.database.deleteParcel(parcel.getId());

                    try {
                        application.database.save();
                    } catch (TransformerException | IOException e) {
                        throw new RuntimeException(e);
                    }

                    ListController.getInstance().update();
                }
            });
        });
    }

    public void update(Parcel parcel) {
        Color color = parcel.getStatus().color;
        ZonedDateTime arrivalTime = parcel.getExpectedDateOfArrival()
                .atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime now = ZonedDateTime.now();
        String formattedETA = DurationFormatter.format(
            Duration.between(
                now,
                arrivalTime
            )
        );

        trackingID.setText(parcel.getId());
        parcelStatusCircle.setFill(color);
        parcelStatus.setText(parcel.getStatus().prettyName);
        parcelStatus.setStyle("-fx-text-fill: #" + color.toString().substring(2));
        parcelETA.setText(formattedETA);

        description.setText(parcel.getDescription());
        weight.setText(WeightFormatter.format(parcel.getWeight()));
        etaDate.setText(formatter.format(parcel.getExpectedDateOfArrival()));

        parcelSenderName.setText(parcel.getFrom().getUser().getName());
        parcelReceiverName.setText(parcel.getTo().getUser().getName());
        parcelSenderAddress.setText(parcel.getFrom().getFullAddress());
        parcelReceiverAddress.setText(parcel.getTo().getFullAddress());

        setActionHandlers(parcel);
    }
}
