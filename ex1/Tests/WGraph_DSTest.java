package ex1.Tests;

import ex1.WGraph_DS;
import ex1.node_info;
import ex1.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private static Random _rnd = null;

    @Test
    void getNode()
    {
        WGraph_DS g0 = WGraph_DSUtility.graph_creator(10,9,1);
        assertNotNull(g0.getNode(0));
        assertNull(g0.getNode(12));
    }

    @Test
    void hasEdge()
    {
        WGraph_DS g0 = WGraph_DSUtility.graph_creator(10,9,1);
        assertTrue(g0.hasEdge(9,2));
        assertTrue(g0.hasEdge(2,9));
        assertFalse(g0.hasEdge(9,6));
        assertFalse(g0.hasEdge(6,9));
        assertFalse(g0.hasEdge(-1,6));
        assertFalse(g0.hasEdge(-5,30));
    }

    @Test
    void getEdge()
    {
        WGraph_DS g0 = WGraph_DSUtility.graph_creator(10,9,1);
        assertTrue(g0.getEdge(9,2)== g0.getEdge(2,9));
        assertTrue(g0.getEdge(6,9)==-1);
        assertTrue(g0.getEdge(-1,6)==-1);
        assertTrue(g0.getEdge(-5,30)==-1);
    }

    @Test
    void addNode() {
    }

    @Test
    void connect() {

    }

    @Test
    void getV() {
    }

    @Test
    void testGetV() {
    }

    @Test
    void removeNode() {
    }

    @Test
    void removeEdge() {
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }

    @Test
    void testToString() {
    }

}