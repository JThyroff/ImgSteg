package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.ARGB;
import de.thyroff.imgsteg.utils.BitBuffer;
import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Hider {

    /**
     * @param image the image
     * @param c     the char to hide
     * @return the best location where the char can be hidden
     */
    private static MyPosition searchBestPos(BufferedImage image, char c) {
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
                if (alpha == c) {
                    m = new MyPosition(x, y, Channel.ALPHA, (short) 0);
                } else if (red == c) {
                    m = new MyPosition(x, y, Channel.RED, (short) 0);
                } else if (green == c) {
                    m = new MyPosition(x, y, Channel.GREEN, (short) 0);
                } else if (blue == c) {
                    m = new MyPosition(x, y, Channel.BLUE, (short) 0);
                }

                if (m != null) {
                    // suitable location found
                    return m;
                }

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// location is not suitable ////////////////////////
                ////////////////////////////////////////////////////////////////////////////////
                int alphaDiff = Math.abs(c - alpha);
                int redDiff = Math.abs(c - red);
                int greenDiff = Math.abs(c - green);
                int blueDiff = Math.abs(c - blue);

                Channel channel = Channel.BLUE;
                short localOffset = (short) (c - blue);
                if (alphaDiff <= redDiff && alphaDiff <= greenDiff && alphaDiff <= blueDiff) {
                    channel = Channel.ALPHA;
                    localOffset = (short) (c - alpha);
                } else if (redDiff <= alphaDiff && redDiff <= greenDiff && redDiff <= blueDiff) {
                    channel = Channel.RED;
                    localOffset = (short) (c - red);
                } else if (greenDiff <= alphaDiff && greenDiff <= redDiff && greenDiff <= blueDiff) {
                    channel = Channel.GREEN;
                    localOffset = (short) (c - green);
                }

                //this location is not suitable -> compare with best point
                if (bestPoint == null || Math.abs(localOffset) < Math.abs(bestPoint.getOffset())) { //overwrite best point
                    bestPoint = new MyPosition(x, y, channel, localOffset);
                }
            }
        }

        return bestPoint;
    }

    public static void hide(File file, File keyFile, File msg) {
        try {
            BufferedImage image = ImageIO.read(file);
            ArrayList<MyPosition> list = new ArrayList<>();
            FileInputStream msgIs = new FileInputStream(msg);

            byte bytes[] = new byte[(int) msg.length()];
            /*for (char c : msg.toCharArray()) {
                list.add(searchBestPos(image, c));
            }*/
            writeKeyIntoImage(keyFile, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param keyFile Path to the Image where the Positions shall be hidden
     * @param list    the list of the Positions
     */
    private static void writeKeyIntoImage(File keyFile, ArrayList<MyPosition> list) throws IOException {

        BufferedImage image = ImageIO.read(keyFile);

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int prod = width * height;

        final int size = list.size();

        //check boundaries
        if ((size * 50 + 32) > prod * 3) {
            throw new IllegalArgumentException("You can not save such a long position list in this small keyImage");
        }
        ////////////////////////////////////////////////////////////////////////////
        ///////   fill buffer    ///////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        BitBuffer bitBuffer = new BitBuffer();
        bitBuffer.add(size);
        bitBuffer.add(list);

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
    }
}
