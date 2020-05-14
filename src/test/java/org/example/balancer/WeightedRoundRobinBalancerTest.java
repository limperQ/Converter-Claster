package org.example.balancer;

import junit.framework.TestCase;
import org.example.utils.PropertyManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeightedRoundRobinBalancerTest {

    WeightedRoundRobinBalancer weightedRoundRobin;
    String[] addresses;
    @Before
    public void init() throws Exception {
        PropertyManager.load();
        weightedRoundRobin = new WeightedRoundRobinBalancer();
        addresses = PropertyManager.getPropertyAsString("addresses", null).split(";");
    }

    @Test
    public void getServerUrlTest() {
        weightedRoundRobin.incrementRequestCounter();
        assertEquals(addresses[1] + "/convert", weightedRoundRobin.getServerUrl());
    }

    @Test
    public void incrementRequestCounterTest() {
        weightedRoundRobin.incrementRequestCounter();
        weightedRoundRobin.incrementRequestCounter();
        weightedRoundRobin.incrementRequestCounter();
        assertEquals(addresses[2] + "/convert", weightedRoundRobin.getServerUrl());
    }
}