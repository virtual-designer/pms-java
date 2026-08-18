package org.nsu.cse215.labgroup3.pms.forms.validators;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;

public class AddFormValidator {
    private ValidationError validateTrackingID(String trackingID) {
        if (!trackingID.matches("^[0-9A-Za-z_-]+$")) {
            return new ValidationError("Tracking ID must only contain numbers, letters and underscores '_' or hyphens '-'");
        }

        return null;
    }

    private ValidationError validateWeight(String weight) {
        try {
            Double.parseDouble(weight);
        }
        catch (Exception _) {
            return new ValidationError("Weight must be a valid numeric value in grams");
        }

        return null;
    }

    private ValidationError validateEstimatedTimeOfArrival(LocalDate date) {
        if (date == null || date.isBefore(LocalDate.now())) {
            return new ValidationError("Date must be provided and not be in the past");
        }

        return null;
    }

    private ValidationError validateInputValueNotBlank(String value) {
        if (value.isBlank()) {
            return new ValidationError("Input value must be provided");
        }

        return null;
    }

    public boolean validateControl(Control element, String value) {
        String id = element.getId();
        ValidationError error = switch (id) {
            case "trackingID" -> validateTrackingID(value);
            case "description",
                 "senderName", "senderUsername", "senderAddress",
                 "receiverName", "receiverUsername", "receiverAddress" -> validateInputValueNotBlank(value);
            case "weight" -> validateWeight(value);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };

        if (error == null) {
            element.setTooltip(null);
            return true;
        }
        else {
            element.setTooltip(new Tooltip(error.message()));
            return false;
        }
    }

    public boolean validateControl(TextInputControl element, String value) {
        boolean result = validateControl((Control) element, value);

        if (result) {
            element.getStyleClass().remove("textFieldError");
        }
        else {
            element.getStyleClass().add("textFieldError");
        }

        return result;
    }

    public boolean validateControl(TextInputControl element) {
        return validateControl(element, element.textProperty().getValue());
    }

    public boolean validateControl(DatePicker element, LocalDate newValue) {
        String id = element.getId();
        ValidationError error = switch (id) {
            case "estimatedTimeOfArrival" -> validateEstimatedTimeOfArrival(newValue);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };

        if (error == null) {
            element.setTooltip(null);
            element.getStyleClass().remove("textFieldError");
            return true;
        }
        else {
            element.setTooltip(new Tooltip(error.message()));
            element.getStyleClass().add("textFieldError");
            return false;
        }
    }

    public boolean validateControl(DatePicker element) {
        return validateControl(element, element.getValue());
    }

    public void attachValidatorOnTextInput(TextInputControl element) {
        element.textProperty().addListener((_, _, newValue) -> {
            validateControl(element, newValue);
        });
    }

    public void attachValidatorOnDatePicker(DatePicker element) {
        element.valueProperty().addListener((_, _, newValue) -> {
            validateControl(element, newValue);
        });
    }

    public boolean validateAll(Control... elements) {
        boolean result = true;

        for (Control element : elements) {
            boolean newResult;

            if (element instanceof TextInputControl textInputControl) {
                newResult = validateControl(textInputControl);
            }
            else if (element instanceof DatePicker datePicker) {
                newResult = validateControl(datePicker);
            }
            else {
                throw new IllegalStateException("Invalid element encountered");
            }

            result = result && newResult;
        }

        return result;
    }
}
