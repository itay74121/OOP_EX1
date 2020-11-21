package ex1.src;



import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable
{
    private WGraph_DS g;

    /**
     * Default constructor
     */
    public WGraph_Algo()
    {
        this.g = null;
    }

    /**
     * A constructor
     * @param g of type WGraph_DS
     */
    public WGraph_Algo(WGraph_DS g)
    {
        init(g); // init the g graph
    }

    /**
     * init g into the g field in the class
     * @param g of type weighted_graph
     */
    @Override
    public void init(weighted_graph g)
    {
        this.g = (WGraph_DS) g; // cast g into the field g
    }

    /**
     *
     * @return the object in the field g of the class
     */
    @Override
    public weighted_graph getGraph() {
        return g;
    }

    /**
     *
     * @return A deep copy of the graph g
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(g);
    }

    /**
     *
     * @return true if graph g is connected using the bfs algorithm,
     * since it doesn't matter if it's weighted graph.
     */
    @Override
    public boolean isConnected() {
        if(g==null) // check if g is null
            return false;
        // can be done using bfs only
        Iterator<node_info> iter =  g.getV().iterator(); // create an iterator on all the nodes in the graph
        node_info n; // create n var
        if (iter.hasNext()) // check if there is another node in iterator
            n=iter.next(); // if so put in n
        else // otherwise return true since the graph has no node and it counts as connected
            return true;

        for(node_info node :g.getV()) // loop through all the nodes
        {
            node.setTag(0); // set all the tags as 0
        }
        n.setTag(1); // set n tag as 1
        LinkedList<WGraph_DS.NodeInfo> q = new LinkedList<WGraph_DS.NodeInfo>(); // create a queue
        q.add((WGraph_DS.NodeInfo) n); // add n to the queue after cast
        int count = 1; //  create a count variable to count how many where seen
        while(!q.isEmpty()) // while the queue is not empty null
        {
            node_info current = q.poll(); // poll out a node out of the queue
            for(node_info node: g.getV(current.getKey())) // loop through all the neighbors of that node
            {
                if(node.getTag()==0) //  if it's tag is 0 it has not been seen yet
                {
                    count++; // add one to the count of seen nodes
                    node.setTag(1); // set the tag as 1, means it was seen
                    q.add((WGraph_DS.NodeInfo) node); // add it to the queue
                }
            }
        }
        return count == g.getV().size(); // return  boolean of all the number of all the nodes that were seen is equal to the number of all the nodes
    }

    /**
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return The total weight of the path with the minimal weight
     * returns -1 if there is no way from ex1.src to dest.
     */
    @Override
    public double shortestPathDist(int src, int dest)
    {
        if (src==dest) // if they are the same the weight is 0
            return 0;
        if(g.getNode(src) == null || g.getNode(dest) == null) // if one is not in the graph the weight is -1
            return -1;
        // make a priority queue
        PriorityQueue<WGraph_DS.NodeInfo> queue = new PriorityQueue<WGraph_DS.NodeInfo>(g.getV().size(),new NodeInfoComparator());

        // put infinity in all tags
        for(node_info n: g.getV())
        {
            n.setTag(-1); // -1 since it's forbidden to have negative weight
        }

        WGraph_DS.NodeInfo srcnode =(WGraph_DS.NodeInfo) g.getNode(src); // get the source node

        // add ex1.src to queue
        srcnode.setTag(0); // set the weight at the source to 0
        queue.add(srcnode); // add it to queue
        //start looping
        while(!queue.isEmpty()) // make sure that the queue isn't empty
        {
            WGraph_DS.NodeInfo n = queue.poll(); // poll a node out of the queue
            for(node_info ni: n.getNi().values()) // loop through the nodes neighbors
            {
                if (ni.getTag()==-1) // never been to this node
                {
                    ni.setTag(n.getTag()+ n.getweight(ni.getKey())); // set the tag with the weight
                    queue.add((WGraph_DS.NodeInfo) ni); // add the node to the queue
                }
                if (ni.getTag()>=0 && ni.getTag()>n.getweight(ni.getKey())+n.getTag()) // already been here might as well update it in the queue
                {
                    // updates in queue are done by removing and adding again.
                    ni.setTag(n.getweight(ni.getKey())+n.getTag()); // set new weight
                    queue.remove(ni); // remove from queue
                    queue.add((WGraph_DS.NodeInfo) ni); // add back to queue
                }
            }
        }

        node_info d = g.getNode(dest); // create variable d to hold the destination
        if (d.getTag()==-1) // if never reached the destination
            return -1;
        return d.getTag(); // return the value in the tag of destination since it's the minimal weight to reach it.
    }

    /**
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return A List of node_info whihc is the path from ex1.src to dest in the graph
     * it returns null if there is no such path.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (g.getNode(src) == null || g.getNode(dest)==null) // check if one of the ex1.src or dest is not in the graph
            return null;
        if(src == dest) // private case of the same node check
        {
            ArrayList<node_info> arr = new ArrayList<node_info>();
            arr.add(g.getNode(src));
            return arr; // return an array with just the ex1.src node
        }
        // set all the tags to the value of -1, representing infinity in djikstra algorithm
        for(node_info n :g.getV())
        {
            n.setTag(-1);//set as -1
        }
        // create a priority queue using the NodeInfoComparator as a comparator
        PriorityQueue<WGraph_DS.NodeInfo> queue = new PriorityQueue<WGraph_DS.NodeInfo>(g.getV().size(),new NodeInfoComparator());
        HashMap<node_info,node_info> parent = new HashMap<node_info,node_info>(); // create a hashmap to store the path and call it parent
        WGraph_DS.NodeInfo s = (WGraph_DS.NodeInfo) g.getNode(src); // get the source and put it in NodeInfo instance
        s.setTag(0); // set it's tag to 0
        queue.add(s); // add it to the queue
        while(!queue.isEmpty()) // run as long as the queue is not empty
        {
            WGraph_DS.NodeInfo n = queue.poll(); // poll out a node from the queue
            for (node_info nei:n.getNi().values()) // loop through the neighbors of the node n
            {
                if (nei.getTag()==-1) // check if the nei tag is -1, if so we never been to this node
                {
                    nei.setTag(n.getTag()+n.getweight(nei.getKey())); //update it's tag
                    queue.add((WGraph_DS.NodeInfo)nei); // add it to the queue
                    parent.put(nei,n); // insert it to hashmap as the way to get to it is through the node n
                }
                if (nei.getTag()>=0) // we have been to this node
                {
                    if (n.getTag()+n.getweight(nei.getKey())<nei.getTag()) // check if the new way to get to it is with less weight
                    {
                        // need to update the new weight in the map and in the queue
                        queue.remove((WGraph_DS.NodeInfo)nei); // remove the node from the queue
                        nei.setTag(n.getTag()+n.getweight(nei.getKey())); // update the node tag
                        queue.add((WGraph_DS.NodeInfo)nei); // add it again to the queue in the right place according to it's new tag
                        parent.put(nei,n); // update the new way to get to it in the parent hashmap
                    }
                }
            }
        }

        if(!parent.containsKey(g.getNode(dest))) // if dest is not in the parent hashmap it means we never got to it and there is no path to it
            return null; // return null since there is no way to it
        ArrayList<node_info> ret = new ArrayList<node_info>(); // make an Arraylist
        node_info t = g.getNode(dest); //get the dest node into t
        ret.add(t); // add it to the array since it's starting point
        while(parent.get(t)!=g.getNode(src)) // as long as the next node in parent isn't src we continue
        {
            t = parent.get(t); // update the next node into t
            ret.add(t);// add it to the array
        }
        ret.add(parent.get(t)); // add the last node src to the array
        Collections.reverse(ret); //  reverse it so it would start in src and end in dest
        return ret; // return the array
    }

    /**
     *
     * @param file - the file name (may include a relative path).
     * @return true if successfully saved into a file otherwise false
     */
    @Override
    public boolean save(String file) {
        try
        {
            FileOutputStream fos = new FileOutputStream(file); // create a file output stream
            ObjectOutputStream ous = new ObjectOutputStream(fos); // create an object output stream from fos
            ous.writeObject(g);// write the g graph to the file
            fos.close(); // close the stream
            ous.close(); // close the object stream
            return true; // return true
        }
        catch (IOException e) // catch io exceptions
        {
            e.printStackTrace(); // print stack trace of what went wrong
            return false; // return false since it didn't work
        }
    }

    /**
     *
     * @param file - file name
     * @return true if successfully loaded from a file and otherwise false
     */
    @Override
    public boolean load(String file)
    {
        try
        {
            FileInputStream fis = new FileInputStream(file); // create a file input stream
            ObjectInputStream ous = new ObjectInputStream(fis); // create an object input stream using the fos
            this.g = (WGraph_DS) ous.readObject(); // load the object from the file into g
            fis.close(); // close the stream
            ous.close(); // close the object stream
            return true; // return true since it worked
        }
        catch (IOException | ClassNotFoundException e) // catch io exceptions and class not found exeptions
        {
            e.printStackTrace(); // print stack trace to see what went wrong
            return false; // return false since it didn't work
        }
    }
}
