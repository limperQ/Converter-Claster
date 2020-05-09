package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.model.Answer;
import org.example.model.ConvertResponse;
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
import java.io.IOException;

@WebServlet("/")
public class TransportServlet extends HttpServlet {
    private static int tryCounter = 0;
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

        String redirectingPath = getServerUrl();

        HttpResponse response = null;
        HttpClient client;
        HttpPost request;
        StringEntity entity;

        boolean isFault = false;
        do {
            try {
                client = HttpClientBuilder.create().build();
                request = new HttpPost(redirectingPath);
                entity = new StringEntity(reqStr);
                request.setEntity(entity);

                response = client.execute(request);
                isFault = false;
                tryCounter = 0;
            } catch (HttpHostConnectException e) {
                redirectingPath = changeServer();
                isFault = true;
                ++tryCounter;
            }
        } while (isFault && tryCounter < serverCounter);

        if(response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("Bad request", null)));
            log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: Bad request");
        }

        HttpEntity convertedResponse = response.getEntity();
        String convertedResponseStr = IOUtils.toString(convertedResponse.getContent());

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(convertedResponseStr);
        log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: OK");
        ++requestCounter;
    }

    public String changeServer() {
        ++requestCounter;
        return getServerUrl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getRequestURL().toString() + "/convert");
        if (resp.getStatus() == HttpServletResponse.SC_OK) {
            log.error("HttpMethod: GET. Server: " + req.getRequestURL().toString() + ". Status: OK");
        } else {
            log.error("HttpMethod: GET. Server: " + req.getRequestURL().toString() + ". Status: Bad request");
        }
    }

    public String getServerUrl() {
        String redirectingPath = "http://localhost:";
        switch(requestCounter % serverCounter) {
            case (0): redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8027).toString(); break;
            case (1): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8028).toString(); break;
            case (2): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8029).toString(); break;
        }
        redirectingPath += "/convert";
        return redirectingPath;
    }
}
