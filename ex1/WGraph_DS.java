package ex1;

import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WGraph_DS implements weighted_graph
{

    private static HashSet<Integer> usedkeys;
    private static int numofnodes = 0;
    private static int keyswap = 0;
    private HashMap<Integer,node_info> nodes;
    private int edgesize;
    private int MC;

    public WGraph_DS()
    {
        nodes = new HashMap<Integer,node_info>();
        edgesize = 0;
        MC = 0;
    }

    public WGraph_DS(WGraph_DS g)
    {
        if (g!=null)
        {
            for(node_info n :g.nodes.values())
            {
                nodes.put(n.getKey(),n);
            }
            for (node_info n: g.nodes.values())
            {

            }
        }
        else
        {

        }
    }
    @Override
    public node_info getNode(int key) {
        return null;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        return 0;
    }

    @Override
    public void addNode(int key) {

    }

    @Override
    public void connect(int node1, int node2, double w) {

    }

    @Override
    public Collection<node_info> getV() {
        return null;
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {

    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgesize;
    }

    @Override
    public int getMC() {
        return MC;
    }

    class NodeInfo implements node_info
    {
        private int key;
        private double tag;
        private String info;
        private HashMap<Integer,node_info> neighbors;
        private HashMap<Integer,Integer> neighborsweights;
        public NodeInfo()
        {
            this(numofnodes,0,"");
        }
        public NodeInfo(int key, double tag, String info)
        {
            boolean enter = false;
            if(usedkeys.contains(key))
            {
                enter = true;
                while (usedkeys.contains(keyswap))
                {
                    keyswap++;
                }
            }
            if (enter)
                key = keyswap;
            usedkeys.add(key);
            setKey(key);
            setTag(tag);
            setInfo(info);
            this.neighbors = new HashMap<Integer, node_info>();
            this.neighborsweights = new HashMap<Integer, Integer>();
            numofnodes++;

        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        private void setKey(int key) {
            this.key = key;
        }
    }
}

