package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.ARGB;
import de.thyroff.imgsteg.utils.BitBuffer;
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

        int argb = -1;
        int x = -1;
        int y = -1;

        ////////////////////////////////////////////////////////////////////////////
        //////     read key to buffer     //////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        int red = -1;
        int green = -1;
        int blue = -1;
        //read size 33 bit
        for (int i = 0; i < 11; i++) {
            x = pixelIndex % width;
            y = pixelIndex / width;

            argb = image.getRGB(x, y);
            red = ARGB.getRed(argb);
            green = ARGB.getGreen(argb);
            blue = ARGB.getBlue(argb);

            bitBuffer.add((red % 2) == 1);
            bitBuffer.add((green % 2) == 1);
            bitBuffer.add((blue % 2) == 1);
        }

        
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

        return null;
    }
}
