package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.ui.components.ParcelCard;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        return new ParcelCard(parcel);
    }
}
