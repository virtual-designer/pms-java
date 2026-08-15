package org.nsu.cse215.labgroup3.pms.database.serializers;

import org.nsu.cse215.labgroup3.pms.database.XMLDataSerializer;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class LocalDateSerializer implements XMLDataSerializer<LocalDate> {
    @Override
    public String encode(LocalDate data) {
        return data.toString();
    }

    @Override
    public LocalDate decode(String data) {
        return LocalDate.parse(data);
    }
}
