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

public class Main
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

        handler.addServletWithMapping(TransportServlet.class, "/");
        handler.addServletWithMapping(MainServlet.class, "/convert");

        try
        {
            server.start();
            log.error("Server has started at port: " + port);
//            server.join();
        }catch(Throwable t){
            log.error("Error while starting server", t);
        }
    }

    private static void runServer() {
        int transportPort = PropertyManager.getPropertyAsInteger("serverTransport.port", 8026);
        int converter1Port = PropertyManager.getPropertyAsInteger("serverConverter.port", 8027);
        int converter2Port = PropertyManager.getPropertyAsInteger("serverConverter.port", 8028);
        String contextStr = PropertyManager.getPropertyAsString("server.context", "server");

        runServer(transportPort, contextStr);
        runServer(converter1Port, contextStr);
        runServer(converter2Port, contextStr);
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
