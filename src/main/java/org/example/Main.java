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
        int port = PropertyManager.getPropertyAsInteger("serverTransport.port", 8027);
        int port2 = PropertyManager.getPropertyAsInteger("serverConverter1.port", 8028);
        int port3 = PropertyManager.getPropertyAsInteger("serverConverter2.port", 8029);

        String contextStr = PropertyManager.getPropertyAsString("server.context", "server");

        runServer(port, contextStr);
        //runServer(port2, contextStr);
        runServer(port3, contextStr);
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
