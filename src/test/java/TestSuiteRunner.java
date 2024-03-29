import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ARGBTest.class, BitBufferTest.class, ByteArrayWriterAndReaderTest.class, DataEncryptorTest.class, ImageToSeedTest.class})

public class TestSuiteRunner {

    static final String USER_DIR = System.getProperty("user.dir");

    public static String testPath = "/src/test/java";

    @BeforeClass
    public static void printMe() {
        System.out.println("Starting Tests ...");
        System.out.println("From : " + USER_DIR + testPath);
    }
}
