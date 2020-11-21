package ex1.src;


import java.util.Comparator;
/**
 * This class is an object for comparison between NodeInfo instances,
 * it compares them according to their tags. it inherits from the interface Comparator.*/
public class NodeInfoComparator implements Comparator<WGraph_DS.NodeInfo>
{
    @Override
    public int compare(WGraph_DS.NodeInfo o1, WGraph_DS.NodeInfo o2) // override the compare function
    {
        if (o1.getTag() == o2.getTag()) // if the tags equal return 0
            return 0;
        if (o1.getTag()>o2.getTag()) // if one tag is bigger return 1
            return 1;
        else // otherwise return -1
            return -1;
    }
}
