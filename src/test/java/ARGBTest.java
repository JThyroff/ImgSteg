import de.thyroff.imgsteg.utils.ARGB;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ARGBTest {
    @Test
    public void test1() {

        int testPix = 0xff_f4_f2_f7;
        boolean[] bits = new boolean[]{true, true, false};

        int injected = ARGB.inject3(testPix, bits);
        int expected = 0xff_f5_f3_f6;

        assertEquals(expected, injected);
    }

    @Test
    public void test2() {

        int testPix = 0xff_f6_24_67;
        boolean[] bits = new boolean[]{false, false, true};

        int injected = ARGB.inject3(testPix, bits);
        int expected = 0xff_f6_24_67;

        assertEquals(expected, injected);
    }

    @Test
    public void test3() {

        int testPix = 0xff_ff_ff_ff;
        boolean[] bits = new boolean[]{false, false, false};

        int injected = ARGB.inject3(testPix, bits);
        int expected = 0xff_fe_fe_fe;

        assertEquals(expected, injected);
    }
}
