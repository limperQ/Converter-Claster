package org.example.balancer;

import org.example.utils.PropertyManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeastConnectionsBalancerTest {

    LeastConnectionsBalancer leastConnectionsBalancer;
    String[] addresses;
    @Before
    public void init() throws Exception {
        PropertyManager.load();
        leastConnectionsBalancer = new LeastConnectionsBalancer();
        addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
    }

    @Test
    public void getServerUrlTest() {
        leastConnectionsBalancer.incrementRequestCounter();
        assertEquals(addresses[1] + "/convert", leastConnectionsBalancer.getServerUrl());
    }

    @Test
    public void incrementRequestCounterTest() {
        int previousCounter = leastConnectionsBalancer.getLeastActiveServerCounter();
        leastConnectionsBalancer.incrementRequestCounter();
        int newCounter = leastConnectionsBalancer.getLeastActiveServerCounter();
        assertEquals(previousCounter + 1, newCounter);
    }
}