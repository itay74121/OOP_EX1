package ex1.Tests;


import ex1.WGraph_DS;
import ex1.node_info;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public final class WGraph_DSUtility
{
    private static Random _rnd = null;

    private WGraph_DSUtility()
    {
        // no contructor
    }

    public static boolean writetofile(String filename, String text)
    {
        try {
            File f = new File(filename);
            if (f.exists()&&f.canWrite())
            {
                FileWriter myWriter = new FileWriter(f, true);
                myWriter.write(text+"\n");
                myWriter.close();
            }
            else
            {
                f.createNewFile();
                FileWriter myWriter = new FileWriter(f, true);
                myWriter.write(text+"\n");
                myWriter.close();
            }
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }

    }

    public static WGraph_DS graph_creator(int v_size, int e_size, int seed)
    {
        String path = "c:/users/itay/ideaprojects/OOP_EX1/src/ex1/log.txt";
        File f = new File(path);
        if(f.exists())
            f.delete();

        WGraph_DS g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {

            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int c = nextRnd(0,1000);
            writetofile(path,a+" "+b+" "+c);
            int i = nodes[a];
            int j = nodes[b];
            g.connect(i,j,c);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(WGraph_DS g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

}
