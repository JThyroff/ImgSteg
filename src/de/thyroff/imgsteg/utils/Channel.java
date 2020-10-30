package de.thyroff.imgsteg.utils;

import java.security.InvalidParameterException;

public enum Channel {
    ALPHA(0),
    RED(1),
    GREEN(2),
    BLUE(3);

    private int channelInt;

    Channel(int channelInt) {
        this.channelInt = channelInt;
    }

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
        } else if (!b[0]) {
            return RED;
        } else if (!b[1]) {
            return GREEN;
        } else {
            return BLUE;
        }
    }

    public int toInt() {
        return channelInt;
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
