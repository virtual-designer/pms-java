package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.ui.components.Alert;
import org.nsu.cse215.labgroup3.pms.utils.DurationFormatter;
import org.nsu.cse215.labgroup3.pms.utils.WeightFormatter;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ListController {
    private static ListController instance;
    private final Application application = Application.getInstance();
    private final PauseTransition transition = new PauseTransition(Duration.millis(250));

    private String searchQuery = "";

    @FXML
    public Label noSearchResultFoundLabel;

    @FXML
    public TextField searchField;

    @FXML
    public VBox listBox;

    public ListController() {
        if (instance != null) {
            throw new IllegalStateException("Multiple instances of singleton: " + ListController.class);
        }

        instance = this;
    }

    public static ListController getInstance() {
        return instance;
    }


    @FXML
    public void onSearchFieldKeyReleased(KeyEvent ignored) {
        if (searchQuery.equals(searchField.textProperty().get())) {
            return;
        }

        searchQuery = searchField.textProperty().get();

        if (noSearchResultFoundLabel.isVisible()) {
            Platform.runLater(() -> {
                noSearchResultFoundLabel.setVisible(false);
                listBox.setVisible(false);
            });
        }

        transition.stop();
        transition.setOnFinished(_ -> update());
        transition.playFromStart();
    }

    public void update() {
        listBox.getChildren().clear();

        Map<String, Parcel> parcels = application.database.getAllParcels();
        String query = searchField.textProperty().get().toLowerCase();

        System.out.println("Searching now: " + query);

        Platform.runLater(() -> {
            List<Parcel> results = parcels
                .values()
                .stream()
                .filter(parcel ->
                    parcel.getId().toLowerCase().contains(query) ||
                    parcel.getExpectedDateOfArrival().toString().toLowerCase().contains(query) ||
                    parcel.getStatus().prettyName.toLowerCase().contains(query) ||
                    parcel.getDescription().toLowerCase().contains(query) ||
                    parcel.getWeight().toString().toLowerCase().contains(query) ||
                    parcel.getFrom().getFullAddress().toLowerCase().contains(query) ||
                    parcel.getTo().getFullAddress().toLowerCase().contains(query) ||
                    parcel.getFrom().getUser().getUsername().toLowerCase().contains(query) ||
                    parcel.getFrom().getUser().getName().toLowerCase().contains(query) ||
                    parcel.getFrom().getUser().getId().toString().toLowerCase().contains(query) ||
                    parcel.getTo().getUser().getUsername().toLowerCase().contains(query) ||
                    parcel.getTo().getUser().getName().toLowerCase().contains(query) ||
                    parcel.getTo().getUser().getId().toString().toLowerCase().contains(query)
                )
                .sorted(Comparator.comparing(Parcel::getExpectedDateOfArrival))
                .toList();

            if (results.isEmpty()) {
                noSearchResultFoundLabel.setVisible(true);
                listBox.setVisible(false);
            }
            else {
                noSearchResultFoundLabel.setVisible(false);
                listBox.setVisible(true);

                for (Parcel parcel : results) {
                    listBox.getChildren().add(renderParcel(parcel));
                }

                listBox.setPadding(new Insets(0, 0, 50 * parcels.size(), 0));
            }
        });
    }

    private Node renderParcel(Parcel parcel) {
        VBox rootBox = new VBox();
        HBox rootHBox = new HBox();
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        Image expandIconImage = new Image(
            Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/chevron-left.png")),
            24.0,
            24.0,
            true,
            true
        );
        ImageView expandIcon = new ImageView(expandIconImage);
        VBox expandIconWrapper = new VBox(expandIcon);

        expandIcon.getStyleClass().addAll("expandIcon");
        expandIconWrapper.getStyleClass().addAll("expandIconWrapper");

        Label trackingIDLabel = new Label(parcel.getId());
        trackingIDLabel.getStyleClass().add("trackingID");

        Label statusLabel = new Label(parcel.getStatus().prettyName);
        statusLabel.getStyleClass().add("status");
        statusLabel.setStyle("-fx-text-fill: #" + parcel.getStatus().color.toString().substring(2));

        ZonedDateTime arrivalTime = parcel.getExpectedDateOfArrival()
                .atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime now = ZonedDateTime.now();

        Label dateLabel = new Label(
            DurationFormatter.format(
                java.time.Duration.between(
                    now,
                    arrivalTime
                )
            )
        );

        dateLabel.getStyleClass().add("etaDate");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle circle = new Circle();

        circle.setRadius(10.0);
        circle.setFill(parcel.getStatus().color);

        leftHBox.setSpacing(10.0);
        leftHBox.getChildren().add(trackingIDLabel);
        leftHBox.getChildren().add(circle);
        leftHBox.getChildren().add(statusLabel);
        leftHBox.getChildren().add(dateLabel);
        leftHBox.setAlignment(Pos.CENTER);

        rightHBox.getChildren().add(expandIconWrapper);
        rightHBox.setAlignment(Pos.CENTER);

        rootHBox.getChildren().add(leftHBox);
        rootHBox.getChildren().add(spacer);
        rootHBox.getChildren().add(rightHBox);

        rootBox.getStyleClass().add("parcel");
        rootBox.getChildren().add(rootHBox);

        VBox pane = new VBox();

        pane.setPadding(new Insets(10, 5, 5, 5));
        pane.setVisible(false);
        pane.setManaged(false);

        expandIconWrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {
            private boolean expanded = false;

            @Override
            public void handle(MouseEvent mouseEvent) {
                expanded = !expanded;

                if (expanded) {
                    pane.setManaged(true);
                    pane.setMaxHeight(Double.MAX_VALUE);
                    expandIcon.getStyleClass().add("expanded");
                }
                else {
                    pane.setManaged(false);
                    pane.setMaxHeight(0);
                    expandIcon.getStyleClass().remove("expanded");
                }

                pane.setVisible(expanded);
                pane.applyCss();
                pane.layout();
            }
        });

        VBox descriptionWrapper = new VBox();
        VBox approximateWeight = new VBox();
        VBox etaDate = new VBox();
        VBox parcelSender = new VBox();
        VBox parcelReceiver = new VBox();

        for (Node node : new Node[] {descriptionWrapper, approximateWeight, etaDate, parcelSender, parcelReceiver}) {
            node.getStyleClass().add("infoField");
        }

        parcelSender.getChildren().add(makeLabel("From:", "fieldLabel"));
        parcelSender.getChildren().add(new Label("%s\n%s".formatted(parcel.getFrom().getUser().getName(), parcel.getFrom().getFullAddress())));

        parcelReceiver.getChildren().add(makeLabel("To:", "fieldLabel"));
        parcelReceiver.getChildren().add(new Label("%s\n%s".formatted(parcel.getTo().getUser().getName(), parcel.getTo().getFullAddress())));

        approximateWeight.getChildren().add(makeLabel("Weight (Approximate):", "fieldLabel"));
        approximateWeight.getChildren().add(new Label(WeightFormatter.format(parcel.getWeight())));

        etaDate.getChildren().add(makeLabel("Estimated Date Of Arrival:", "fieldLabel"));
        etaDate.getChildren().add(new Label(parcel.getExpectedDateOfArrival().format(DateTimeFormatter.ofPattern("MMM d, y"))));

        descriptionWrapper.getChildren().add(makeLabel("Description:", "fieldLabel"));
        descriptionWrapper.getChildren().add(new Label(parcel.getDescription()));

        GridPane grid1 = new GridPane();

        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 2);
            grid1.getColumnConstraints().add(cc);
        }

        grid1.setVgap(10);
        grid1.setHgap(10);

        grid1.add(descriptionWrapper, 0, 0);
        grid1.add(approximateWeight, 0, 1);
        grid1.add(etaDate, 1, 1);
        grid1.add(parcelSender, 0, 2);
        grid1.add(parcelReceiver, 1, 2);

        GridPane.setColumnSpan(descriptionWrapper, 2);
        pane.getChildren().add(grid1);

        HBox controls = new HBox();

        controls.setPadding(new Insets(10, 0, 0, 0));
        controls.setSpacing(10.0);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        editButton.getStyleClass().addAll("customButton", "editButton");
        deleteButton.getStyleClass().addAll("customButton", "deleteButton");

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

                    update();
                }
            });
        });

        controls.getChildren().addAll(editButton, deleteButton);

        pane.getChildren().add(controls);
        rootBox.getChildren().add(pane);

        return rootBox;
    }

    private Label makeLabel(String str, String className) {
        Label label = new Label(str);

        label.setWrapText(true);

        if (className != null) {
            label.getStyleClass().add(className);
        }

        return label;
    }
}
