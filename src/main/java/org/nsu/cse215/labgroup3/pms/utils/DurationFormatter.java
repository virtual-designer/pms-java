package org.nsu.cse215.labgroup3.pms.utils;

import java.time.Duration;

public class DurationFormatter {
    public static String format(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            return "Today";
        }

        StringBuilder builder = new StringBuilder();

        long days = duration.toDaysPart();
        long hours = duration.toHoursPart();

        if (days >= 1) {
            builder.append(" ").append(days).append(" day").append(days > 1 ? "s" : "");
        }

        if (hours >= 1) {
            builder.append(" ").append(hours).append(" hour").append(hours > 1 ? "s" : "");
        }

        if (builder.isEmpty()) {
            return "Today";
        }

        return builder.toString().trim();
    }
}
