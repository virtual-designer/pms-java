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

    public static Font[] loadDefaultFonts() throws IOException {
        String[] fontVariations = {"Bold", "BoldItalic", "Italic", "Medium", "MediumItalic", "Regular", "SemiBold", "SemiBoldItalic"};
        Font[] fonts = new Font[fontVariations.length];
        int i = 0;

        for (String variation : fontVariations) {
            fonts[i++] = loadFontFromResource("fonts/GoogleSans-%s.ttf".formatted(variation));
        }

        return fonts;
    }
}
