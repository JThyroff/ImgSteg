package de.thyroff.imgsteg.legacy;

import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class HiderLegacy {

    /**
     * @param image the image
     * @param c     the char to hide
     * @return the best location where the char can be hidden
     */
    private static MyPosition searchBestPos(BufferedImage image, char c) {
        MyPosition bestPoint = null;
        //iterates over all the pixels in the given image. If a suitable location is found the method returns.
        //if there is not a suitable position, the best suitable one is stored with an offset.
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

    /**
     * hides the message in the given file. Generates a key image that is needed to reveal the message again.
     * @param file the file where the message should be hidden in
     * @param msg the message that is hidden in the file
     */
    public static void hide(File file, String msg) {
        try {
            BufferedImage image = ImageIO.read(file);
            LinkedList<MyPosition> list = new LinkedList<>();
            for (char c : msg.toCharArray()) {
                list.add(searchBestPos(image, c));
            }
            writeImage(file.getParent(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a Key-Image of the Position list and stores it at the given path.
     * @param path Path to the location where the key will be stored
     * @param list the list of positions which will be stored in the key
     */
    private static void writeImage(String path, LinkedList<MyPosition> list) {
        BufferedImage bi = new BufferedImage(list.size(), 2, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < list.size(); x++) {
            MyPosition myPosition = list.get(x);
            bi.setRGB(x, 0, myPosition.getX() + (myPosition.getY() << 16));
            bi.setRGB(x, 1, myPosition.getChannel().toInt() + (myPosition.getOffset() << 16));
        }
        try {
            ImageIO.write(bi, "PNG", new File(path + "/key.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
