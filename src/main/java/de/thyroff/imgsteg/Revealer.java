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
import java.util.List;

public class Revealer {
    /**
     * extracts the key from the key file to a List of Positions
     *
     * @param keyFile the key file
     * @return the position list
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

        //read list size first 33 bit
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

    private static byte getByte(BufferedImage image, MyPosition pos) {
        int rgb = image.getRGB(pos.getX(), pos.getY());
        int alpha = (rgb >> 24) & 0xff;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = rgb & 0xff;

        switch (pos.getChannel()) {

            case ALPHA -> {
                return (byte) (alpha + pos.getOffset());
            }
            case RED -> {
                return (byte) (red + pos.getOffset());
            }
            case GREEN -> {
                return (byte) (green + pos.getOffset());
            }
            case BLUE -> {
                return (byte) (blue + pos.getOffset());
            }
            default -> {
                return 0;
            }
        }
    }

    /**
     * reads the data from the given image at the given locations
     *
     * @param file        to read the data from
     * @param myPositions the position list
     * @return byte array of the data
     */
    public static byte[] reveal(File file, List<MyPosition> myPositions) {
        try {
            BufferedImage image = ImageIO.read(file);
            byte[] bytes = new byte[myPositions.size()];

            //myPositions.stream().map((position) -> getByte(image, new MyPosition(position.getX(), position.getY(),position.getChannel(),position.getOffset()))).toArray();

            for (int x = 0; x < myPositions.size(); x++) {
                MyPosition myPosition = myPositions.get(x);
                short x_coord = myPosition.getX();
                short y_coord = myPosition.getY();
                short offset = myPosition.getOffset();
                bytes[x] = getByte(image, new MyPosition(x_coord, y_coord, myPosition.getChannel(), offset));
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
