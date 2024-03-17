import de.thyroff.imgsteg.ByteArrayToHex;
import de.thyroff.imgsteg.DataEncryptor;
import de.thyroff.imgsteg.FileToByteArray;
import de.thyroff.imgsteg.ImageToSeed;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;

public class DataEncryptorTest {
    @Test
    public void testEncryptAndDecrypt() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String path = TestUtils.getTestImgOriginalDir() + "/testImg1.jpg";
        File original = new File(path);
        File testFile = TestUtils.copyFile(original);

        byte[] seed = ImageToSeed.imageToSeed(testFile.getPath());
        System.out.println("Seed: "+ByteArrayToHex.bytesToHex(seed));

        String dataPath = TestUtils.getTestImgOriginalDir() + "/testMsg.txt";
        File dataOriginal = new File(dataPath);
        File dataTestFile = TestUtils.copyFile(dataOriginal);
        byte[] dataBytes = FileToByteArray.readFileToByteArray(dataTestFile);
        System.out.println("Data: "+ ByteArrayToHex.bytesToHex(dataBytes));
        byte[] encrypted = DataEncryptor.encryptData(dataBytes, seed);
        System.out.println("Encrypted: "+ ByteArrayToHex.bytesToHex(encrypted));
        byte[] decrypted = DataEncryptor.decryptData(encrypted, seed);

        assertArrayEquals(dataBytes, decrypted);
    }


}
