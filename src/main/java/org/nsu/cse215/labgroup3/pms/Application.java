package org.nsu.cse215.labgroup3.pms;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.core.ui.View;
import org.nsu.cse215.labgroup3.pms.ui.views.FindView;
import org.nsu.cse215.labgroup3.pms.ui.views.HomeView;

import java.util.*;
import java.util.function.Consumer;

public class Application extends javafx.application.Application {
    private final StackPane stackPane = new StackPane();
    private final VBox rootBox = new VBox(stackPane);
    private final Scene scene = new Scene(rootBox, 750, 500);
    public final View homeScene = new HomeView(scene);
    public final View findScene = new FindView(scene);

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

    private void animatePushView(Node oldRoot, Node newRoot) {
        final TranslateTransition leftTransition = new TranslateTransition(Duration.millis(200), oldRoot);

        leftTransition.fromXProperty().set(0.0);
        leftTransition.toXProperty().set(-750.0);

        final TranslateTransition rightTransition = new TranslateTransition(Duration.millis(200), newRoot);

        rightTransition.fromXProperty().set(750.0);
        rightTransition.toXProperty().set(0.0);

        leftTransition.setOnFinished(event -> {
            leftTransition.getNode().setTranslateX(750.0);
            leftTransition.getNode().setVisible(false);
            rightTransition.getNode().setVisible(true);
            rightTransition.play();
        });

        leftTransition.play();
    }

    private void animatePopView(Node oldRoot, Node newRoot, Consumer<Node> onFinish) {
        final TranslateTransition rightTransition = new TranslateTransition(Duration.millis(200), oldRoot);

        rightTransition.fromXProperty().set(0.0);
        rightTransition.toXProperty().set(750.0);

        final TranslateTransition leftTransition = new TranslateTransition(Duration.millis(200), newRoot);

        leftTransition.fromXProperty().set(-750.0);
        leftTransition.toXProperty().set(0.0);

        rightTransition.setOnFinished(event -> {
            rightTransition.getNode().setTranslateX(-750.0);
            rightTransition.getNode().setVisible(false);
            leftTransition.getNode().setVisible(true);
            leftTransition.play();
            onFinish.accept(newRoot);
        });

        rightTransition.play();
    }

    public void pushView(View view) {
        boolean empty = stackPane.getChildren().isEmpty();
        Node oldRoot = null;

        if (!empty) {
            oldRoot = Objects.requireNonNull(stackPane.getChildren().get(stackPane.getChildren().size() - 1));
            view.root.setVisible(false);
        }

        stackPane.getChildren().add(view.root);

        if (!empty) {
            animatePushView(oldRoot, view.root);
        }
    }

    public void popView() {
        if (stackPane.getChildren().size() >= 2) {
            Node newRoot = stackPane.getChildren().get(stackPane.getChildren().size() - 2);
            Node oldRoot = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
            animatePopView(oldRoot, newRoot, (node) -> stackPane.getChildren().remove(stackPane.getChildren().size() - 1));
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
