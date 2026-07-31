package org.nsu.cse215.labgroup3.pms.database.models;

import javafx.scene.paint.Color;

public enum DeliveryStatus {
    DELIVERED(Color.GREEN),
    IN_TRANSIT(Color.YELLOW),
    PROCESSING(Color.GRAY),
    RECEIVED(Color.GRAY),
    REJECTED(Color.ORANGE);

    public final String prettyName;
    public final Color color;

    DeliveryStatus(Color color) {
        this.color = color;
        this.prettyName = computePrettyName();
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
