package de.thyroff.imgsteg.utils;

import java.security.InvalidParameterException;

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

    public static Channel toChannel(boolean[] b) {
        if (b.length != 2) {
            throw new InvalidParameterException("Array has to have length 2");
        }
        if (!b[0] && !b[1]) {
            return ALPHA;
        } else if (!b[0] && b[1]) {
            return RED;
        } else if (!b[1]) {
            return GREEN;
        } else {
            return BLUE;
        }
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
