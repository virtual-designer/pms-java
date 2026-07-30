package org.nsu.cse215.labgroup3.pms.core.ui;

public class Size {
    public final double width;
    public final double height;

    public Size(double size) {
        this(size, size);
    }

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Size(width=%f, height=%f)".formatted(width, height);
    }
}
