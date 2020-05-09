package org.example.balancer;

import org.example.utils.PropertyManager;

public class RoundRobinBalancer implements IBalancer{
    private int serverCounter;
    private int requestCounter;

    public int getRequestCounter() { return requestCounter; }
    public int getServerCounter() { return serverCounter; }

    public RoundRobinBalancer() {
        serverCounter = PropertyManager.getPropertyAsString("addresses", null).split(";").length;
        requestCounter = 0;
    }
    public String getServerUrl() {
        String[] addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        String redirectingPath = addresses[requestCounter % serverCounter];
        redirectingPath += "/convert";
        return redirectingPath;
    }
    public void incrementRequestCounter(){
        ++requestCounter;
    }
}
