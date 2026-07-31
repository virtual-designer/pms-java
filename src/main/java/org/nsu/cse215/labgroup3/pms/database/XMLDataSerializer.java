package org.nsu.cse215.labgroup3.pms.database;

public interface XMLDataSerializer<T> {
    String encode(T data);
    T decode(String data);
}
