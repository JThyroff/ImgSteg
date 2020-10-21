package de.thyroff.imgsteg.utils;

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

    public int toInt() {
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

    public boolean[] toBoolean() {
        boolean[] b = new boolean[2];
        switch (this) {
            case ALPHA -> {

            }
            case RED -> {
                b[1] = true;
            }
            case GREEN -> {
                b[0] = true;
            }
            case BLUE -> {
                b[0] = true;
                b[1] = true;
            }
        }
        return b;
    }
}
