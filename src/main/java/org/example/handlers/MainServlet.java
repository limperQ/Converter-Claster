package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.example.crypto.AesCipher;
import org.example.model.Answer;
import org.example.utils.Common;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

@WebServlet("/convert")
public class MainServlet extends HttpServlet {
    static String secretKey;
    private static Logger log = LoggerFactory.getLogger(MainServlet.class.getSimpleName());

    public void init() {
        secretKey = PropertyManager.getPropertyAsString("secretKey", "BeGvWqgrVd42hfeH");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqStr = IOUtils.toString(req.getInputStream());
        String decryptedStr = AesCipher.decrypt(secretKey, reqStr);
        Boolean isEncrypted = decryptedStr != null;
        if (isEncrypted) {
            reqStr = decryptedStr;
        }

        Object respObj = convert(resp, reqStr);

        String convertedResponseStr = Common.getPrettyGson().toJson(respObj);
        if (isEncrypted){
            convertedResponseStr = AesCipher.encrypt(secretKey, Common.getPrettyGson().toJson(respObj));;
        }

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(convertedResponseStr);

    }

    private Object convert(HttpServletResponse resp, String xmlBody) {
        JAXBContext jc = null;
        Unmarshaller jaxbUnmarshaller = null;
        StringReader reader = new StringReader(xmlBody);
        Object respObj = null;
        try {
            if (xmlBody.contains("<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:admi.002.001.01\">")) {
                jc = JAXBContext.newInstance(iso.std.iso._20022.tech.xsd.admi_002_001.Document.class);
                jaxbUnmarshaller = jc.createUnmarshaller();
                respObj = (iso.std.iso._20022.tech.xsd.admi_002_001.Document) jaxbUnmarshaller.unmarshal(reader);
            } else if (xmlBody.contains("<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.028.001.03\">")) {
                jc = JAXBContext.newInstance(iso.std.iso._20022.tech.xsd.pacs_028_001.Document.class);
                jaxbUnmarshaller = jc.createUnmarshaller();
                respObj = (iso.std.iso._20022.tech.xsd.pacs_028_001.Document) jaxbUnmarshaller.unmarshal(reader);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (JAXBException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        return respObj;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        Answer answer = new Answer("OK", null);
        String str = Common.getPrettyGson().toJson(answer);
        response.getWriter().println(str);
    }
}
