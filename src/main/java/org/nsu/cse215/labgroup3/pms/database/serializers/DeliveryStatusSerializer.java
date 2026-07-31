package org.nsu.cse215.labgroup3.pms.database.serializers;

import org.nsu.cse215.labgroup3.pms.database.XMLDataSerializer;
import org.nsu.cse215.labgroup3.pms.database.models.DeliveryStatus;

public class DeliveryStatusSerializer implements XMLDataSerializer<DeliveryStatus> {
    @Override
    public String encode(DeliveryStatus data) {
        return data.name();
    }

    @Override
    public DeliveryStatus decode(String data) {
        return DeliveryStatus.valueOf(data);
    }
}
