package org.example.balancer;

public interface Balancer {
    public String getServerUrl();
    public void incrementRequestCounter();
    public void decrementRequestCounter();
}
