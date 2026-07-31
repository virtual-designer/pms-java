package org.nsu.cse215.labgroup3.pms.ui.views;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.nsu.cse215.labgroup3.pms.core.ui.View;

public class HomeView extends View {
    public static final String VIEW_NAME = "home";

    public HomeView(Scene scene) throws Exception {
        super(scene);
    }

    @Override
    protected Parent loadRoot() throws Exception {
        return loadRootFromResourceFXML(VIEW_NAME);
    }

    @Override
    protected void render() {}
}
