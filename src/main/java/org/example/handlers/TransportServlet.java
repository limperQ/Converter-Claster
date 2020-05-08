package org.example.handlers;

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
    private static int requestCounter = 0;
    private static int serverCounter = 3;
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectingPath = "http://localhost:";

        switch(requestCounter % serverCounter) {
            case (0): redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8026).toString(); break;
            case (1): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8027).toString(); break;
            case (2): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8028).toString(); break;
        }
        //  TODO: переадресует на doGET, нужно на doPost
        redirectingPath += "/convert";
        resp.sendRedirect(redirectingPath);
        ++requestCounter;
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
