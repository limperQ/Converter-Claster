package org.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.example.handlers.MainServlet;
import org.example.handlers.TransportServlet;
import org.example.utils.Common;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main
{
    private static Logger log = LoggerFactory.getLogger(Main.class.getSimpleName());
    private static String[] addresses;
    private static Server server;

    public static void main(String[] args) throws Exception
    {
        PropertyManager.load();
        Common.configure();

        addresses = PropertyManager.getPropertyAsString("addresses", "localhost:8888").split(";");

        runServer();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stopServer();
            }
        },"Stop Jetty Hook"));

        Thread healthCheck = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                        ArrayList<Integer> serverIndex = null;

                    try {
                        serverIndex = checkServers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (serverIndex.size() > 0) {
                            for (int index : serverIndex) {
                                String contextStr = addresses[index].split(":")[0] + addresses[index].split(":")[1];
                                int port = Integer.parseInt(addresses[index].split(":")[2], 10);
                                try {
                                    runServer(port, contextStr);
                                } catch (Exception ex) {
                                    log.error("The server " + contextStr + ":" + port + " is already running");
                                    continue;
                                }
                            }
                        }
                    try {
                        Thread.sleep(PropertyManager.getPropertyAsInteger("healthCheckPeriod", 5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        healthCheck.start();
    }

    public static ArrayList<Integer> checkServers() throws IOException {
        ArrayList<Integer> downServerIndexes = new ArrayList<Integer>();
        for (int i = 0; i < addresses.length; ++i) {
            String url = addresses[i];
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            try {
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                    downServerIndexes.add(i);
                }
            }
            catch (HttpHostConnectException ex) {
                downServerIndexes.add(i);
            }
        }
        return downServerIndexes;
    }

    public static void runServer(int port, String contextStr)
    {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextStr);
        server.setHandler(context);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(MainServlet.class, "/convert");
        handler.addServletWithMapping(TransportServlet.class, "/");

        try
        {
            server.start();
            log.error("Server has started at port: " + port);
            //server.join();
        }catch(Throwable t){
            log.error("Error while starting server", t);
        }
    }

    private static void runServer() {
        String contextStr;
        int port;

        for (int i = 0; i < addresses.length; ++i){
            contextStr = addresses[i].split(":")[0] + addresses[i].split(":")[1];
            port = Integer.parseInt(addresses[i].split(":")[2], 10);
            runServer(port, contextStr);
        }
    }

    public static void stopServer() {
        try {
            if(server.isRunning()){
                server.stop();
            }
        } catch (Exception e) {
            log.error("Error while stopping server", e);
        }
    }

}
