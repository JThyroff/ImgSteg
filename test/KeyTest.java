import de.thyroff.imgsteg.Hider;
import de.thyroff.imgsteg.Revealer;
import de.thyroff.imgsteg.utils.Channel;
import de.thyroff.imgsteg.utils.MyPosition;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class KeyTest {

    public void keyTestImg(String path) {
        ArrayList<MyPosition> expected = new ArrayList<>();
        expected.add(new MyPosition((short) 1, (short) 2, Channel.RED, (short) 4));
        expected.add(new MyPosition((short) 43, (short) 3, Channel.BLUE, (short) -2));

        File original = new File(path);

        copyFile(original);
        File testFile = new File("copy_" + path);

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

    @Test
    public void keyTest1() {
        keyTestImg("test/testImg1.jpg");
    }

    @Test
    public void keyTestBlack() {
        keyTestImg("test/blankKey84.png");
    }

    @Test
    public void keyTestWhite() {
        keyTestImg("test/blankKey84White.png");
    }

    public static void copyFile(File original) {
        File copied = new File(original.getParentFile().getPath() + "/" + "copy_" + original.getName());
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(original));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(copied))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


