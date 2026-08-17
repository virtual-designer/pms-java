package org.nsu.cse215.labgroup3.pms;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.nsu.cse215.labgroup3.pms.core.ui.View;
import org.nsu.cse215.labgroup3.pms.database.Database;
import org.nsu.cse215.labgroup3.pms.ui.views.AddView;
import org.nsu.cse215.labgroup3.pms.ui.views.HomeView;
import org.nsu.cse215.labgroup3.pms.ui.views.ManageView;
import org.nsu.cse215.labgroup3.pms.ui.views.UpdateView;
import org.nsu.cse215.labgroup3.pms.utils.FontUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Application extends javafx.application.Application {
    private static final String STAGE_TITLE = "Courier Management System";

    public final Database database = new Database();

    private final StackPane stackPane = new StackPane();
    private final VBox rootBox = new VBox(stackPane);
    private final Scene scene = new Scene(rootBox, 750, 550);

    public final View homeView = new HomeView(scene);
    public final View addView = new AddView(scene);
    public final View updateView = new UpdateView(scene);
    public final View manageView = new ManageView(scene);

    public final List<View> views = List.of(homeView, addView, updateView, manageView);

    private static Application instance;

    public Application() throws Exception {
        if (instance == null) {
            instance = this;
            database.load();
            return;
        }

        throw new IllegalStateException("An Application object already exists!");
    }

    public static Application getInstance() {
        return Objects.requireNonNull(instance);
    }

    public static Database getDatabase() {
        return getInstance().database;
    }

    private void animatePushView(Node oldRoot, Node newRoot) {
        final TranslateTransition leftTransition = new TranslateTransition(Duration.millis(200), oldRoot);
        final double width = Math.max(oldRoot.getScene().getWidth(), newRoot.getScene().getWidth());

        leftTransition.fromXProperty().set(0.0);
        leftTransition.toXProperty().set(-width);

        final TranslateTransition rightTransition = new TranslateTransition(Duration.millis(200), newRoot);

        rightTransition.fromXProperty().set(width);
        rightTransition.toXProperty().set(0.0);

        leftTransition.setOnFinished(event -> {
            leftTransition.getNode().setTranslateX(width);
            leftTransition.getNode().setVisible(false);
            rightTransition.getNode().setVisible(true);
            rightTransition.play();
        });

        leftTransition.play();
    }

    private void animatePopView(Node oldRoot, Node newRoot, Consumer<Node> onFinish) {
        final TranslateTransition rightTransition = new TranslateTransition(Duration.millis(200), oldRoot);
        final double width = Math.max(oldRoot.getScene().getWidth(), newRoot.getScene().getWidth());

        rightTransition.fromXProperty().set(0.0);
        rightTransition.toXProperty().set(width);

        final TranslateTransition leftTransition = new TranslateTransition(Duration.millis(200), newRoot);

        leftTransition.fromXProperty().set(-width);
        leftTransition.toXProperty().set(0.0);

        rightTransition.setOnFinished(event -> {
            rightTransition.getNode().setTranslateX(-width);
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
            view.getRoot().setVisible(false);
        }

        stackPane.getChildren().add(view.getRoot());

        if (!empty) {
            animatePushView(oldRoot, view.getRoot());
        }
    }

    public void popView() {
        if (stackPane.getChildren().size() >= 2) {
            Node newRoot = stackPane.getChildren().get(stackPane.getChildren().size() - 2);
            Node oldRoot = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
            animatePopView(oldRoot, newRoot, (node) -> stackPane.getChildren().remove(stackPane.getChildren().size() - 1));
        }
    }

    private void initializeViews() throws Exception {
        FontUtils.loadDefaultFonts();

        for (final var view : views) {
            view.initialize();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        initializeViews();
        stage.setTitle(STAGE_TITLE);

        rootBox.getStyleClass().add("rootBox");
        scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ui/global.css")).toExternalForm());
        stackPane.getChildren().add(homeView.getRoot());

        stage.setScene(scene);
        stage.show();
    }
}
