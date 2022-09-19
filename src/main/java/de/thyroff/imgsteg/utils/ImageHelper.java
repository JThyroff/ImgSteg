package de.thyroff.imgsteg.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHelper {

    /**
     * Resize given Buffered Image to new Dimensions
     *
     * @param img  the image
     * @param newW the new width
     * @param newH the new height
     * @return the resized Buffered Image
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static ImageIcon getResizedImageIconFromPath(String path) {
        BufferedImage newPicture;
        try {
            newPicture = resize(ImageIO.read(new File(path)), 100, 100);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return new ImageIcon(newPicture);
    }
}
