package org.example.balancer;

import junit.framework.TestCase;
import org.example.utils.PropertyManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundRobinBalancerTest {
    RoundRobinBalancer roundRobinBalancer;
    String[] addresses;
    @Before
    public void init() throws Exception {
        PropertyManager.load();
        roundRobinBalancer = new RoundRobinBalancer();
        addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
    }

    @Test
    public void getServerUrlTest() {
        roundRobinBalancer.incrementRequestCounter();
        assertEquals(addresses[1] + "/convert", roundRobinBalancer.getServerUrl());
    }

    @Test
    public void incrementRequestCounterTest() {
        roundRobinBalancer.incrementRequestCounter();
        roundRobinBalancer.incrementRequestCounter();
        roundRobinBalancer.incrementRequestCounter();
        assertEquals(addresses[3] + "/convert", roundRobinBalancer.getServerUrl());
    }
}