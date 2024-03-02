import java.io.*;

public class TestUtils {
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

    public static byte[] readFileToBytes(String filePath) throws IOException {
        // Create a FileInputStream object
        FileInputStream fileInputStream = new FileInputStream(filePath);

        // Determine the file size
        long fileSize = fileInputStream.available();

        // Create a byte array to hold the file data
        byte[] buffer = new byte[(int) fileSize];

        // Read bytes from the file into the buffer
        fileInputStream.read(buffer);

        // Close the FileInputStream
        fileInputStream.close();
        return buffer;
    }
}
