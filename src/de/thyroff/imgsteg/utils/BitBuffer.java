package de.thyroff.imgsteg.utils;

import java.util.ArrayList;

public class BitBuffer {

    private ArrayList<Boolean> buffer = new ArrayList<>();

    private void add(int toAdd, int bits) {
        for (int d = 0; d < bits; d++) {
            buffer.add((toAdd % 2) != 0);
            toAdd >>= 1;
        }
    }

    public void add(int toAdd) {
        add(toAdd, 32);
    }

    public void add(short toAdd) {
        add(toAdd, 16);
    }

    public void add(boolean[] booleanArray) {
        for (boolean b : booleanArray) {
            buffer.add(b);
        }
    }

    /**
     * 16 x
     * 16 y
     * 2 channel
     * 16 offset
     *
     * @param list the position list
     */
    public void add(ArrayList<MyPosition> list) {
        for (MyPosition pos : list) {
            this.add(pos.getX());
            this.add(pos.getY());
            this.add(pos.getChannel().toBoolean());
            this.add(pos.getOffset());
        }
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    public int size() {
        return buffer.size();
    }

    public boolean removeFirst() {
        return buffer.remove(0);
    }

    public ArrayList<Boolean> getBuffer() {
        return buffer;
    }

    public static boolean[] toBooleanArray(int i) {
        boolean[] b = new boolean[32];
        for (int d = 0; d < 32; d++) {
            b[d] = (i % 2 != 0);
            i >>= 1;
        }
        return b;
    }


}
