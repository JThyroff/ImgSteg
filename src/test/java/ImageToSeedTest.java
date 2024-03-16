import de.thyroff.imgsteg.ImageToSeed;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ImageToSeedTest {
    @Test
    public void testImageToSeed1() throws IOException {
        String path = TestUtils.getTestImgOriginalDir() + "/testImg1.jpg";
        String path1 = TestUtils.getTestImgOriginalDir() + "/blankKey84.png";
        String path2 = TestUtils.getTestImgOriginalDir() + "/blankKey84White.png";

        String[] paths = new String[]{path, path1, path2};
        for (String p : paths){
            File original = new File(p);
            File testFile = TestUtils.copyFile(original);

            long seed = ImageToSeed.imageToSeed(testFile.getPath());
            System.out.println((seed));
        }
    }

}
