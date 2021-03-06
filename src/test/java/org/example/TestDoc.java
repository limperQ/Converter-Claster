package org.example;

import com.google.gson.Gson;
import iso.std.iso._20022.tech.xsd.pacs_028_001.Document;
import org.example.utils.Common;
import org.example.utils.TextReader;
import org.junit.Test;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import java.io.*;

public class TestDoc {
    @Test
    public void testDoc() {
        try {
            Gson gson = new Gson();
            TextReader textReader = new TextReader();
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();

            String s = textReader.readTestInput("/pacs.028.001.03_status_request.xml");
            StringReader reader = new StringReader(s);
            iso.std.iso._20022.tech.xsd.pacs_028_001.Document o = (iso.std.iso._20022.tech.xsd.pacs_028_001.Document)jaxbUnmarshaller.unmarshal(reader);

            System.out.println(o.getFIToFIPmtStsReq().getGrpHdr());

            System.out.println(Common.getPrettyGson().toJson(o));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
