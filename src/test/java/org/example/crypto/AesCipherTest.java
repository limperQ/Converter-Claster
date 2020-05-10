package org.example.crypto;

import org.junit.Assert;
import org.junit.Test;

public class AesCipherTest {
    String secretKey = "BeGvWqgrVd42hfeH";
    String testStringForEncrypt = "TestStringForEncrypt";
    @Test
    public void testCrypto() {
        String encryptedStr = AesCipher.encrypt(secretKey, testStringForEncrypt);
        String decryptedStr = AesCipher.decrypt(secretKey, encryptedStr);

        Assert.assertEquals(decryptedStr, testStringForEncrypt);
    }
}