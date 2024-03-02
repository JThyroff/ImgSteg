import de.thyroff.imgsteg.ByteArrayWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ByteArrayWriterAndReaderTest {

    @Test
    public void testBytesIntoBlackImage() throws IOException {
        String path = TestUtils.getTestImgOriginalDir() + "/blankKey84.png";
        File original = new File(path);
        File testFile = TestUtils.copyFile(original);

        String bytesFilePath = TestUtils.getTestImgOriginalDir() + "/testMsg.txt";
        byte[] bytes = TestUtils.readFileToBytes(bytesFilePath);

        ByteArrayWriter.writeByteArrayIntoImage(bytes, testFile);
    }
}
