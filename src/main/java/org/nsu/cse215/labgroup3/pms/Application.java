package org.nsu.cse215.labgroup3.pms;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nsu.cse215.labgroup3.pms.core.ui.View;
import org.nsu.cse215.labgroup3.pms.ui.scenes.FindScene;
import org.nsu.cse215.labgroup3.pms.ui.scenes.HomeScene;

import java.util.*;

public class Application extends javafx.application.Application {
    private final StackPane stackPane = new StackPane();
    private final VBox rootBox = new VBox(stackPane);
    private final Scene scene = new Scene(rootBox, 750, 500);
    public final View homeScene = new HomeScene(scene);
    public final View findScene = new FindScene(scene);

    private static Application instance;

    public Application() throws Exception {
        if (instance == null) {
            instance = this;
            return;
        }

        throw new IllegalStateException("An Application object already exists!");
    }

    public static Application getInstance() {
        return Objects.requireNonNull(instance);
    }

    public void pushView(View view) {
        if (!stackPane.getChildren().isEmpty()) {
            stackPane.getChildren().get(stackPane.getChildren().size() - 1).setVisible(false);
        }

        stackPane.getChildren().add(view.root);
    }

    public void popView() {
        stackPane.getChildren().remove(stackPane.getChildren().size() - 1);

        if (!stackPane.getChildren().isEmpty()) {
            stackPane.getChildren().get(stackPane.getChildren().size() - 1).setVisible(true);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Courier Management Service");
        rootBox.getStyleClass().add("rootBox");
        scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ui/global.css")).toExternalForm());
        stackPane.getChildren().add(homeScene.root);
        stage.setScene(scene);
        stage.show();
    }
}
