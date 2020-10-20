import de.thyroff.imgsteg.utils.Helper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelperTest {
    @Test
    public void test1() {
        boolean[] booleans = Helper.toBooleanArray(55);
        boolean[] expected = new boolean[32];
        for (int i = 0; i < 6; i++) {
            expected[i] = i != 3;
        }

        assertTrue(Arrays.equals(booleans, expected));
    }
}
