package ex1;



import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable
{
    private WGraph_DS g;

    public WGraph_Algo()
    {
        this.g = null;
    }
    public WGraph_Algo(WGraph_DS g)
    {
        init(g);
    }

    @Override
    public void init(weighted_graph g)
    {
        this.g = (WGraph_DS) g;
    }


    @Override
    public weighted_graph getGraph() {
        return g;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(g);
    }

    @Override
    public boolean isConnected() {
        // can be done using bfs only
        Iterator<node_info> iter =  g.getV().iterator();
        node_info n;
        if (iter.hasNext())
            n=iter.next();
        else
            return true;

        for(node_info node :g.getV())
        {
            node.setTag(0);
        }
        n.setTag(1);
        LinkedList<WGraph_DS.NodeInfo> q = new LinkedList<WGraph_DS.NodeInfo>();
        q.add((WGraph_DS.NodeInfo) n);
        int count = 1;
        while(!q.isEmpty())
        {
            node_info current = q.poll();
            for(node_info node: g.getV(current.getKey()))
            {
                if(node.getTag()==0)
                {
                    count++;
                    node.setTag(1);
                    q.add((WGraph_DS.NodeInfo) node);
                }
            }
        }
        return count == g.getV().size();
    }

    @Override
    public double shortestPathDist(int src, int dest)
    {
        //check if null
        if (src==dest)
            return 0;
        if(g.getNode(src) == null || g.getNode(dest) == null)
            return -1;
        // make a priority queue
        PriorityQueue<WGraph_DS.NodeInfo> queue = new PriorityQueue<WGraph_DS.NodeInfo>(g.getV().size(),new NodeInfoComparator());

        // put infinity in all tags
        for(node_info n: g.getV())
        {
            n.setTag(-1);
        }

        WGraph_DS.NodeInfo srcnode =(WGraph_DS.NodeInfo) g.getNode(src);

        // add src to queue
        srcnode.setTag(0);
        queue.add(srcnode);
        //start looping
        while(!queue.isEmpty())
        {
            WGraph_DS.NodeInfo n = queue.poll();
            for(node_info ni: n.getNi().values())
            {
                if (ni.getTag()==-1) // never been to this node in my life
                {
                    ni.setTag(n.getTag()+ n.getweight(ni.getKey()));
                    queue.add((WGraph_DS.NodeInfo) ni);
                }
                if (ni.getTag()>=0 && ni.getTag()>n.getweight(ni.getKey())+n.getTag()) // already been here might as well update it in the queue
                {
                    ni.setTag(n.getweight(ni.getKey())+n.getTag());
                    queue.remove(ni);
                    queue.add((WGraph_DS.NodeInfo) ni);
                }
            }
        }

        node_info d = g.getNode(dest);
        if (d.getTag()==-1)
            return -1;
        return d.getTag();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (g.getNode(src) == null || g.getNode(dest)==null)
            return null;
        if(src == dest)
        {
            ArrayList<node_info> arr = new ArrayList<node_info>();
            arr.add(g.getNode(src));
            return arr;
        }
        for(node_info n :g.getV())
        {
            n.setTag(-1);
        }

        PriorityQueue<WGraph_DS.NodeInfo> queue = new PriorityQueue<WGraph_DS.NodeInfo>(g.getV().size(),new NodeInfoComparator());
        HashMap<node_info,node_info> parent = new HashMap<node_info,node_info>();
        WGraph_DS.NodeInfo s = (WGraph_DS.NodeInfo) g.getNode(src);
        s.setTag(0);
        queue.add(s);
        while(!queue.isEmpty())
        {
            WGraph_DS.NodeInfo n = queue.poll();
            for (node_info nei:n.getNi().values())
            {
                if (nei.getTag()==-1)
                {
                    nei.setTag(n.getTag()+n.getweight(nei.getKey()));
                    queue.add((WGraph_DS.NodeInfo)nei);
                    parent.put(nei,n);
                }
                if (nei.getTag()>=0)
                {
                    if (n.getTag()+n.getweight(nei.getKey())<nei.getTag())
                    {
                        queue.remove((WGraph_DS.NodeInfo)nei);
                        nei.setTag(n.getTag()+n.getweight(nei.getKey()));
                        queue.add((WGraph_DS.NodeInfo)nei);
                        parent.put(nei,n);
                    }
                }

            }
        }
        if(!parent.containsKey(g.getNode(dest)))
            return null;
        ArrayList<node_info> ret = new ArrayList<node_info>();
        node_info t = g.getNode(dest);
        ret.add(t);
        while(parent.get(t)!=g.getNode(src))
        {
            t = parent.get(t);
            ret.add(t);
        }
        ret.add(parent.get(t));
        Collections.reverse(ret);
        return ret;
    }

    @Override
    public boolean save(String file) {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(g);
            fos.close();
            ous.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file)
    {
        try
        {
            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream ous = new ObjectInputStream(fos);
            this.g = (WGraph_DS) ous.readObject();
            fos.close();
            ous.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
