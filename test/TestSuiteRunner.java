import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ARGBTest.class, BitBufferTest.class, HiderAndRevealerTest.class})

public class TestSuiteRunner {

    @BeforeClass
    public static void printMe() {
        System.out.println("Starting Tests");
    }
}
