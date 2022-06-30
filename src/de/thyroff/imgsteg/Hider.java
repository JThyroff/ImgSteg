package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.ARGB;
import de.thyroff.imgsteg.utils.BitBuffer;
import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Hider {

    /**
     * @param image the image
     * @param b     the char to hide
     * @return the best location where the char can be hidden
     */
    private static MyPosition searchBestPos(BufferedImage image, byte b) {
        MyPosition bestPoint = null;
        for (short x = 0; x < image.getWidth(); x++) {
            for (short y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// test whether location is suitable ///////////////
                ////////////////////////////////////////////////////////////////////////////////

                MyPosition m = null;
                if (alpha == b) {
                    m = new MyPosition(x, y, Channel.ALPHA, (short) 0);
                } else if (red == b) {
                    m = new MyPosition(x, y, Channel.RED, (short) 0);
                } else if (green == b) {
                    m = new MyPosition(x, y, Channel.GREEN, (short) 0);
                } else if (blue == b) {
                    m = new MyPosition(x, y, Channel.BLUE, (short) 0);
                }

                if (m != null) {
                    // suitable location found
                    return m;
                }

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// location is not suitable ////////////////////////
                ////////////////////////////////////////////////////////////////////////////////
                int alphaDiff = Math.abs(b - alpha);
                int redDiff = Math.abs(b - red);
                int greenDiff = Math.abs(b - green);
                int blueDiff = Math.abs(b - blue);

                Channel channel = Channel.BLUE;
                short localOffset = (short) (b - blue);
                if (alphaDiff <= redDiff && alphaDiff <= greenDiff && alphaDiff <= blueDiff) {
                    channel = Channel.ALPHA;
                    localOffset = (short) (b - alpha);
                } else if (redDiff <= alphaDiff && redDiff <= greenDiff && redDiff <= blueDiff) {
                    channel = Channel.RED;
                    localOffset = (short) (b - red);
                } else if (greenDiff <= alphaDiff && greenDiff <= redDiff && greenDiff <= blueDiff) {
                    channel = Channel.GREEN;
                    localOffset = (short) (b - green);
                }

                //this location is not suitable -> compare with best point
                if (bestPoint == null || Math.abs(localOffset) < Math.abs(bestPoint.getOffset())) { //overwrite best point
                    bestPoint = new MyPosition(x, y, channel, localOffset);
                }
            }
        }

        return bestPoint;
    }

    public static void hide(File file, File keyFile, Path msgPath) {
        try {
            BufferedImage image = ImageIO.read(file);
            ArrayList<MyPosition> list = new ArrayList<>();

            //iterate over the msgBytes in the File
            byte[] msgBytes = Files.readAllBytes(msgPath);
            System.out.println("");
            for (byte b : msgBytes) {
                list.add(searchBestPos(image, b));
            }
            writeKeyIntoImage(keyFile, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param keyFile Path to the Image where the Positions shall be hidden
     * @param list    the list of the Positions
     */
    public static void writeKeyIntoImage(File keyFile, ArrayList<MyPosition> list) throws IOException {

        BufferedImage image = ImageIO.read(keyFile);

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int prod = width * height;

        final int size = list.size();  // 32 bit integer

        //check boundaries whether the given key can be stored in the image
        int msgBitLength = size * MyPosition.BIT_COUNT + 32;
        if (msgBitLength > prod * 3) { //size times bit count of MyPosition
            throw new IllegalArgumentException(
                    "You can not save such a long position list in this small keyImage. "
                            + "\nMessage Bit Length: " + msgBitLength
                            + "\nAllowed Length: " + prod * 3
            );
        } else {
            System.out.println(
                    "Msg length okay."
                            + "\nMessage Bit Length: " + msgBitLength
                            + "\nAllowed Length: " + prod * 3
            );
        }
        ////////////////////////////////////////////////////////////////////////////
        ///////   fill buffer    ///////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        BitBuffer bitBuffer = new BitBuffer();
        bitBuffer.add(size);
        addToBuffer(list, bitBuffer);

        //do some bit stuffing
        if (bitBuffer.size() % 3 != 0) {
            bitBuffer.add(new boolean[3 - bitBuffer.size() % 3]);
        }

        ////////////////////////////////////////////////////////////////////////////
        ///////    buffer filled     ///////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        final String name = keyFile.getName();
        int pixelIndex = Math.abs(name.hashCode()) % prod;

        int argb = -1;
        int x = -1;
        int y = -1;

        assert bitBuffer.size() % 3 == 0;

        ////////////////////////////////////////////////////////////////////////////
        //////     do injection     ////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        while (!bitBuffer.isEmpty()) {
            boolean bit1 = bitBuffer.removeFirst();
            boolean bit2 = bitBuffer.removeFirst();
            boolean bit3 = bitBuffer.removeFirst();

            x = pixelIndex % width;
            y = pixelIndex / width;

            argb = image.getRGB(x, y);
            argb = ARGB.inject(argb, bit1, bit2, bit3);
            image.setRGB(x, y, argb);

            pixelIndex++;
            if (pixelIndex >= prod) {
                pixelIndex = 0;
            }

        }
        ImageIO.write(image, "png", keyFile);
    }

    /**
     * 50 bit per position
     * 16 x
     * 16 y
     * 2 channel
     * 16 offset
     *
     * @param list the position list
     */
    private static void addToBuffer(ArrayList<MyPosition> list, BitBuffer buffer) {
        for (MyPosition pos : list) {
            buffer.add(pos.getX());
            buffer.add(pos.getY());
            buffer.add(pos.getChannel().toBoolean());
            buffer.add(pos.getOffset());
        }
    }
}
