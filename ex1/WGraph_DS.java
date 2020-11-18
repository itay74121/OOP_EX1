package ex1;


import java.io.Serializable;
import java.time.temporal.WeekFields;
import java.util.*;

public class WGraph_DS implements weighted_graph,Serializable
{

    private HashSet<Integer> usedkeys;
    private int numofnodes;
    private HashMap<Integer,node_info> nodes;
    private int edgesize;
    private int MC;

    public WGraph_DS()
    {
        nodes = new HashMap<Integer,node_info>();
        edgesize = 0;
        MC = 0;
        usedkeys = new HashSet<Integer>();
        numofnodes = 0;

    }

    public WGraph_DS(WGraph_DS g)
    {
        if (g!=null)
        {
            usedkeys = new HashSet<Integer>();
            nodes = new HashMap<Integer, node_info>();
            // phase 1: copy nodes
            for(node_info n :g.nodes.values())
            {
                NodeInfo ni = new NodeInfo(n.getKey(),n.getTag(),n.getInfo());
                nodes.put(ni.getKey(),ni);
            }
            // phase 2: copy their relationships and weights
            for (node_info n: g.nodes.values())
            {
                NodeInfo temp = (NodeInfo)n;
                NodeInfo thisnode = (NodeInfo) nodes.get(temp.getKey());
                for(node_info neighbor: temp.getNi().values())
                {
                    NodeInfo other = (NodeInfo) nodes.get(neighbor.getKey());
                    if(!thisnode.hasNi(other))
                        thisnode.addNi(other,temp.getweight(other.getKey()));
                }
            }
            //phase 3: complete final copy
            edgesize = g.edgesize;
            MC = g.MC;
            numofnodes = g.numofnodes;
            Iterator<Integer> iter = g.usedkeys.iterator();
            while (iter.hasNext())
            {
                usedkeys.add(iter.next());
            }
        }
        else
        {
            // in-case g is null do this:
            nodes = new HashMap<Integer,node_info>();
            usedkeys = new HashSet<Integer>();
            numofnodes = 0;
            edgesize = 0;
            MC = 0;
        }
    }

    @Override
    public node_info getNode(int key)
    {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2)
    {
        if (getNode(node1)==null || getNode(node2)==null)
            return false;
        NodeInfo n1 = (NodeInfo) getNode(node1);
        node_info n2 = getNode(node2);
        return n1.hasNi(n2);
    }

    @Override
    public double getEdge(int node1, int node2)
    {
        NodeInfo n1 = (NodeInfo) getNode(node1);
        return n1.getweight(node2); // returns -1 if they are not connected.
    }


    @Override
    public void addNode(int key)
    {
        if (usedkeys.contains(key))
            return;
        NodeInfo temp = new NodeInfo(key,0,"");
        nodes.put(temp.getKey(),temp);
        MC++;
    }

