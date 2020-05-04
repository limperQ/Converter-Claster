package org.example.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.Main;
import org.example.model.Answer;
import org.example.model.Item;
import org.example.model.Request;
import org.example.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainServlet extends HttpServlet
{
    private static Logger log = LoggerFactory.getLogger(MainServlet.class.getSimpleName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String reqStr = IOUtils.toString(req.getInputStream());
        if(StringUtils.isBlank(reqStr))
        {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("BEEEE", null)));
            return;
        }
        Request r = Common.getPrettyGson().fromJson(reqStr, Request.class);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        List<Item> items = Arrays.asList(new Item("First", r.getName() + "1"));
        resp.getWriter().println(Common.getPrettyGson().toJson(new Answer("OK", items)));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        Answer answer = new Answer("OK", null);
        String str = Common.getPrettyGson().toJson(answer);
        log.error("Json:{}", str);
        response.getWriter().println(str);
    }


}
