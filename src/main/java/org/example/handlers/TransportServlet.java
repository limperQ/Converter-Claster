package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.Main;
import org.example.model.Answer;
import org.example.model.Item;
import org.example.model.Request;
import org.example.utils.Common;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransportServlet extends HttpServlet {
    private static int counter = 0;
    private static int serverCounter = 3;
    private static Logger log = LoggerFactory.getLogger(TransportServlet.class.getSimpleName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectingPath = "http://localhost:";

        switch(counter % serverCounter) {
            case (0): redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8026).toString(); break;
            case (1): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8027).toString(); break;
            case (2): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8028).toString(); break;
        }
        //  TODO: переадресует на doGET, нужно на doPost
        redirectingPath += "/convert";
        resp.sendRedirect(redirectingPath);
        ++serverCounter;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectingPath = "http://localhost:";
        switch(counter % serverCounter) {
            case (0): redirectingPath += PropertyManager.getPropertyAsInteger("serverTransport.port", 8026).toString(); break;
            case (1): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter1.port", 8027).toString(); break;
            case (2): redirectingPath += PropertyManager.getPropertyAsInteger("serverConverter2.port", 8028).toString(); break;
        }
        redirectingPath += "/convert";
        resp.sendRedirect(redirectingPath);
        ++serverCounter;
    }
}
