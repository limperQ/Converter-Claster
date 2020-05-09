package org.example.balancer;

public interface IBalancer {
    public String getServerUrl();
    public void incrementRequestCounter();
}
