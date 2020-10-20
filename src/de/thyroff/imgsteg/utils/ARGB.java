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
}
