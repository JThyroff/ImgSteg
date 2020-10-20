package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Revealer {

    /**
     * returns the encoded char at the given position in the image
     *
     * @param image image
     * @param pos   position
     * @return the char
     */
    private static char getChar(BufferedImage image, MyPosition pos) {
        int rgb = image.getRGB(pos.getX(), pos.getY());
        int alpha = (rgb >> 24) & 0xff;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = rgb & 0xff;

        switch (pos.getChannel()) {

            case ALPHA -> {
                return (char) (alpha + pos.getOffset());
            }
            case RED -> {
                return (char) (red + pos.getOffset());
            }
            case GREEN -> {
                return (char) (green + pos.getOffset());
            }
            case BLUE -> {
                return (char) (blue + pos.getOffset());
            }
            default -> {
                return ' ';
            }
        }
    }

    /**
     * reveals the message in the File with the key
     *
     * @param selectedFile info file
     * @param key          key file
     * @return the Message
     */
    public static String reveal(File selectedFile, File key) {
        try {
            BufferedImage image = ImageIO.read(selectedFile);
            BufferedImage keyImg = ImageIO.read(key);

            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < keyImg.getWidth(); x++) {
                int posYX = keyImg.getRGB(x, 0);
                int offsetChannel = keyImg.getRGB(x, 1);

                short x_coord = (short) (posYX);
                short y_coord = (short) (posYX >> 16);
                short offset = (short) (offsetChannel >> 16);
                sb.append(getChar(image, new MyPosition(x_coord, y_coord, Channel.toChannel((short) offsetChannel), offset)));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
