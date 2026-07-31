package org.nsu.cse215.labgroup3.pms.utils;

public class WeightFormatter {
    public static String format(double weight) {
        double value = weight;
        String unit = "g";

        if (value >= 1000.0) {
            value /= 1000.0;
            unit = "kg";
        }

        return "%.2f %s".formatted(value, unit);
    }
}
