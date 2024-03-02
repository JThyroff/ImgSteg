package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.ARGB;
import de.thyroff.imgsteg.utils.BitBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ByteStreamWriter {
    private static final Logger logger = Logger.getLogger(Hider.class.getName());


    /**
     * ! Overrides the provided image.
     * Injects arbitrary Byte Stream into the provided Image. Simple injection without any other logic. Starting at 0 / 0.
     * @param buffer The byte stream to be injected into the image.
     * @param image The image file into which the byte stream will be injected.
     * @throws IOException If an I/O error occurs while reading the image file.
     * @throws IllegalArgumentException If the buffer does not fit into the image.
     */
    public static void writeByteStreamIntoImage(byte[] buffer, File image) throws IOException {
        logger.log(Level.FINER, "write byte stream into image.");

        BufferedImage bufferedImage = ImageIO.read(image);

        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int pixelcount = width * height;

        /* do a check if the buffer fits the image
        * we only want to use the lowest bits per color channel and only the RGB channels (no alpha)
        * means we can fit 3 bits per pixel and a byte has 8 bit
         */
        //TODO upgrade to two lowest bits per pixel
        if (buffer.length * 8 > pixelcount * 3){
            throw new IllegalArgumentException("Buffer doesn't fit the image.");
        }


        //write byte array into bit buffer
        BitBuffer bitBuffer = new BitBuffer();
        //write buffer length in the beginning. 32 bit.
        bitBuffer.add(buffer.length);
        bitBuffer.add(buffer);

        ////////////////////////////////////////////////////////////////////////////
        //////     do injection     ////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        int argb, x, y;

        int pixelIndex = 0;

        while (!bitBuffer.isEmpty()) {
            boolean bit1 = bitBuffer.removeFirst();
            boolean bit2 = bitBuffer.removeFirst();
            boolean bit3 = bitBuffer.removeFirst();

            x = pixelIndex % width;
            y = pixelIndex / width;

            argb = bufferedImage.getRGB(x, y);
            argb = ARGB.inject3(argb, bit1, bit2, bit3);
            bufferedImage.setRGB(x, y, argb);

            pixelIndex++;
            assert pixelIndex <= pixelcount;
        }
        //overwrite the image file
        ImageIO.write(bufferedImage, "png", image);
    }
}
