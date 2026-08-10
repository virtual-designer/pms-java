package org.nsu.cse215.labgroup3.pms.controllers;

import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class AddController {
    public DatePicker estimatedTimeOfArrival;

    private static class DateConverter extends StringConverter<LocalDate> {
        @Override
        public String toString(LocalDate localDate) {
            if (localDate == null) {
                return "";
            }

            return "%s %02d, %04d".formatted(localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US), localDate.getDayOfMonth(), localDate.getYear());
        }

        @Override
        public LocalDate fromString(String s) {
            if (s.isBlank()) {
                return null;
            }

            String[] parts = s.split(",?\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid date string: \"%s\"".formatted(s));
            }

            String monthName = parts[0];
            String dayOfMonth = parts[1];
            String year = parts[2];

            return LocalDate.of(Integer.parseInt(year, 10), Month.valueOf(monthName.toUpperCase()), Integer.parseInt(dayOfMonth, 10));
        }
    }

    public AddController() {
        Platform.runLater(() -> estimatedTimeOfArrival.setConverter(new DateConverter()));
    }
}
