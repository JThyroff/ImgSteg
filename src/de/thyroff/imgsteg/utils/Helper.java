package de.thyroff.imgsteg.utils;

public class Helper {
    public static boolean[] toBooleanArray(int i) {
        boolean[] b = new boolean[32];
        for (int d = 0; d < 32; d++) {
            b[d] = (i % 2 != 0);
            i >>= 1;
        }
        return b;
    }
}
