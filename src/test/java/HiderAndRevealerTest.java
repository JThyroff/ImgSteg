import de.thyroff.imgsteg.Hider;
import de.thyroff.imgsteg.Revealer;
import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HiderAndRevealerTest {
    private static final String TEST_IMG_DIR = TestSuiteRunner.USER_DIR + "/src/test/resources/images/";
    private static final String ORIGINAL_DIR = "original";
    private static final String COPY_DIR = "copy";

    private static final String OUT_DIR = "out";

    public static String getTestImgOriginalDir() {
        return TEST_IMG_DIR + "/" + ORIGINAL_DIR;
    }

    public static String getTestImgCopyDir() {
        return TEST_IMG_DIR + "/" + COPY_DIR;
    }

    public static String getOutDir() {
        return TEST_IMG_DIR + "/" + OUT_DIR;
    }

    /**
     * Writes three positions into a test file and extracts them again. The results should match.
     *
     * @param path Path to the original image file
     */
    public void test_writeKeyIntoImage(String path) {
        ArrayList<MyPosition> expected = new ArrayList<>();
        expected.add(new MyPosition((short) 1, (short) 2, Channel.RED, (short) 4));
        expected.add(new MyPosition((short) 43, (short) 3, Channel.BLUE, (short) -2));
        expected.add(new MyPosition((short) 29, (short) 0, Channel.GREEN, (short) 9));

        File original = new File(path);

        File testFile = copyFile(original);

        try {
            Hider.writeKeyIntoImage(testFile, expected);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        try {
            ArrayList<MyPosition> myPositions = Revealer.extractKeyFromImage(testFile);
            assertEquals(expected, myPositions);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * @param path Path to the original image file
     */
    public void test_writeKeyIntoImageRnd(String path) {
        ArrayList<MyPosition> expected = new ArrayList<>();

        File original = new File(path);

        File testFile = copyFile(original);
        try {
            BufferedImage image = ImageIO.read(testFile);
            int width = image.getWidth();
            int height = image.getHeight();
            Random random = new Random();
            int entries = random.nextInt(Math.max((int) ((width * height) * 0.01), 5));
            entries = Math.max(entries, 3); //minimum three entries
            for (int i = 0; i < entries; i++) {
                expected.add(new MyPosition((short) random.nextInt(height), (short) random.nextInt(width), Channel.toChannel(random.nextInt(4)), (short) random.nextInt()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Hider.writeKeyIntoImage(testFile, expected);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        try {
            ArrayList<MyPosition> myPositions = Revealer.extractKeyFromImage(testFile);
            assertEquals(expected, myPositions);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests key injection for a big image.
     */
    @Test
    public void keyTest1() {
        String path = getTestImgOriginalDir() + "/testImg1.jpg";
        test_writeKeyIntoImage(path);
        test_writeKeyIntoImageRnd(path);
    }

    @Test
    public void keyTestBlack() {
        String path = getTestImgOriginalDir() + "/blankKey84.png";
        test_writeKeyIntoImage(path);
        test_writeKeyIntoImageRnd(path);
    }

    @Test
    public void keyTestWhite() {
        String path = getTestImgOriginalDir() + "/blankKey84White.png";
        test_writeKeyIntoImage(path);
        test_writeKeyIntoImageRnd(path);
    }

    /**
     * Hides and reveals the test messages and checks for same content. Full run test.
     */
    @Test
    public void test_hide() {
        String pathFile = getTestImgOriginalDir() + "/blankKey84.png";
        String pathKey = getTestImgOriginalDir() + "/blankKey84White.png";
        Path pathMsg = Path.of(getTestImgOriginalDir() + "/testMsg.txt");

        File copyFile = copyFile(new File(pathFile));
        File copyKey = copyFile(new File(pathKey));

        Hider.hide(copyFile, copyKey, pathMsg);
        try {
            ArrayList<MyPosition> keyFromImage = Revealer.extractKeyFromImage(copyKey);
            byte[] revealedData = Revealer.reveal(copyFile, keyFromImage);

            try (FileOutputStream fos = new FileOutputStream(getOutDir() + "/outMsg")) {
                fos.write(revealedData);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            //check for same content
            if (Files.mismatch(Path.of(getOutDir() + "/outMsg"), pathMsg) != -1) {
                fail();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File copyFile(File original) {
        File copied = new File(getTestImgCopyDir() + "/copy_" + original.getName());
        try (InputStream in = new BufferedInputStream(new FileInputStream(original)); OutputStream out = new BufferedOutputStream(new FileOutputStream(copied))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copied;
    }

}


