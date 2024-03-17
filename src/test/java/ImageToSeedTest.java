import de.thyroff.imgsteg.ByteArrayToHex;
import de.thyroff.imgsteg.ImageToSeed;
import org.junit.Test;

import java.io.File;

public class ImageToSeedTest {
    @Test
    public void testImageToSeed1() {
        String path = TestUtils.getTestImgOriginalDir() + "/testImg1.jpg";
        String path1 = TestUtils.getTestImgOriginalDir() + "/blankKey84.png";
        String path2 = TestUtils.getTestImgOriginalDir() + "/blankKey84White.png";

        String[] paths = new String[]{path, path1, path2};
        for (String p : paths){
            File original = new File(p);
            File testFile = TestUtils.copyFile(original);

            byte[] seed = ImageToSeed.imageToSeed(testFile.getPath());
            System.out.println(ByteArrayToHex.bytesToHex(seed));
        }
    }

}
