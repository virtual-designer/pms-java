package org.nsu.cse215.labgroup3.pms.database;

public class SerializationException extends RuntimeException {
    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Exception exception) {
        super(exception);
    }
}
