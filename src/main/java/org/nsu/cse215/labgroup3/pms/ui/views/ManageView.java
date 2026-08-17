package org.nsu.cse215.labgroup3.pms.ui.views;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.nsu.cse215.labgroup3.pms.core.ui.View;

public class ManageView extends View {
    public static final String VIEW_NAME = "views/manage-view";

    public ManageView(Scene scene) {
        super(scene);
    }

    @Override
    protected Parent loadRoot() throws Exception {
        return loadRootFromResourceFXML(VIEW_NAME);
    }
}
