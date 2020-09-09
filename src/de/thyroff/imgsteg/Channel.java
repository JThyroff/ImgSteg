package de.thyroff.imgsteg;

public enum Channel {
    ALPHA,
    RED,
    GREEN,
    BLUE;

    public static Channel toChannel(int c) {
        return switch (c) {
            case 0 -> ALPHA;
            case 1 -> RED;
            case 2 -> GREEN;
            case 3 -> BLUE;
            default -> null;
        };
    }

    int toInt() {
        switch (this) {
            case ALPHA -> {
                return 0;
            }
            case RED -> {
                return 1;
            }
            case GREEN -> {
                return 2;
            }
            case BLUE -> {
                return 3;
            }
        }
        return -1;
    }
}
