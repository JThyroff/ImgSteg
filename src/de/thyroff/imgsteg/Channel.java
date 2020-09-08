package de.thyroff.imgsteg;

public enum Channel {
    ALPHA,
    RED,
    GREEN,
    BLUE;

    int toInt() {
        switch (this) {
            case ALPHA -> {
                return 0;
            }
            case RED -> {
                return 1;
            }
            case GREEN -> {
                return 3;
            }
            case BLUE -> {
                return 4;
            }
        }
        return -1;
    }
}
