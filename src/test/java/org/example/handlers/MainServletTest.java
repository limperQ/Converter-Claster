package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.Main;
import org.example.crypto.AesCipher;
import org.example.utils.Common;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.utils.TextReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class MainServletTest
{
    @Before
    public void init()
    {
        Main.runServer(8090, "/");
    }

    @Test
    public void doGet() throws Exception
    {
        String url = "http://localhost:8090/path";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        org.junit.Assert.assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void doPost() throws Exception
    {
        String url = "http://localhost:8090/path";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        JAXBContext jc = JAXBContext.newInstance(iso.std.iso._20022.tech.xsd.pacs_028_001.Document.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        String s = new TextReader().readTestInput("/pacs.028.001.03_status_request.xml");

        String secretKey = "BeGvWqgrVd42hfeH";
        String encryptedText = AesCipher.encrypt(secretKey, s);
        System.out.println(encryptedText);

        String decryptedStr = AesCipher.decrypt(secretKey, encryptedText);
        System.out.println(decryptedStr);

        StringReader reader = new StringReader(decryptedStr);
        iso.std.iso._20022.tech.xsd.pacs_028_001.Document testObject = (iso.std.iso._20022.tech.xsd.pacs_028_001.Document) jaxbUnmarshaller.unmarshal(reader);
        String str = Common.getPrettyGson().toJson(testObject);
        StringEntity entity = new StringEntity(s);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        HttpEntity resp = response.getEntity();
        String respStr = IOUtils.toString(resp.getContent());

        org.junit.Assert.assertEquals(respStr, str);
    }


    @After
    public void entTest()
    {
        Main.stopServer();
    }


}