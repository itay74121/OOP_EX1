package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;


class WGraph_AlgoTest {
    private WGraph_DS private_g;
    @BeforeEach
    private void init_private_g()
    {
        private_g = WGraph_DSUtility.graphCreator(10,9,1);
    }

    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        WGraph_DS g0 = private_g;
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0);
        // regular copy test
        WGraph_DS g1 = (WGraph_DS) ag0.copy();
        assertEquals(g1,g0);
        //what if there is no graph inside ag0
        ag0.init(null);
        assertNotEquals(ag0.copy(),g0);
    }

    @Test
    void isConnected() {
        WGraph_DS g0 = new WGraph_DS(); // create a graph
        // add nodes
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        // connect them
        g0.connect(1,2,5);
        g0.connect(1,3,6);
        // create a graph algo instance
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0); // init g0 into graph algo instance
        assertTrue(ag0.isConnected()); // check if the graph is connected and it should be
        g0.removeNode(1); // remove node 1
        assertFalse(ag0.isConnected()); // connectivity should be false
        g0.connect(2,3,9); // connect 2 to 3
        assertTrue(ag0.isConnected()); // connectivity should be true again
    }

    @Test
    void shortestPathDist() {
        // create a graph
        WGraph_DS g0 = new WGraph_DS();
        // add nodes
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        // connect the nodes
        g0.connect(1,2,5);
        g0.connect(1,3,6);
        g0.connect(2,3,16);
        // make a graph algo instance
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0); // init the graph to it
        assertEquals((int)ag0.shortestPathDist(2,3),11); //shortest path weight from 2 to 3 should be 11
    }

    @Test
    void shortestPath() {
        WGraph_DS g0 = new WGraph_DS(); // create a new graph
        // add nodes
        g0.addNode(1);
        g0.addNode(2);
        g0.addNode(3);
        // connect the nodes
        g0.connect(1,2,5);
        g0.connect(1,3,6);
        g0.connect(2,3,16);
        //create graph algo instance
        WGraph_Algo ag0 = new WGraph_Algo();
        ag0.init(g0); // init g0 into the instance
        // create an arraylist
        ArrayList<node_info> arr1 = new ArrayList<node_info>();
        ArrayList<node_info> arr2 = (ArrayList<node_info>) ag0.shortestPath(2,3); // use shortest path and get it into arraylist of type node_info
        // init the arraylist arr1
        arr1.add(g0.getNode(2));
        arr1.add(g0.getNode(1));
        arr1.add(g0.getNode(3));
        // check using assertion that their size match
        assertEquals(arr1.size(),arr2.size());
        // loop through both of their items and check that they match using assertion
        for (int i = 0; i < arr1.size(); i++) {
            assertEquals(arr1.get(i),arr2.get(i));
        }
    }
    @Test
    void save_load()
    {
        WGraph_DS g0 = private_g; // take in the private graph
        WGraph_Algo ag0 = new WGraph_Algo(g0); // create new instance of graph algo
        String path = System.getProperty("user.dir") + "/test.txt"; // take a path to a test file
        File f = new File(path); // create a file object
        try
        {
            if (f.exists())  // check if file exists
                f.delete(); // if so delete
            f.createNewFile(); // create a new file like the file we deleted
        }
        catch ( IOException e)
        {
            e.printStackTrace(); // print stack trace to see what went wrong
        }

        ag0.save(f.getPath()); // test the save
        ag0.load(f.getPath()); // test the load
        assertEquals(ag0.getGraph(),g0); // make sure that they are equal
        f.delete(); // delete the file again to make things clean.
    }
}