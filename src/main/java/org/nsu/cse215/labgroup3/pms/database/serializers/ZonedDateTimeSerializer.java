package org.nsu.cse215.labgroup3.pms.database.serializers;

import org.nsu.cse215.labgroup3.pms.database.XMLDataSerializer;

import java.time.Instant;
import java.time.ZonedDateTime;

public class ZonedDateTimeSerializer implements XMLDataSerializer<ZonedDateTime> {
    @Override
    public String encode(ZonedDateTime data) {
        return data.toString();
    }

    @Override
    public ZonedDateTime decode(String data) {
        return ZonedDateTime.parse(data);
    }
}
