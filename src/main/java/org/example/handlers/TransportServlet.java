package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.model.Answer;
import org.example.model.Item;
import org.example.utils.Common;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.model.Request;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/")
public class TransportServlet extends HttpServlet {
    private static int requestCounter = 0;
    private static int serverCounter = 3;
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqStr = IOUtils.toString(req.getInputStream());

        if (StringUtils.isBlank(reqStr)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("BEEEE", null)));
            return;
        }

        String redirectingPath = "http://localhost:";
        switch (requestCounter % serverCounter) {
            case (0):
                redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8029).toString();
                break;
            case (1):
                redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8027).toString();
                break;
            case (2):
                redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8028).toString();
                break;
        }
        redirectingPath += "/convert";
        ++requestCounter;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(redirectingPath);
        StringEntity entity = new StringEntity(reqStr);

        request.setEntity(entity);
        HttpResponse response = client.execute(request);

        HttpEntity convertedResponse = response.getEntity();
        String convertedResponseStr = IOUtils.toString(convertedResponse.getContent());

        resp.setContentType("application/xml");
        resp.setStatus(HttpServletResponse.SC_OK);
        List<Item> items = Arrays.asList(new Item("First", convertedResponseStr));
        resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("OK", items)));


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectingPath = "http://localhost:";
        switch(requestCounter % serverCounter) {
            case (0): redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8026).toString(); break;
            case (1): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8027).toString(); break;
            case (2): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8028).toString(); break;
        }
        redirectingPath += "/convert";
        resp.sendRedirect(redirectingPath);
        ++requestCounter;
    }
}
