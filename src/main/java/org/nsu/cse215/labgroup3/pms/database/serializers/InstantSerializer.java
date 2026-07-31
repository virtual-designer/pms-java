package org.nsu.cse215.labgroup3.pms.database.serializers;

import org.nsu.cse215.labgroup3.pms.database.XMLDataSerializer;

import java.time.Instant;

public class InstantSerializer implements XMLDataSerializer<Instant> {
    @Override
    public String encode(Instant data) {
        return data.toString();
    }

    @Override
    public Instant decode(String data) {
        return Instant.parse(data);
    }
}
