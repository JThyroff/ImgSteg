package de.thyroff.imgsteg.utils;

public class ARGB {
    public static int getRed(int argb) {
        return (argb >> 16) & 0xff;
    }

    public static int getBlue(int argb) {
        return argb & 0xff;
    }

    public static int getGreen(int argb) {
        return (argb >> 8) & 0xff;
    }

    public static int getAlpha(int argb) {
        return (argb >> 24) & 0xff;
    }

    public static int toARGB(int alpha, int red, int green, int blue) {
        int toReturn = 0;
        toReturn = (short) alpha << 24;
        toReturn += (short) red << 16;
        toReturn += (short) green << 8;
        toReturn += (short) blue;
        return toReturn;
    }

    public static int toARGB(int red, int green, int blue) {
        return toARGB(255, red, green, blue);
    }

    /**
     * injects three bits into the rgb channels
     *
     * @param argb the pixel
     * @param bits the bits
     * @return the new pixel
     */
    public static int inject(int argb, boolean[] bits) {
        if (bits.length != 3) {
            throw new IllegalArgumentException("Length has to be 3");
        }
        int mask = 0;
        // 0000_0000  0000_0000  0000_0000  0000_0000
        mask = 1 << 16; // red
        if (bits[0]) {
            argb = argb | mask;
        } else {
            mask = ~mask;
            argb = argb & mask;
        }

        mask = 1 << 8; // green
        if (bits[1]) {
            argb = argb | mask;
        } else {
            mask = ~mask;
            argb = argb & mask;
        }

        mask = 1; // blue
        if (bits[2]) {
            argb = argb | mask;
        } else {
            mask = ~mask;
            argb = argb & mask;
        }
        return argb;
    }

}
