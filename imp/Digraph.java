package imp;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Digraph implements DirectedWeightedGraph {

    //class that hold all the details about a specific node.
    static class container{
        Node node;
        HashMap<Integer,EdgeData> outEdges,inEdges;
        container(NodeData n){
            node = (Node) n;
        outEdges = new HashMap<>();
        inEdges = new HashMap<>();

        }
    }

    int mc;//count the number of changes that was made to the graph

    private final HashMap<String,EdgeData> edges;
    private final HashMap<Integer,NodeData> nodes;
    private final HashMap<Integer,container> adjList; // adjacency list
    int edgeNum;

    //empty constructor
    public Digraph(){
        mc = 0;
        adjList=new HashMap<>();
        edges=new HashMap<>();
        nodes=new HashMap<>();
    }

    //constructor that receives path to json file containing a graph
    public Digraph(String path) throws IOException {
        mc = 0;
        adjList = new HashMap<>();
        edges=new HashMap<>();
        nodes=new HashMap<>();

        Gson g = new Gson();
        //read the json file into map
        Map<String,ArrayList<Map<String,?>>> map= g.fromJson(new FileReader(path), Map.class);
        edgeNum=map.get("Edges").size();

        //insert the nodes to the hashmap
        for (Map<String,?> x:map.get("Nodes")){
            int id=((Double)x.get("id")).intValue();
            Location l=new Location(""+x.get("pos"));
            Node node=new Node(id,l);
            adjList.put(id,new container(node));
            nodes.put(node.getKey(),node);

        }
        //insert the edges
        for (Map<String,?> x:map.get("Edges")){

            int src=((Double)x.get("src")).intValue();
            int dest=((Double)x.get("dest")).intValue();
            double w= (Double) x.get("w");

            EdgeData edge=new Edge(src,w,dest);
            //add to source node
            adjList.get(src).outEdges.put(dest,edge);
            //add to the destination node
            adjList.get(dest).inEdges.put(src,edge);
            edges.put(src+","+dest,edge);
        }

    }

    /**
     *
     * @param key - the node_id
     * @return reference to node, null if not found
     */
    @Override
    public NodeData getNode(int key) {
        if(adjList.get(key) == null)
            return null;

        return adjList.get(key).node;
    }

    /**
     *
     * @param src - id of source node
     * @param dest id of destination node
     * @return reference to the Edge object, return null if Edge does not exist
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        if(adjList.get(src) == null)
            return null;

        return adjList.get(src).outEdges.get(dest);
    }

    /**
     * @param n - a node object
     */
    @Override
    public void addNode(NodeData n) {

        adjList.put(n.getKey(),new container(n));
        nodes.put(n.getKey(),n);
        mc++;
    }

    /**
     *
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(!adjList.containsKey(src) || !adjList.containsKey(dest))
            return;

        adjList.get(src).outEdges.put(dest, new Edge(src, w, dest));
        edges.put(src+","+dest,new Edge(src, w, dest));
        edgeNum++;
        mc++;

    }

    /**
     * @return iterator containing all the nodes in the graph
     * @throws RuntimeException
     */
    @Override
    public Iterator<NodeData> nodeIter()throws RuntimeException {

        return nodes.values().iterator();
    }

    /**
     * @return iterator containing all the Edges in the graph
     * @throws RuntimeException
     */
    @Override
    public Iterator<EdgeData> edgeIter() throws RuntimeException{

        return edges.values().iterator();
    }

    /**
     * @param node_id  -
     * @return Iterator that iterate over all the Edges of the given node
     * @throws RuntimeException
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id)throws RuntimeException {

        return adjList.get(node_id).outEdges.values().iterator();
    }

    /**
     *
     * @param key - the id of the node we want to remove
     * @return the node that was removed, null if non removed
     */
    @Override
    public NodeData removeNode(int key) {

        NodeData n = adjList.get(key).node;
        //remove the out edges
        ArrayList<Integer> out = new ArrayList<>(adjList.get(key).outEdges.keySet());
        ArrayList<Integer> in = new ArrayList<>(adjList.get(key).inEdges.keySet());

        for (int i: out)
            removeEdge(key,i);
        for (int i: in)
            removeEdge(i,key);
        //remove in edges

        adjList.remove(key);
        nodes.remove(key);
        mc++;
        return n;
    }

    /**
     *
     * @param src - the id of the source node
     * @param dest - the id of the destination node
     * @return the removed edge, null if non was removed
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {

        EdgeData d = adjList.get(src).outEdges.remove(dest) ;
        adjList.get(dest).inEdges.remove(src);
        edges.remove(src+","+dest);
        if(d!=null){
            edgeNum--;
            mc++;
        }
        return d;
    }

    @Override
    public int nodeSize() {
        return adjList.size();
    }

    @Override
    public int edgeSize() {
        return edgeNum;
    }

    @Override
    public int getMC() {
        return mc;
    }

}
