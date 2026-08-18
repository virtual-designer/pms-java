package org.nsu.cse215.labgroup3.pms.forms.converters;

import javafx.util.StringConverter;
import org.nsu.cse215.labgroup3.pms.database.models.DeliveryStatus;

public class DeliveryStatusConverter extends StringConverter<DeliveryStatus> {
    @Override
    public String toString(DeliveryStatus deliveryStatus) {
        return deliveryStatus.prettyName;
    }

    @Override
    public DeliveryStatus fromString(String s) {
        return DeliveryStatus.fromPrettyName(s);
    }
}
