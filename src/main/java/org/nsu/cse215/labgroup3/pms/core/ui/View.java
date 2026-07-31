package org.nsu.cse215.labgroup3.pms.core.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public abstract class View {
    protected final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    public final Scene scene;
    private Parent root;

    public View(Scene scene) {
        this.scene = scene;
    }

    public void initialize() throws Exception {
        root = loadRoot();
    }

    protected Parent loadRootFromResourceFXML(String resourceName) throws IOException {
        return new FXMLLoader(classLoader.getResource("ui/%s-view.fxml".formatted(resourceName))).load();
    }

    protected void loadCSSFromResource(String resourceName) {
        loadCSSFromResourceRawName("ui/%s-view.css".formatted(resourceName));
    }

    private void loadCSSFromResourceRawName(String resourceName) {
        scene.getStylesheets().add(Objects.requireNonNull(classLoader.getResource(resourceName)).toExternalForm());
    }

    public Parent getRoot() {
        return root;
    }

    protected abstract Parent loadRoot() throws Exception;
}
