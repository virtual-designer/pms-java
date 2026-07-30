package org.nsu.cse215.labgroup3.pms.ui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.nsu.cse215.labgroup3.pms.core.ui.View;

public class HomeScene extends View {
    public static final String VIEW_NAME = "home";

    public HomeScene(Scene scene) throws Exception {
        super(scene);
        loadCSSFromResource(VIEW_NAME);
    }

    @Override
    protected Parent loadRoot() throws Exception {
        return loadRootFromResourceFXML(VIEW_NAME);
    }

    @Override
    protected void render() {}
}
