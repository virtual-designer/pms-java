package org.nsu.cse215.labgroup3.pms.ui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import org.nsu.cse215.labgroup3.pms.core.ui.View;

public class FindScene extends View {
    public static final String SCENE_NAME = "find";

    public FindScene(Scene scene) throws Exception {
        super(scene);
        loadCSSFromResource(SCENE_NAME);
    }

    @Override
    protected Parent loadRoot() throws Exception {
        return loadRootFromResourceFXML(SCENE_NAME);
    }

    @Override
    protected void render() {}
}
