package org.example.balancer;

import org.example.utils.PropertyManager;

public class LeastConnectionsBalancer implements IBalancer {
    private int serverCounter;
    private int requestCounter;
    private int[] activeRequestCounters;
    public int getRequestCounter() { return requestCounter; }
    public int getServerCounter() { return serverCounter; }

    public LeastConnectionsBalancer() {
        serverCounter = PropertyManager.getPropertyAsString("addresses", null).split(";").length;
        requestCounter = 0;
        activeRequestCounters = new int[serverCounter];
    }
    public String getServerUrl() {
        int min = activeRequestCounters[0], minIndex = 0;
        for (int i = 0; i < activeRequestCounters.length; ++i) {
            if (activeRequestCounters[i] < min){
                min = activeRequestCounters[i];
                minIndex = i;
            }
        }

        String[] addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        ++activeRequestCounters[minIndex];
        String redirectingPath = addresses[minIndex];
        redirectingPath += "/convert";
        return redirectingPath;
    }
    public void incrementRequestCounter() {
        ++requestCounter;
    }

}
