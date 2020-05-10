package org.example.balancer;

import org.example.utils.PropertyManager;

public class WeightedRoundRobinBalancer implements IBalancer{
    private int serverCounter;
    private int requestCounter;
    private String[] addresses;
    private int[] weights;
    private int totalWeightSum;

    public int getRequestCounter() { return requestCounter; }
    public int getServerCounter() { return serverCounter; }

    public WeightedRoundRobinBalancer() {
        addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
        serverCounter = addresses.length;
        weights = new int[serverCounter];
        requestCounter = 0;
        for (int i = 0; i < addresses.length; ++i) {
            weights[i] = 1;
            if (addresses[i].equals("http://localhost:8889") || addresses[i].equals(("http://localhost:8890"))) {
                weights[i] = 2;
            }
            totalWeightSum += weights[i];
        }
    }
    public String getServerUrl() {
        int weightSum = 0;
        String redirectingPath = addresses[0];
        for (int i = 0; i < addresses.length; ++i){
            weightSum += weights[i];
            if (weightSum > requestCounter % totalWeightSum) {
                redirectingPath = addresses[i] + "/convert";
                break;
            }
        }
        return redirectingPath;
    }
    public void incrementRequestCounter() { ++requestCounter; }
    public void decrementRequestCounter() { --requestCounter; }
}