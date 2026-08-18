package org.nsu.cse215.labgroup3.pms.ui.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.nsu.cse215.labgroup3.pms.controllers.ParcelCardController;
import org.nsu.cse215.labgroup3.pms.database.models.Parcel;

import java.io.IOException;

public class ParcelCard extends VBox {
    private Parcel parcel;
    private final ParcelCardController controller;

    public ParcelCard(Parcel parcel) {
        this.parcel = parcel;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        FXMLLoader loader = new FXMLLoader(classLoader.getResource("ui/components/parcel-card.fxml"));
        Node node;

        try {
            node = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller = loader.getController();
        setParcel(parcel);
        getChildren().add(node);
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
        controller.update(parcel);
    }
}