    @Override
    public void connect(int node1, int node2, double w)
    {
        if (node1 == node2)
            return;
        if (getNode(node1)==null|| getNode(node2)==null)
            return;
        NodeInfo n1 = (NodeInfo)getNode(node1);
        if (n1.hasNi(node2))
        {
            n1.updateWeight(node2,w);
            MC++;
        }
        else
        {
            n1.addNi(getNode(node2),w);
            MC++;
            edgesize++;
        }
    }

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id)
    {
        NodeInfo n = (NodeInfo) getNode(node_id);
        if (n==null)
            return null;
        else
        {
            ArrayList<node_info> ret = new ArrayList<node_info>();
            for(node_info t:n.getNi().values())
            {
                ret.add(t);
            }
            return ret;
        }
    }

    @Override
    public node_info removeNode(int key) {
        if(nodes.containsKey(key))
        {
            NodeInfo n = (NodeInfo) getNode(key);
            edgesize -= n.getNi().size();
            MC += n.getNi().size()+1;
            n.destroyConnections();
            nodes.remove(n.getKey());
            return n;
        }
        else
            return null;
    }

    @Override
    public void removeEdge(int node1, int node2)
    {
        if (getNode(node1)==null || getNode(node2)==null)
            return;
        NodeInfo n1 = (NodeInfo) getNode(node1);
        if (n1.hasNi(node2))
        {
            n1.removeNi(node2);
        }
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
    public String toString()
    {
        String ret = "";
        for(node_info n: getV())
        {
            NodeInfo t1 = (NodeInfo) n;
            ret += t1;
            ret += "[ ";
            for(node_info j: getV(t1.getKey()))
            {
                NodeInfo t2 = (NodeInfo) j;
                ret += t2;
                ret += "("+t2.getweight(n.getKey())+")";
            }
            ret += " ]\n";
        }
        return ret;
    }

    public boolean equals(Object o)
    {
        WGraph_DS other = (WGraph_DS)o;
        if(other.edgeSize()!=edgeSize() || nodeSize()!=other.nodeSize())
            return false;
        //phase1:
        for (node_info n: other.nodes.values())
        {
            if(getNode(n.getKey())==null) return false;
            if(!((NodeInfo)getNode(n.getKey())).equals(n)) return false;
        }
        //phase2:
        for (node_info n: nodes.values())
        {
            if(other.getNode(n.getKey())==null) return false;
            if(!((NodeInfo)other.getNode(n.getKey())).equals(n)) return false;
        }
        return true;

    }
    public class NodeInfo implements node_info, Serializable {
        private int key;
        private double tag;
        private String info;
        private HashMap<Integer, node_info> neighbors;
        private HashMap<Integer, Double> neighborsweights;

        public NodeInfo() {
            this(numofnodes, 0, "");
        }

        public NodeInfo(int key, double tag, String info) {
            usedkeys.add(key);
            setKey(key);
            setTag(tag);
            setInfo(info);
            this.neighbors = new HashMap<Integer, node_info>();
            this.neighborsweights = new HashMap<Integer, Double>();
            numofnodes++;

        }

        public boolean equals(Object o)
        {
            NodeInfo other = (NodeInfo)o;
            if (other.getKey() != getKey())
                return false;
            for(node_info n:other.getNi().values())
            {
                if(!hasNi(n.getKey())) return false; // if its has it as a neighbor
                if (other.getweight(n.getKey())!=getweight(n.getKey())) return false; // check if
            }
            return true;
        }

        public HashMap<Integer, node_info> getNi() {
            return neighbors;
        }

        public HashMap<Integer, Double> getNiWeights() {
            return neighborsweights;
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
            this.info = s;
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

        public boolean addNi(node_info other, double otherweight) {
            if (!neighbors.containsKey(other.getKey()) && !neighborsweights.containsKey(other.getKey()))
            {
                // inseret other into hashmap and weight hashmap
                neighbors.put(other.getKey(), other);
                neighborsweights.put(other.getKey(), otherweight);

                NodeInfo temp = (NodeInfo) other; // cast into object
                // put the same otherway around
                temp.getNi().put(this.getKey(), this);
                temp.getNiWeights().put(this.getKey(), otherweight);
                return true;
            } else {
                return false;
            }
        }

        public boolean removeNi(node_info other)
        {
            if (other == null)
                return false;
            if (neighbors.containsKey(other.getKey()) && neighborsweights.containsKey(other.getKey()))
            {
                neighbors.remove(other.getKey());
                neighborsweights.remove(other.getKey());
                NodeInfo t = (NodeInfo) other;
                t.getNi().remove(getKey());
                t.getNiWeights().remove(getKey());
                return true;
            }
            else
            {
                return false;
            }
        }
        public boolean removeNi(int key)
        {
            node_info n = neighbors.get(key);
            return removeNi(n);
        }

        public boolean hasNi(node_info other)
        {
            return neighborsweights.containsKey(other.getKey()) && neighbors.containsKey(other.getKey());
        }

        public boolean hasNi(int key)
        {
            return neighborsweights.containsKey(key) && neighbors.containsKey(key);
        }

        public double getweight(int key)
        {
            if (neighborsweights.containsKey(key))
                return neighborsweights.get(key);
            else
                return -1;
        }

        public boolean updateWeight(int key,double weight)
        {
            if (neighbors.containsKey(key))
            {
                neighborsweights.put(key,weight);
                ((NodeInfo)neighbors.get(key)).neighborsweights.put(getKey(),weight);
                return true;
            }
            else
                return false;
        }

        public void destroyConnections()
        {
            for(node_info t: getV(getKey()))
            {
                removeNi(t);
            }
        }

        public String toString()
        {
            return "[Node "+getKey()+"]";
        }

    }
}

