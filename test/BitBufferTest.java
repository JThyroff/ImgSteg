import de.thyroff.imgsteg.utils.BitBuffer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitBufferTest {
    @Test
    public void test1() {
        boolean[] booleans = BitBuffer.toBooleanArray(55);
        boolean[] expected = new boolean[32];
        for (int i = 0; i < 6; i++) {
            expected[i] = i != 3;
        }

        assertArrayEquals(booleans, expected);
    }

    @Test
    public void test2() {
        BitBuffer bitBuffer = new BitBuffer();
        bitBuffer.add(5);

        ArrayList<Boolean> expected = new ArrayList<>();

        for (int i = 0; i < 32; i++) {
            if (i == 0 || i == 2) {
                expected.add(true);
            } else {
                expected.add(false);
            }
        }

        assertEquals(expected, bitBuffer.getBuffer());
    }

    @Test
    public void test3() {
        BitBuffer bitBuffer = new BitBuffer();
        bitBuffer.add((short) 5);

        ArrayList<Boolean> expected = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 2) {
                expected.add(true);
            } else {
                expected.add(false);
            }
        }

        assertEquals(expected, bitBuffer.getBuffer());
    }

    @Test
    public void removeInt() {
        BitBuffer bitBuffer = new BitBuffer();
        int expected = 48_89_89_05_0;
        bitBuffer.add(expected);
        bitBuffer.add(343434);

        assertEquals(expected, bitBuffer.removeInt());
    }
}
