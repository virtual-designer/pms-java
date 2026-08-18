package org.nsu.cse215.labgroup3.pms.database.models;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryStatus {
    DELIVERED(Color.LIGHTGREEN),
    IN_TRANSIT(Color.YELLOW),
    OUT_FOR_DELIVERY(Color.LIGHTBLUE),
    PROCESSING(Color.GRAY),
    RECEIVED(Color.GRAY),
    REJECTED(Color.ORANGE);

    private static Map<String, DeliveryStatus> prettyNameMap;
    public final String prettyName;
    public final Color color;

    DeliveryStatus(Color color) {
        this.color = color;
        this.prettyName = computePrettyName();
        initialize();
    }

    private void initialize() {
        if (prettyNameMap == null) {
            prettyNameMap = new HashMap<>();
        }

        prettyNameMap.put(prettyName, this);
    }

    public static DeliveryStatus fromPrettyName(String prettyName) {
        return prettyNameMap.get(prettyName);
    }

    private String computePrettyName() {
        StringBuilder builder = new StringBuilder();

        for (String part : name().split("_")) {
            builder
                .append(part.charAt(0))
                .append(part.substring(1).toLowerCase())
                .append(" ");
        }

        return builder.toString().stripTrailing();
    }
}
