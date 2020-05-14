package org.example.crypto;

import org.apache.commons.codec.binary.Hex;
import org.example.handlers.TransportServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

public class AesCipher {
    private static final String INIT_VECTOR = "rsTewmRhfLseswWe";
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());

    public static String encrypt(String secretKey, String plainText) {
        try {
            if(!isKeyLengthValid(secretKey)) {
                throw new Exception(("Secret key`s length must be 128, 192 or 256 bits."));
            }

            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            return new String(Hex.encodeHex(cipher.doFinal(plainText.getBytes("UTF-8")), false));
        } catch (Throwable ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public static String decrypt(String secretKey, String cipherText) {
        try {
            if(!isKeyLengthValid(secretKey)) {
                throw new Exception(("Secret key`s length must be 128, 192 or 256 bits."));
            }

            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            return new String(cipher.doFinal(Hex.decodeHex(cipherText.toCharArray())));
        } catch (Throwable ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    private static boolean isKeyLengthValid(String secretKey) {
        return secretKey.length() == 16 || secretKey.length() == 24 || secretKey.length() == 32;
    }
}