package org.example.balancer;

import org.example.utils.PropertyManager;

public class RoundRobinBalancer implements IBalancer{
    private int serverCounter;
    private int requestCounter;
    private String[] addresses;

    public int getRequestCounter() { return requestCounter; }
    public int getServerCounter() { return serverCounter; }

    public RoundRobinBalancer() {
        addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        serverCounter = addresses.length;
        requestCounter = 0;
    }
    public String getServerUrl() {
        String redirectingPath = addresses[requestCounter % serverCounter];
        redirectingPath += "/convert";
        return redirectingPath;
    }
    public void incrementRequestCounter(){
        ++requestCounter;
    }
    public void decrementRequestCounter() { --requestCounter; }
}
