package de.thyroff.imgsteg.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BitBuffer {

    private final ArrayList<Boolean> buffer = new ArrayList<>();

    public void add(boolean b) {
        buffer.add(b);
    }

    private void add(int toAdd, int bits) {
        for (int d = 0; d < bits; d++) {
            // works for positive and negative values
            // put true for a 1 and false for 0
            buffer.add((toAdd % 2) != 0);
            //shift to the right
            toAdd >>= 1;
        }
    }

    public void add(int toAdd) {
        add(toAdd, 32);
    }

    public void add(short toAdd) {
        add(toAdd, 16);
    }

    public void add(byte[] bytes){
        for(byte b : bytes){
            for (int i = 0; i < 8; i++){
                // write every single bit into the buffer
                // starting at lowest bits going to highest
                boolean bit = ((b >> i) & 1) == 1;
                buffer.add(bit);
            }
        }
    }

    /**
     * adds bits from one pixel to the buffer
     *
     * @param image      the image
     * @param width      the width
     * @param pixelIndex the pixel index
     */
    public void add(BufferedImage image, int width, int pixelIndex) {
        int argb = image.getRGB(pixelIndex % width, pixelIndex / width);

        buffer.add((ARGB.getRed(argb) % 2) == 1);
        buffer.add((ARGB.getGreen(argb) % 2) == 1);
        buffer.add((ARGB.getBlue(argb) % 2) == 1);
    }

    public void add(boolean[] booleanArray) {
        for (boolean b : booleanArray) {
            buffer.add(b);
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

    /**
     * @return the first 32 bit in the buffer as integer
     */
    public int removeInt() {
        if (this.size() < 32) {
            throw new RuntimeException("Buffer is not large enough to remove int");
        }
        int toReturn = 0;
        for (int i = 0; i < 32; i++) {
            if (this.removeFirst()) {
                toReturn += 1 << i;
            }
        }
        return toReturn;
    }

    /**
     * @return the first 16 bit in the buffer as short
     */
    public short removeShort() {
        if (this.size() < 16) {
            throw new RuntimeException("Buffer is not large enough to remove short");
        }
        short toReturn = 0;
        for (int i = 0; i < 16; i++) {
            if (this.removeFirst()) {
                toReturn += 1 << i;
            }
        }
        return toReturn;
    }
    /**
     * @return the first 8 bit in the buffer as byte
     */
    public byte removeByte() {
        if (this.size() < 8) {
            throw new RuntimeException("Buffer is not large enough to remove byte");
        }
        byte toReturn = 0;
        for (int i = 0; i < 8; i++) {
            if (this.removeFirst()) {
                toReturn += 1 << i;
            }
        }
        return toReturn;
    }

    /**
     * removes a boolean array from the buffer
     *
     * @param length the length of the array which shall be returned
     * @return the array
     */
    public boolean[] removeBooleanArray(int length) {
        if (this.size() < length) {
            throw new RuntimeException("Buffer is not large enough to remove " + length + " booleans");
        }
        boolean[] b = new boolean[length];
        for (int i = 0; i < length; i++) {
            b[i] = this.removeFirst();
        }
        return b;
    }

    /**
     * removes a byte array from the buffer
     *
     * @param length the length of the array which shall be returned
     * @return the array
     */
    public byte[] removeByteArray(int length) {
        if (this.size() < length) {
            throw new RuntimeException("Buffer is not large enough to remove " + length + " bytes");
        }
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = removeByte();
        }
        return b;
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
