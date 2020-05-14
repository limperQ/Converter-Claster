package org.example.crypto;

import org.junit.Assert;
import org.junit.Test;

public class AesCipherTest {
    String secretKey = "BeGvWqgrVd42hfeH";
    String testStringForEncrypt = "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:admi.002.001.01\">\n" +
            "    <admi.002.001.01>\n" +
            "        <RltdRef>\n" +
            "            <Ref>GUBMAG1911140022S</Ref>\n" +
            "        </RltdRef>\n" +
            "        <Rsn>\n" +
            "            <RjctgPtyRsn>EA107</RjctgPtyRsn>\n" +
            "            <RjctnDtTm>2019-11-14T18:35:31</RjctnDtTm>\n" +
            "            <RsnDesc>Incoming message was not recognized or document has got wrong structure</RsnDesc>\n" +
            "        </Rsn>\n" +
            "    </admi.002.001.01>\n" +
            "</Document>";
    @Test
    public void testCrypto() {
        String encryptedStr = AesCipher.encrypt(secretKey, testStringForEncrypt);
        String decryptedStr = AesCipher.decrypt(secretKey, encryptedStr);

        System.out.println(encryptedStr);
        Assert.assertEquals(decryptedStr, testStringForEncrypt);
    }
}