This project main goal is to achieve a model of a weighted graph,
and do all kinds of operations on it like check connectivity, best path from src to dest and etc...
I used tools that were already built in the java language like a hashmap and a priority queue, just to make things simpler,
and so I could focus on the implementation of the model.

The interfaces that were used are:
1. node_info
2. weighted_graph
3. weighted_graph_algorithms
-------------------------------------------
The classes are:
1. WGraph_DS
2. NodeInfo (which is an inner class in the WGraph_DS class)
3. WGraph_Algo
4. NodeInfoComparator
-------------------------------------------
Test classes files are:
1. WGraph_AlgoTest
2. WGraph_DSTest
3. WGraph_DSUtility - a class that is containing
-------------------------------------------

NodeInfo class:
This class represents a node in the graph.
since it's am inner class in WGraph_DS it uses some of the fields in it, like
the usedkeys hashset, numberofnodes and stuff like this.

Fields in the class:
1. int key - is a unique key of the node
2. double tag - is a value that represent the weight to the node from the src.
3. String info - some information in string form about the node
4. hashmap neighbors - is a hashmap that takes a key and gives a node back
5. hashmap neighborsweights - is a hashmap that takes a key of a node and gives the weight from this node to the other.

Constructors:
1. NodeInfo which takes a key a tag and info, sets them, and add the key to the usedkeys hashset

methods in the class (not from interface):
1. equals - takes an object o and tests if it equals to this node object
2. getNi - return the neighbors hashmap
3. getNiWeights - return the neighbors weights hashmap
4. setKey - is a private method since changing the key shouldn't be possible outside the class, and all it does is set a new key
5. addNi - takes a neighbor of node_info and weight of double and connect them and update the weights.
6. removeNi - remove a neighbor from the neighbor list and the weight from the hashmap
7. removeNi- the same as above but takes an int key instead of an object
8. hasNi- check if this node is a neighbor of another node
9. hasNi - overload of the above but with int key instead of an object
10. getweight - get the weight between this node and a different node.
11. updateWeight - update the weight between this node and a different node
12. destroyConnections - disconnect this node from all it's neighbors
13. toString - return a string representation of a node

-------------------------------------------
WGraph_Ds class:
This class is the model for graph it-self, and it has an inner class the NodeInfo class.
That means that every graph have it's own nodes and since the uniqueness of the keys,
one graph can't have two nodes with same key but to different graphs can.

Fields in the class:
1. hashset usedkeys - every key that was already used is in the used keys hash set
2. numofnodes - keep track on how many nodes were made for this graph
3. hashmap nodes - all the nodes that are actually in the graph is in this hashmap. it maps keys to node objects
4. edgesize - keep track on the amount of edges in the graph
5. MC - mode count, keeps track on how many changes were made in the graph.

Constructors:
1. default constructor - creates an empty graph with no nodes.
2. copy constructor - takes a graph g and copy all the nodes with connections to one another with weight and copy it to this grpah

methods in the class (not from interface):
1. toString - creates a string representation of the graph
2. equals - take another object o and say if it's equal to this graph.

-------------------------------------------

WGraph_Algo class:
This class is like a black box object which you insert a graph into, and you can use it
to do some kind of operations like save the graph to a file load it from a file check connectivity and such...

Fields in the class:
1. graph g - a graph instance called g

Constructors:
1. default constructor - puts null inside g and does nothing
2. constructor that takes a graph and put it in g

methods in the class (not from interface):
all methods in the class are from the interface

--------------------------------------------
WGraph_DSUtility class:
provide a graph creating function just to make things easier in the tests.

Fields in the class:
1. _rnd -which is a random object

Constructors:
There are no constructor since this class is being used in a static way only

Methods :
1. graphCreator - creates a graph with known node size and edge size with random weights.









