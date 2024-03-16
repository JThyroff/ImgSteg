package de.thyroff.imgsteg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageToSeed {

    public static byte[] imageToSeed(String imagePath) {
        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Convert the image to grayscale and resize if needed
            int width = 100; // Set desired width
            int height = 100; // Set desired height
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            resizedImage.getGraphics().drawImage(image, 0, 0, width, height, null);

            // Calculate the hash of the image data
            byte[] imageData = toByteArray(resizedImage);

            return hashByteArray(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Error occurred, return default seed value
    }

    private static byte[] toByteArray(BufferedImage image) throws IOException {
        // Convert BufferedImage to byte array
        // You may need to modify this based on your image format and requirements
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos); // You can specify the image format here
        baos.flush();
        byte[] imageData = baos.toByteArray();
        baos.close();
        return imageData;
    }

    private static byte[] hashByteArray(byte[] data) {
        try {
            // Calculate SHA-256 hash of the byte array
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);

            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null; // Error occurred, return default hash value
    }
}

