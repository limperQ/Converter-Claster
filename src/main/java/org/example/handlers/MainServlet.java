package org.example.handlers;

import com.google.gson.Gson;
import iso.std.iso._20022.tech.xsd.admi_002_001.Document;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.model.Answer;
import org.example.model.Item;
import org.example.model.Request;
import org.example.utils.Common;
import org.example.utils.TextReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;


public class MainServlet extends HttpServlet
{
    private static Logger log = LoggerFactory.getLogger(MainServlet.class.getSimpleName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String reqStr = IOUtils.toString(req.getInputStream());
        if(StringUtils.isBlank(reqStr))
        {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("BEEEE", null)));
            return;
        }
        resp.setContentType("text/xml");
        resp.setStatus(HttpServletResponse.SC_OK);
        try {
            Gson gson = new Gson();
            JAXBContext jc = JAXBContext.newInstance(iso.std.iso._20022.tech.xsd.admi_002_001.Document.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();

            StringReader reader = new StringReader(reqStr);
            iso.std.iso._20022.tech.xsd.admi_002_001.Document o = (Document) jaxbUnmarshaller.unmarshal(reader);

            resp.getWriter().println(Common.getPrettyGson().toJson(o));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        Answer answer = new Answer("OK", null);
        String str = Common.getPrettyGson().toJson(answer);
        log.error("Json:{}", str);
        response.getWriter().println(str);
    }


}
