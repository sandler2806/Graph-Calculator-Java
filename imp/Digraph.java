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

    static class container{
        NodeData node;
        HashMap<Integer,EdgeData> edges;

        container(NodeData n){node = n;
        edges = new HashMap<>();
        }
    }


//    HashMap<Integer, imp.Node>  nodes = new HashMap<>();
    HashMap<Integer,container> adjList;
    int edgeNum; // nu

    //empty constructor
    public Digraph(){
        adjList=new HashMap<>();
    }

    public Digraph(String path) throws IOException {
        adjList = new HashMap<>();

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

        }
        //insert the edges
        for (Map<String,?> x:map.get("Edges")){

            int src=((Double)x.get("src")).intValue();
            int dest=((Double)x.get("dest")).intValue();
            double w= (Double) x.get("w");

            Edge edge=new Edge(src,w,dest);
            adjList.get(src).edges.put(dest,edge);

        }

    }

    public static void main(String[] args)  {

//        imp.Digraph g = new imp.Digraph("data/G1.json");
//        System.out.println("");
    }

    @Override
    public NodeData getNode(int key) {
            return adjList.get(key).node;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return adjList.get(src).edges.get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        adjList.put(n.getKey(),new container(n));
    }

    @Override
    public void connect(int src, int dest, double w) {
        adjList.get(src).edges.put(dest, new Edge(src, w, dest));
        edgeNum++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {

        ArrayList<NodeData> nodes = new ArrayList<>();
        for(container c : adjList.values())
            nodes.add(c.node);

        return nodes.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        ArrayList<EdgeData> edges = new ArrayList<>();
        for (container c : adjList.values())
            edges.addAll(c.edges.values());

        return edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {

        return adjList.get(node_id).edges.values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {

        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        EdgeData d = adjList.get(src).edges.remove(dest) ;
        if(d!=null)
            edgeNum--;

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
        return 0;
    }
}
