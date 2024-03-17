package de.thyroff.imgsteg;

import de.thyroff.imgsteg.utils.BitBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ByteArrayReader {
    private static final Logger logger = Logger.getLogger(ByteArrayReader.class.getName());

    /**
     * Reads Byte Stream from the provided Image. Simply takes the lowest bits of RGB color channels without any other logic. Starting at 0 / 0.
     * @param image The image file into which the byte stream will be injected.
     * @throws IOException If an I/O error occurs while reading the image file.
     */
    public static byte[] readByteArrayFromImage(File image) throws IOException {
        logger.log(Level.FINER, "read byte stream from image");

        BufferedImage bufferedImage = ImageIO.read(image);

        final int width = bufferedImage.getWidth();

        int pixelIndex = 0;

        BitBuffer bitBuffer = new BitBuffer();

        //determine buffer length
        //read first 33 bit: length of buffer array
        for (int i = 0; i < 11; i++) {
            bitBuffer.add(bufferedImage, width, pixelIndex);
            pixelIndex++;
        }
        final int bufferSize = bitBuffer.removeInt(); //positions to be read - ie. size of the following buffer

        assert bitBuffer.size() == 1; //first bit of first byte already in the buffer

        //now read the rest of the bytes
        while (bitBuffer.size() < bufferSize * 8){
            bitBuffer.add(bufferedImage, width, pixelIndex);
            pixelIndex++;
        }
        logger.log(Level.FINER, "Byte stream read.");

        //convert bitBuffer to byte array
        return bitBuffer.removeByteArray(bufferSize);
    }
}
