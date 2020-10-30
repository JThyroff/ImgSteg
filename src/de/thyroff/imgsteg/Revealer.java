package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.ARGB;
import de.thyroff.imgsteg.utils.BitBuffer;
import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Revealer {
    /**
     * extracts the key from the key file to a List of Positions
     *
     * @param keyFile the key file
     * @return the position list
     * @throws IOException
     */
    public static ArrayList<MyPosition> extractKeyFromImage(File keyFile) throws IOException {
        BufferedImage image = ImageIO.read(keyFile);

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int prod = width * height;

        final String name = keyFile.getName();
        int pixelIndex = Math.abs(name.hashCode()) % prod;

        BitBuffer bitBuffer = new BitBuffer();

        ////////////////////////////////////////////////////////////////////////////
        //////     read key to buffer     //////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        //read size first 33 bit
        for (int i = 0; i < 11; i++) {
            addToBuffer(image, width, pixelIndex, bitBuffer);

            pixelIndex++;
            if (pixelIndex >= prod) {
                pixelIndex = 0;
            }
        }
        final int listSize = bitBuffer.removeInt(); //positions to be read

        assert bitBuffer.size() == 1; //first bit of first position already in the buffer

        ////////////////////////////////////////////////////////////////////////////
        //////     read positions    ///////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        while (bitBuffer.size() < listSize * 50) { // 50 bit per position. The smaller than sign is crucial because of bit stuffing
            addToBuffer(image, width, pixelIndex, bitBuffer);

            pixelIndex++;
            if (pixelIndex >= prod) {
                pixelIndex = 0;
            }
        }

        ////////////////////////////////////////////////////////////////////////////
        //////     read buffer to list     /////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        ArrayList<MyPosition> posList = new ArrayList<>();

        while (posList.size() < listSize) {
            //read 50 bit of the buffer and turn them into a position object
            short x = bitBuffer.removeShort();
            short y = bitBuffer.removeShort();
            boolean[] channel = bitBuffer.removeBooleanArray(2);
            short offset = bitBuffer.removeShort();

            posList.add(new MyPosition(x, y, Channel.toChannel(channel), offset));
        }
        assert bitBuffer.size() < 3;
        return posList;
    }

    /**
     * adds bits from one pixel to the buffer
     *
     * @param image      the image
     * @param width      the width
     * @param pixelIndex the pixel index
     * @param bitBuffer  the buffer
     */
    private static void addToBuffer(BufferedImage image, int width, int pixelIndex, BitBuffer bitBuffer) {
        int argb = image.getRGB(pixelIndex % width, pixelIndex / width);

        bitBuffer.add((ARGB.getRed(argb) % 2) == 1);
        bitBuffer.add((ARGB.getGreen(argb) % 2) == 1);
        bitBuffer.add((ARGB.getBlue(argb) % 2) == 1);
    }
}
