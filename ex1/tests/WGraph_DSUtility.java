package ex1.tests;


import ex1.src.node_info;
import ex1.src.WGraph_DS;

import java.util.Random;

public final class WGraph_DSUtility
{
    private static Random _rnd = null;

    /***
     *
     * @param v_size nodes size
     * @param e_size edges size
     * @param seed for the random values
     * @return a WGraph_DS object with v_size nodes and e_size edges
     */
    public static WGraph_DS graphCreator(int v_size, int e_size, int seed)
    {
        WGraph_DS g = new WGraph_DS(); // create the new graph
        _rnd = new Random(seed); // insert the seed
        for (int i = 0; i < v_size; i++) { // create v_size nodes in the graph
            g.addNode(i);
        }
        Object [] nodes =  g.getV().toArray(); // cast list of all nodes into Object[] array
        while(g.edgeSize() < e_size) { // loop as long as there are less than e_size edges in the graph
            int a = (int)(_rnd.nextDouble()*v_size); // take random node index
            int b =  (int)(_rnd.nextDouble()*v_size); // take random node index
            int c = (int)(_rnd.nextDouble()*1000); // take random value from 0 to 1000
            int i = ((node_info)nodes[a]).getKey(); // get the key of the node at that index
            int j = ((node_info)nodes[b]).getKey(); // get the key of the node at that index
            g.connect(i,j,c); // connect them
        }
        return g; // return the graph g since it's ready
    }
}
