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


}
