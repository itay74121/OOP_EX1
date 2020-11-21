package ex1.src;


import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph,Serializable
{

    private final HashSet<Integer> usedkeys; // hashset of all the used keys, used to preserve uniqueness of node's key.
    private int numofnodes; // describe the number of instances of nodes that where made to this graph
    private final HashMap<Integer, node_info> nodes; // The hashmap which maps key to a node
    private int edgesize; // The edge size in the graph
    private int MC; // Mode Count of the graph represent how many modifications were made in the graph
    /** Doesn't take any arguments
     * A default constructor to the class Wgraph_DS*/
    public WGraph_DS()
    {
        // create a new hashmap to hold all the nodes in the graph
        nodes = new HashMap<Integer,node_info>(); // the hashmap takes key to a node
        edgesize = 0; // set the edge size to 0
        MC = 0; //  set the mode count to 0
        usedkeys = new HashSet<Integer>(); // get a new hashset for the keys
        numofnodes = 0; // set number of nodes instances to 0.

    }
    /**
     * @param g is a WGraph_DS
     * This is a copy constructor to the graph.
     * Helps in duplicating a graph.*/
    public WGraph_DS(WGraph_DS g)
    {
        if (g!=null) // make sure the given graph g is not null
        {
            // phase 0: initializing some starting variables
            usedkeys = new HashSet<Integer>(); // create a new hashset
            nodes = new HashMap<Integer, node_info>(); // create a new nodes hashmap
            // phase 1: copy nodes
            // copying all nodes with keys from g into this graph
            for(node_info n :g.nodes.values()) // loop through all nodes currently in the graph g
            {
                NodeInfo ni = new NodeInfo(n.getKey(),n.getTag(),n.getInfo()); // create a new node object using the values from n.
                nodes.put(ni.getKey(),ni);// creating a parallel node in this graph and assigning it to the same key in the hashmap
            }
            // phase 2: copy their relationships and weights
            for (node_info n: g.nodes.values()) // loop through the nodes currently inside the graph g
            {
                NodeInfo temp = (NodeInfo)n; // convert into the class NodeInfo so we could use it's functions
                NodeInfo thisnode = (NodeInfo) nodes.get(temp.getKey()); // get the corresponding node in our graph
                for(node_info neighbor: temp.getNi().values())  // go through all the neighbors of the node n
                {
                    NodeInfo other = (NodeInfo) nodes.get(neighbor.getKey()); // get parallel neighbor in our graph
                    if(!thisnode.hasNi(other)) // and if they are not already neighbors with the corresponding node in our graph
                        thisnode.addNi(other,temp.getweight(other.getKey())); // we connect them using the weight that neighbor have with n
                }
            }
            //phase 3: complete final copy
            edgesize = g.edgesize; // copy the same edge size
            MC = g.MC; // copy the same mode count
            numofnodes = g.numofnodes; // copy the number of nodes instances
            Iterator<Integer> iter = g.usedkeys.iterator(); // use an iterator to complete the hashset of the used nodes
            while (iter.hasNext()) // iterate through
            {
                usedkeys.add(iter.next()); // put inside used keys what was already used in graph g
            }
        }
        else // in-case that g is null after all
        {
            // in-case g is null do this just like a default constructor
            nodes = new HashMap<Integer,node_info>(); // init a new hashmap
            usedkeys = new HashSet<Integer>(); // init used keys
            numofnodes = 0; // set to 0
            edgesize = 0; //  set to 0
            MC = 0; // set to 0
        }
    }
    /**
     * @param key is a integer type key. can  be positive or negative
     * Get a node by key from the graph
     * @return a pointer to the node with the corresponding key, and
     * null if the node doesn't exists in the graph */
    @Override
    public node_info getNode(int key)
    {
        return nodes.get(key);
    }
    /**
     * accepts:
     * @param node1 is an int
     * @param node2 is an int
     * @return boolean if an edge exists in the graph between node1 and node2.*/
    @Override
    public boolean hasEdge(int node1, int node2)
    {
        if (getNode(node1)==null || getNode(node2)==null) // make sure both nodes in the graph
            return false; // if not false is returned
        NodeInfo n1 = (NodeInfo) getNode(node1); // cast so we could use hasNi inside NodeInfo
        node_info n2 = getNode(node2); // get the other node
        return n1.hasNi(n2);
    }
    /**
     * @param node1 the key of the first node
     * @param node2 the key of the second node
     * @return The value between the nodes, iff there is an edge between them.
     * If not it returns -1.*/
    @Override
    public double getEdge(int node1, int node2)
    {
        if (!hasEdge(node1,node2)) // check it there is no edge between them
            return -1; // if so return -1
        NodeInfo n1 = (NodeInfo) getNode(node1); // cast into NodeInfo so we could use the method "getweight"
        return n1.getweight(node2); // returns the value between them
    }

    /**
     * @param key of type int, describes the key value of certain node.
     * @return void the function doesn't return anything.
     * This function creates a new node and adds it to the graph immediately */
    @Override
    public void addNode(int key)
    {
        if (usedkeys.contains(key)) // make sure that the key isn't already used before in-order to maintain uniqueness
            return;
        NodeInfo temp = new NodeInfo(key,0,""); //  create the new node
        nodes.put(temp.getKey(),temp); // add it to the graph
        MC++; // add another to MC
    }
    /**
     * @param node1 the key of the first node
     * @param node2 the key of the second node
     * @param w the weight desired to be put between them.
     * @apiNote the value of w must be positive double, which means no values under 0 will be allowed.
     * @apiNote The function doesn't allow a node to be self adjacent.*/
    @Override
    public void connect(int node1, int node2, double w)
    {
        if (w<0) // if w is under 0
            return; // terminate function
        if (node1 == node2) // if the node is self neighboring
            return; // terminate function
        if (getNode(node1)==null|| getNode(node2)==null) // if one of the nodes not in the graph
            return; // terminate function
        NodeInfo n1 = (NodeInfo)getNode(node1); // get and cast the first node into n1
        if (n1.hasNi(node2)) // check if they are adjacent
        {
            // if so update their new weight
            n1.updateWeight(node2,w);
            MC++; // add one to mode count
        }
        else // otherwise make a connection and put the new weight
        {
            n1.addNi(getNode(node2),w);
            MC++; // add one to mode count
            edgesize++; // and add one to edge size
        }
    }
    /**
     * function takes no parameters and
     * @return a shallow copy (pointer)to an Collection
     * which contains all the nodes in the graph.*/
    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }
    /**
     * @param node_id  is the key value of the desired node
     * @return a copy to a Collection which contains all the
     * adjacent nodes to node_id.
     * */
    @Override
    public Collection<node_info> getV(int node_id)
    {
        NodeInfo n = (NodeInfo) getNode(node_id); // get and cast the node into NodeInfo n
        if (n==null) // check if its null
            return null; // if so return null since node is not in the graph and has no neighbors
        else // otherwise
        {
            ArrayList<node_info> ret = new ArrayList<node_info>(); // create an array list of the nodes
            for(node_info t:n.getNi().values()) // loop through n neighbors
            {
                ret.add(t); // add them to ret
            }
            return ret; // return ret
        }
    }
    /**
     * @param key of type int represents the key of the node
     * @return the node that have been removed from the graph*/
    @Override
    public node_info removeNode(int key) {
        if(nodes.containsKey(key)) //make sure that node is in the graph
        {
            NodeInfo n = (NodeInfo) getNode(key); // get and cast into NodeInfo n
            edgesize -= n.getNi().size(); //update edgesize to be minus all the neighbors
            MC += n.getNi().size()+1; // update the mode count to be all the neighbors plus removing the node itself from the graph
            n.destroyConnections(); // use the function destroyConnections to disconnect node from all of it's neighbors
            nodes.remove(n.getKey()); // remove the node it-self from the map
            return n; // return the removed node
        }
        else // otherwise return null since no node was removed from the graph
            return null;
    }
    /**
     * @param node1 the key of the first node
     * @param node2 the key of the second node
     * The function removes the edge between these two nodes iff there is an edge between them*/
    @Override
    public void removeEdge(int node1, int node2)
    {
        if (getNode(node1)==null || getNode(node2)==null) // make sure they are both in the graph
            return; // terminate function
        NodeInfo n1 = (NodeInfo) getNode(node1); //get and cast into NodeInfo n1 in-order to gain access to the hasNi function
        if (n1.hasNi(node2))// make sure that they are neighbors
        {
            if (n1.removeNi(node2)) // if the nodes has stopped being neighbors the function returns true
            {
                edgesize--; // update edgesize
                MC++; // update mode count
            }
        }
    }
    /**
     * @return The node size in the graph*/
    @Override
    public int nodeSize()
    {
        return nodes.size();
    }

    /**
     * @return return the edge size in the graph*/
    @Override
    public int edgeSize() {
        return edgesize;
    }

    /**
     *
     * @return The modification count on the graph */
    @Override
    public int getMC() {
        return MC;
    }

    /**
     * @return A string representation of the graph */
    public String toString()
    {
        String ret = ""; // start with an empty string
        for(node_info n: getV()) // loop through all the nodes in the graph
        {
            NodeInfo t1 = (NodeInfo) n; // cast n into NodeInfo n
            ret += t1; // add t1 using the NodeInfo toString
            ret += "[ "; // addition to make neat
            for(node_info j: getV(t1.getKey())) // loop through n neighbors
            {
                NodeInfo t2 = (NodeInfo) j; // cast into t2 NodeInfo
                ret += t2; // add using NodeInfo toString
                ret += "("+t2.getweight(n.getKey())+")"; // add the weight in parenthesis
            }
            ret += " ]\n"; // add final closing ] with new line
        }
        return ret; // return the result
    }

    /**
     *
     * @param o Of type object
     * @return boolean if this graph equals to the graph o.
     */
    public boolean equals(Object o)
    {
        if (!(o instanceof WGraph_DS))// if o is not a graph object
            return false;

        WGraph_DS other = (WGraph_DS)o; // cast into WGraph_DS since we know it's a graph
        if(other.edgeSize()!=edgeSize() || nodeSize()!=other.nodeSize()) // if the edge size or the node size are not the same it's not the same graph
            return false;
        //phase1: make sure all nodes in other graph have a parallel node in this graph
        for (node_info n: other.nodes.values())
        {
            if(getNode(n.getKey())==null) return false; // if the key of n doesn't exist in this graph return false
            if(!((NodeInfo)getNode(n.getKey())).equals(n)) return false; // if the corresponding node doesn't equal the node n return false
        }
        //phase2: make sure all nodes in this graph have a parallel equal node in the
        for (node_info n: nodes.values())
        {   // Do the same as above
            if(other.getNode(n.getKey())==null) return false;
            if(!((NodeInfo)other.getNode(n.getKey())).equals(n)) return false;
        }
        // if all the nodes in the other graph have an equal node in this graph and vice-versa thus we can infer that they are both equals
        return true;

    }

    public class NodeInfo implements node_info, Serializable {
        private int key; // The unique value of the node
        private double tag; // tag of the node
        private String info; // info about the node like color or state of visited or unvisited
        private HashMap<Integer, node_info> neighbors; // a hashmap to contain all the keys of the neighboring nodes pointing to the neighboring nodes
        private HashMap<Integer, Double> neighborsweights; // a hashmap to contain the weights of the neighboring nodes

        /**
         *
         * @param key the unique key of the node
         * @param tag desired tag of the node
         * @param info // desired info for the node
         */
        public NodeInfo(int key, double tag, String info) {
            usedkeys.add(key); // add the key to the used
            setKey(key); // set the key
            setTag(tag); // set the tag
            setInfo(info); // set the info
            this.neighbors = new HashMap<Integer, node_info>(); // initiate a hashmap for neighbors
            this.neighborsweights = new HashMap<Integer, Double>(); // initiate a hashmap for weights
            numofnodes++; // add one to the nodes that were created
        }

        /**
         *
         * @param o object of NodeInfo
         * @return if the node o is equal to this node
         */
        public boolean equals(Object o)
        {
            NodeInfo other=null; // initiate other null
            if (o instanceof NodeInfo) // make sure that o is NodeInfo
                other = (NodeInfo)o; // if so downcast into other
            if (other!=null) // if other isn't null
            {
                if (other.getKey() != getKey()) // if their key is not the same return false
                    return false;
                for(node_info n:other.getNi().values()) // loop through neighbors of of other
                {
                    if(!hasNi(n.getKey())) return false; // if this node has a corresponding neighbor with the same key as n
                    if (other.getweight(n.getKey())!=getweight(n.getKey())) return false; // check if other weight with n is the same as this node has with the neighbor with the key of n
                }
                return true; // return true if we passed all these ex1.ex1.src.tests
            }
            else //otherwise return false
                return false;
        }

        /**
         *
         * @return the neighbors hashmap
         */
        public HashMap<Integer, node_info> getNi() {
            return neighbors;
        }

        /**
         *
         * @return  the weights hashmap
         */
        public HashMap<Integer, Double> getNiWeights() {
            return neighborsweights;
        }

        /**
         *
         * @return  the key of this node
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         *
         * @return the info of this node
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         *
         * @param s of type String
         * This function puts  the String s in the info of this node.
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         *
         * @return the tag of this node.
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * Set the value key of this node. function declared private to preserve uniqueness.
         * @param key of type int.
         */
        private void setKey(int key) {
            this.key = key;
        }

        /**
         * Adding a neighbor to this node.
         * @param other of type node_info, represent the other node.
         * @param otherweight of type double, represent the node.
         * @return true if the node was added with this weight and false if not added.
         */
        public boolean addNi(node_info other, double otherweight) {
            if (!neighbors.containsKey(other.getKey()) && !neighborsweights.containsKey(other.getKey())) // make sure they are not neighbors and they don't have weights
            {
                // inseret other into hashmap and weight hashmap
                neighbors.put(other.getKey(), other); // insert into hashmap
                neighborsweights.put(other.getKey(), otherweight); // insert weight

                NodeInfo temp = (NodeInfo) other; // cast into NodeInfo temp
                // put the same otherway around
                temp.getNi().put(this.getKey(), this);
                temp.getNiWeights().put(this.getKey(), otherweight);
                return true; // return true since the process is done
            }
            else // otherwise return false
            {
                return false;
            }
        }

        /**
         *  Removing a neighbor from this node
         * @param other of type node_info
         * @return true if was succesfuly removed and false if not.
         */
        public boolean removeNi(node_info other)
        {
            if (other == null)  // if its null
                return false; // return false since it was not removed
            if (neighbors.containsKey(other.getKey()) && neighborsweights.containsKey(other.getKey())) // make sure that they are neighbors.
            {
                neighbors.remove(other.getKey()); // remove other from this neighbor hashmap
                neighborsweights.remove(other.getKey()); // remove the weight of other from this weight hashmap
                NodeInfo t = (NodeInfo) other; // cast other into NodeInfo t
                t.getNi().remove(getKey()); // do the same but vice-versa
                t.getNiWeights().remove(getKey()); // do the same but vice-versa
                return true; //  return true operation was done successfully
            }
            else
            {
                return false; // return false operation failed
            }
        }

        /**
         *
         * @param key the key of the desired node
         * @return true if the node was removed
         */
        public boolean removeNi(int key)
        {
            node_info n = neighbors.get(key); // get the node from the neighbors hashmap
            return removeNi(n); // let the other removeNi handle this
        }

        /**
         *
         * @param other of type node_info
         * @return true if this node is a neighbor of this other node
         */
        public boolean hasNi(node_info other)
        {
            return neighborsweights.containsKey(other.getKey()) && neighbors.containsKey(other.getKey()); // return if it has a value in neighbors hashmap and weights hashmap
        }

        /**
         *
         * @param key the key of the desired node
         * @return true if they are neighbors
         */
        public boolean hasNi(int key)
        {
            return neighborsweights.containsKey(key) && neighbors.containsKey(key); // check if weights and neighbors hashmap contain it's key
        }

        /**
         *
         * @param key the key of the desired node
         * @return the weight between the node with key and this node
         */
        public double getweight(int key)
        {
            if (neighborsweights.containsKey(key)) // if there is weight between them
                return neighborsweights.get(key); // return the weight
            else
                return -1;// otherwise return -1
        }

        /**
         *
         * @param key the key of the desired node
         * @param weight the weight desired to be updated
         * @return true if successful if not false
         */
        public boolean updateWeight(int key,double weight)
        {
            if (weight<0) // if weight is negative return  false
                return false;
            if (neighbors.containsKey(key)) // check if the neighbors contain weight
            {
                neighborsweights.put(key,weight); // update the weight
                ((NodeInfo)neighbors.get(key)).neighborsweights.put(getKey(),weight); //  do this in the other node
                return true;
            }
            else // otherwise return false
                return false;
        }

        /**
         * Destroy all the connection between this node and all it's neighbors
         */
        public void destroyConnections()
        {
            for(node_info t: getV(getKey())) // loop through all the neighboring nodes
            {
                removeNi(t); // remove the neighbor
            }
        }

        /**
         *
         * @return A string representation of this node.
         */
        public String toString()
        {
            return "[Node "+getKey()+"]"; //  return this representation with the key : [Node ${key}]
        }

    }
}

