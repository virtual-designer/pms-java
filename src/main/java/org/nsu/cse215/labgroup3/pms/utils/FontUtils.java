package org.nsu.cse215.labgroup3.pms.utils;

import javafx.scene.text.Font;

import java.io.IOException;

public class FontUtils {
    public static Font loadFontFromResource(String resourceName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (var stream = classLoader.getResourceAsStream(resourceName)) {
            return Font.loadFont(stream, 25);
        }
    }

    public static Font loadDefaultFont() throws IOException {
        return loadFontFromResource("fonts/GoogleSansVariable.ttf");
    }
}
