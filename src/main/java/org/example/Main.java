package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.example.handlers.MainServlet;
import org.example.handlers.TransportServlet;
import org.example.utils.Common;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class    Main
{
    private static Logger log = LoggerFactory.getLogger(Main.class.getSimpleName());

    private static Server server;

    public static void main(String[] args) throws Exception
    {
        PropertyManager.load();
        Common.configure();
        runServer();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {

                stopServer();

            }
        },"Stop Jetty Hook"));
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
        String[] adresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        for (int i = 0;i < adresses.length; ++i){
            contextStr = adresses[i].split(":")[0] + adresses[i].split(":")[1];
            port = Integer.parseInt(adresses[i].split(":")[2], 10);
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
