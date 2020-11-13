package ex1;

import java.util.Comparator;

public class NodeInfoComparator implements Comparator<WGraph_DS.NodeInfo>
{

    @Override
    public int compare(WGraph_DS.NodeInfo o1, WGraph_DS.NodeInfo o2)
    {
        if (o1.getTag() == o2.getTag())
            return 0;
        if (o1.getTag()>o2.getTag())
            return 1;
        else
            return -1;
    }
}
