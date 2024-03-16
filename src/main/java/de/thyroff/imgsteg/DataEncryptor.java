package de.thyroff.imgsteg;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class DataEncryptor {

    public static byte[] encryptData(byte[] data, byte[] seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create AES key from seed bytes
        SecretKeySpec secretKey = new SecretKeySpec(seed, "AES");

        // Initialize AES cipher for encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    public static byte[] decryptData(byte[] encryptedData, byte[] seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create AES key from seed bytes
        SecretKeySpec secretKey = new SecretKeySpec(seed, "AES");

        // Initialize AES cipher for decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(encryptedData);
    }
}

