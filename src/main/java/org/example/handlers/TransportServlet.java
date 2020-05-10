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
import org.example.balancer.IBalancer;
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
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());
    private String balanceMethod;
    private static int tryCounter;
    private static int serverCounter;
    private IBalancer balancer;
    static final String secretKey = "BeGvWqgrVd42hfeH";

    @Override
    public void init(){
        balanceMethod = PropertyManager.getPropertyAsString("balanceMethod", null);
        serverCounter = PropertyManager.getPropertyAsString("addresses", null).split(";").length;
        tryCounter = 0;

        switch (balanceMethod){
            case "RoundRobin": balancer = new RoundRobinBalancer(); break;
            case "LeastConnections": balancer = new LeastConnectionsBalancer(); break;
            case "WeightedRoundRobin": balancer = new WeightedRoundRobinBalancer(); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqStr = IOUtils.toString(req.getInputStream());
        String decryptedStr = AesCipher.decrypt(secretKey, reqStr);
        Boolean isEncrypted = decryptedStr != null;
        if (isEncrypted){
            reqStr = decryptedStr;
        }

        if (StringUtils.isBlank(reqStr)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("Empty body", null)));
            return;
        }
        String str = req.getRemoteUser();
        String str1 = req.getRemoteAddr();
        String str2 = req.getRemoteHost();
        int str3 = req.getRemotePort();

        String redirectingPath = balancer.getServerUrl();

        HttpResponse response = sendRequest(redirectingPath, reqStr);

        if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("Bad request", null)));
            log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: Bad request");
        }

        HttpEntity convertedResponse = response.getEntity();
        String convertedResponseStr = IOUtils.toString(convertedResponse.getContent());
        String encryptedStr = AesCipher.encrypt(secretKey, convertedResponseStr);
        if (isEncrypted){
            convertedResponseStr = encryptedStr;
        }

        resp.setContentType(isEncrypted ? "text/text" : "application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        resp.getWriter().print(redirectingPath + "\n" + (convertedResponseStr));
        log.error("HttpMethod: POST. Server: " + redirectingPath + ". Status: OK");
        if (PropertyManager.getPropertyAsString("balanceMethod", null).equals("LeastConnections")){
            balancer.decrementRequestCounter();
        }
    }

    public HttpResponse sendRequest(String path, String reqStr){
        HttpResponse response = null;
        HttpClient client;
        HttpPost request;
        StringEntity entity;

        boolean isFault = false;
        do {
            try {
                client = HttpClientBuilder.create().build();
                request = new HttpPost(path);
                entity = new StringEntity(reqStr);
                request.setEntity(entity);

                response = client.execute(request);
                balancer.incrementRequestCounter();
                isFault = false;
                tryCounter = 0;
            } catch (HttpHostConnectException e) {
                path = changeServer();
                isFault = true;
                ++tryCounter;
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
    public String changeServer() {
        balancer.incrementRequestCounter();
        return balancer.getServerUrl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getRequestURL().toString() + "convert");
    }

}
