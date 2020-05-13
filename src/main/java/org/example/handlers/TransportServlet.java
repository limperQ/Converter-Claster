package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.balancer.Balancer;
import org.example.balancer.LeastConnectionsBalancer;
import org.example.balancer.RoundRobinBalancer;
import org.example.balancer.WeightedRoundRobinBalancer;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet("/")
public class TransportServlet extends HttpServlet {
    private static String transportPath;
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());
    private String balanceMethod;
    private String redirectingPath;
    private static int tryCounter;
    private static int serverCounter;
    private Balancer balancer;
    static String secretKey;

    @Override
    public void init(){
        balanceMethod = PropertyManager.getPropertyAsString("balanceMethod", "RoundRobin");
        serverCounter = PropertyManager.getPropertyAsString("addresses", "localhost:8888").split(";").length;
        tryCounter = 0;
        secretKey = PropertyManager.getPropertyAsString("secretKey", "BeGvWqgrVd42hfeH");

        switch (balanceMethod){
            case "RoundRobin": balancer = new RoundRobinBalancer(); break;
            case "LeastConnections": balancer = new LeastConnectionsBalancer(); break;
            case "WeightedRoundRobin": balancer = new WeightedRoundRobinBalancer(); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        transportPath = req.getRequestURL().toString();
        String reqStr = IOUtils.toString(req.getInputStream());

        if (StringUtils.isBlank(reqStr)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("Empty body", null)));
            return;
        }

        redirectingPath = balancer.getServerUrl();
        HttpResponse response = sendRequest(reqStr);

        if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("Bad request", null)));
            log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: Bad request");
        }

        HttpEntity convertedResponse = response.getEntity();
        String responseStr = IOUtils.toString(convertedResponse.getContent());

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(responseStr);

        log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: OK");
        if (PropertyManager.getPropertyAsString("balanceMethod", "LeastConnections").equals("LeastConnections")){
            balancer.decrementRequestCounter();
        }
    }

    public HttpResponse sendRequest(String reqStr){
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

                String str = request.getURI().toString();
                if (PropertyManager.getPropertyAsBoolean("onlyTransport", false) &&
                        (transportPath + "convert").equals(request.getURI().toString())) {
                    isFault = prepateToRetry();
                } else {
                    response = client.execute(request);
                    balancer.incrementRequestCounter();
                    isFault = false;
                    tryCounter = 0;
                }
            } catch (HttpHostConnectException e) {
                isFault = prepateToRetry();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (isFault && tryCounter < serverCounter);
        return response;
    }
    boolean prepateToRetry(){
        redirectingPath = changeServer();
        ++tryCounter;
        return true;
    }

    public String changeServer() {
        balancer.incrementRequestCounter();
        return balancer.getServerUrl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getRequestURL().toString() + "convert");
    }

}
