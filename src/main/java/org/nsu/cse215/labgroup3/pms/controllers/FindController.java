package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.utils.DurationFormatter;
import org.nsu.cse215.labgroup3.pms.utils.WeightFormatter;

import java.time.ZonedDateTime;
import java.util.Optional;

public class FindController {
    private final Application application = Application.getInstance();

    private final PauseTransition transition = new PauseTransition(Duration.millis(250));
    private String searchQuery = "";

    @FXML
    public TextField searchField;

    @FXML
    public VBox searchResults;

    @FXML
    public Label noSearchResultFoundLabel;

    @FXML
    public VBox searchResultParcelDetails;

    @FXML
    public Label searchResultParcelId;

    @FXML
    public Label searchResultParcelDescription;

    @FXML
    public Label searchResultParcelFromName;

    @FXML
    public Label searchResultParcelFromAddress;

    @FXML
    public Label searchResultParcelToName;

    @FXML
    public Label searchResultParcelToAddress;

    @FXML
    public Label searchResultParcelWeight;

    @FXML
    public Label searchResultParcelStatus;

    @FXML
    public Label searchResultParcelETA;

    @FXML
    public void onSearchFieldKeyReleased(KeyEvent keyEvent) {
        if (searchQuery.equals(searchField.textProperty().get())) {
            return;
        }

        searchQuery = searchField.textProperty().get();

        if (noSearchResultFoundLabel.isVisible() || searchResultParcelDetails.isVisible()) {
            Platform.runLater(() -> {
                noSearchResultFoundLabel.setVisible(false);
                searchResultParcelDetails.setVisible(false);
            });
        }

        transition.stop();
        transition.setOnFinished(this::performSearch);
        transition.playFromStart();
    }

    private void performSearch(ActionEvent actionEvent) {
        String query = searchField.textProperty().get();

        System.out.println("Searching now: " + query);
        Optional<Parcel> result = application.database.getParcel(query);

        Platform.runLater(() -> {
            if (result.isEmpty()) {
                noSearchResultFoundLabel.setVisible(true);
                searchResultParcelDetails.setVisible(false);
            }
            else {
                Parcel parcel = result.get();

                noSearchResultFoundLabel.setVisible(false);
                searchResultParcelDetails.setVisible(true);

                searchResultParcelId.setText(parcel.getId());
                searchResultParcelDescription.setText(parcel.getDescription());
                searchResultParcelFromName.setText(parcel.getFrom().getUser().getName());
                searchResultParcelFromAddress.setText(parcel.getFrom().getFullAddress());
                searchResultParcelToName.setText(parcel.getTo().getUser().getName());
                searchResultParcelToAddress.setText(parcel.getTo().getFullAddress());
                searchResultParcelWeight.setText(WeightFormatter.format(parcel.getWeight()));
                searchResultParcelStatus.setText(parcel.getStatus().prettyName);
                searchResultParcelStatus.setTextFill(parcel.getStatus().color);
                searchResultParcelETA.setText(DurationFormatter.format(java.time.Duration.between(ZonedDateTime.now(), parcel.getExpectedTimeOfArrival())));
            }
        });
    }
}
