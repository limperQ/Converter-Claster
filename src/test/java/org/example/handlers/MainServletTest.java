package org.example.handlers;

import iso.std.iso._20022.tech.xsd.pacs_028_001.Document;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.Main;
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
        String url = "http://localhost:8090/convert";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        TextReader textReader = new TextReader();
        String sendString = textReader.readTestInput("/pacs.028.001.03_status_request.xml");

        JAXBContext jc = JAXBContext.newInstance(Document.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        StringReader reader = new StringReader(sendString);
        iso.std.iso._20022.tech.xsd.pacs_028_001.Document o = (iso.std.iso._20022.tech.xsd.pacs_028_001.Document)jaxbUnmarshaller.unmarshal(reader);
        String objectString = Common.getPrettyGson().toJson(o);

        StringEntity stringEntity = new StringEntity(sendString);
        request.setEntity(stringEntity);

        HttpResponse response = client.execute(request);
        HttpEntity resp = response.getEntity();
        String respStr = IOUtils.toString(resp.getContent());

        org.junit.Assert.assertEquals(respStr, objectString);
    }


    @After
    public void entTest()
    {
        Main.stopServer();
    }


}