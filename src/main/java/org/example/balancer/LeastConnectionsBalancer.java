package org.example.balancer;

import org.example.utils.PropertyManager;

public class LeastConnectionsBalancer implements IBalancer {
    private int serverCounter;
    private int currentIndex;
    private int[] activeRequestCounters;
    public int getServerCounter() { return serverCounter; }

    public LeastConnectionsBalancer() {
        serverCounter = PropertyManager.getPropertyAsString("addresses", null).split(";").length;
        activeRequestCounters = new int[serverCounter];
    }
    public String getServerUrl() {
        int min = activeRequestCounters[0];
        for (int i = 0; i < activeRequestCounters.length; ++i) {
            if (activeRequestCounters[i] < min){
                min = activeRequestCounters[i];
                currentIndex = i;
            }
        }

        String[] addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        String redirectingPath = addresses[currentIndex];
        redirectingPath += "/convert";
        return redirectingPath;
    }
    public void incrementRequestCounter() {
        ++activeRequestCounters[currentIndex];
    }
    public void decrementRequestCounter() {
        --activeRequestCounters[currentIndex];
    }
}
