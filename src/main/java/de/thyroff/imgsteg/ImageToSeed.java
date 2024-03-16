package de.thyroff.imgsteg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageToSeed {

    public static void main(String[] args) {
        String imagePath = "example_image.jpg";
        long seed = imageToSeed(imagePath);
        System.out.println("Random seed generated from the image: " + seed);

        // Now you can use the seed to initialize a random number generator
        // For example:
        // Random random = new Random(seed);
    }

    public static long imageToSeed(String imagePath) {
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
        return 0; // Error occurred, return default seed value
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

    private static long hashByteArray(byte[] data) {
        try {
            // Calculate SHA-256 hash of the byte array
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);

            // Convert hash bytes to long value
            long hashValue = 0;
            for (int i = 0; i < 8; i++) {
                hashValue <<= 8;
                hashValue |= (hash[i] & 0xFF);
            }
            return hashValue;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0; // Error occurred, return default hash value
    }
}

