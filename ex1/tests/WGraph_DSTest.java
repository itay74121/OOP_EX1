package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private static Random _rnd = null;

    @Test
    void getNode()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        assertNotNull(g0.getNode(0));
        assertNull(g0.getNode(12));
    }

    @Test
    void hasEdge()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
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
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        assertTrue(g0.getEdge(9,2)== g0.getEdge(2,9));
        assertTrue(g0.getEdge(6,9)==-1);
        assertTrue(g0.getEdge(-1,6)==-1);
        assertTrue(g0.getEdge(-5,30)==-1);
    }

    @Test
    void addNode()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        int size = g0.nodeSize();
        //adding the same node should do nothing
        g0.addNode(1);
        assertEquals(size,g0.nodeSize());
        //even after removing it
        g0.removeNode(1);
        g0.addNode(1);
        assertEquals(size-1,g0.nodeSize());
        //adding  a node with negative key should be okay
        g0.addNode(-1);
        assertEquals(size,g0.nodeSize());
        // and the rules of removing and adding again should be the same
        g0.removeNode(-1);
        g0.addNode(-1);
        assertEquals(size-1,g0.nodeSize());
    }

    @Test
    void connect()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);

        int edge_size = g0.edgeSize();
        // connecting nodes already connected shouldn't change edgesize
        // like 0 and 8 in graph g0
        g0.connect(0,8,g0.getEdge(0,8));
        g0.connect(8,0,g0.getEdge(0,8));
        assertEquals(edge_size,g0.edgeSize());
        // a node shouldn't be able to be self adjacent
        g0.connect(0,0,0); // connecting 0 to itself with weight 0
        assertEquals(edge_size,g0.edgeSize());
        //connecting nodes which don't exist in the graph
        g0.connect(-1,5,6);
        g0.connect(-1,-5,8);
        assertEquals(edge_size,g0.edgeSize());
        // update weight when they are already connected
        g0.connect(0,8,8);
        assertEquals(8,g0.getEdge(0,8));
        //update or connect nodes with negative weights
        int mc = g0.getMC();
        g0.connect(0,8,-5);
        g0.connect(0,5,-9);
        assertEquals(mc,g0.getMC());
        assertEquals(edge_size,g0.edgeSize());
        // connect nodes which are not connected with positive weight
        g0.connect(0,5,8);
        assertEquals(mc+1,g0.getMC());
        assertEquals(edge_size+1,g0.edgeSize());
    }

    @Test
    void getV()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        WGraph_DS g1 = WGraph_DSUtility.graphCreator(10,9,1);
        assertTrue(g0.getV().containsAll(g1.getV()) && g1.getV().containsAll(g0.getV()) ); // will check if it's the same shallow copy

    }

    @Test
    void testGetV()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        WGraph_DS g1 = WGraph_DSUtility.graphCreator(10,9,1);
        assertTrue(g0.getV(7).containsAll(g1.getV(7)) && g1.getV(7).containsAll(g0.getV(7)) );
    }

    @Test
    void removeNode()
    {
        WGraph_DS g0 = WGraph_DSUtility.graphCreator(10,9,1);
        int nodesize = g0.nodeSize();
        int mc =  g0.getMC();
        // removing a node that doesn't exists in the graph
        g0.removeNode(-1);
        assertEquals(mc,g0.getMC());
        assertEquals(nodesize,g0.nodeSize());
        // removing a node that is in the graph
        g0.removeNode(0);
        assertEquals(nodesize-1,g0.nodeSize());
        assertFalse(g0.hasEdge(8,0));
        assertEquals(mc+2,g0.getMC());
        //removing a node that had already been removed

    }

    @Test
    void removeEdge()
    {
        WGraph_DS g1 = WGraph_DSUtility.graphCreator(10,9,1);
        int e = g1.edgeSize();
        // remove edge
        g1.removeEdge(0,8);
        assertEquals(g1.edgeSize(),e-1);
        // remove non existent edge or an edge that had already been removed
        // The same lines of code would work again
        g1.removeEdge(0,8);
        assertEquals(g1.edgeSize(),e-1);
        // remove edge of nodes that don't exist
        g1.removeEdge(0,1); // edge doesn't exist
        g1.removeEdge(-5,-4); // both nodes not in the graph
        g1.removeEdge(1,-9); // one node in the graph the other is not
        assertEquals(g1.edgeSize(),e-1); // check if anything is updated, and it shouldn't
    }

    @Test
    void nodeSize()
    {
        WGraph_DS g1 = WGraph_DSUtility.graphCreator(10,9,1);
        assertEquals(g1.getV().size(),g1.nodeSize());
    }

    @Test
    void edgeSize()
    {
        WGraph_DS g1 = WGraph_DSUtility.graphCreator(10,9,1); // create a graph
        int l = 0; // the sum of all neighbors
        for(node_info n: g1.getV())
        {
            WGraph_DS.NodeInfo t = (WGraph_DS.NodeInfo) n; // casting into NodeInfo
            l += t.getNi().size(); // add the num of neighbors into l
        }
        assertEquals(l/2,g1.edgeSize()); // check according to the formula
    }

}