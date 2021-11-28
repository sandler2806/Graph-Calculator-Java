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

    ArrayList<HashMap<Integer, Edge>>  adjList = new ArrayList<>();
    ArrayList<NodeData> nodes=new ArrayList<>();
    int size;

    public Digraph() throws IOException {

        String path = "data/G1.json";
        Gson g = new Gson();


        Map<String,ArrayList<Map<String,?>>> map= g.fromJson(new FileReader(path), Map.class);
        size=map.get("Edges").size();
        for (Map<String,?> x:map.get("Nodes")){
            Location l=new Location(""+x.get("pos"));
            Node node=new Node(((Double)x.get("id")).intValue(),l);
            nodes.add(node);
            adjList.add(new HashMap<Integer,Edge>());
        }
        for (Map<String,?> x:map.get("Edges")){

            int src=((Double)x.get("src")).intValue();
            int dest=((Double)x.get("dest")).intValue();
            double w= (Double) x.get("w");

            Edge edge=new Edge(src,w,dest);
            adjList.get(src).put(dest,edge);

        }
    }

    public static void main(String[] args) throws IOException {

        Digraph g = new Digraph();
        System.out.println("");
    }

    @Override
    public NodeData getNode(int key) {
        if(key<nodes.size())
            return nodes.get(key);
        else
            return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return adjList.get(src).getOrDefault(dest, null);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.add(n);
        adjList.add(new HashMap<Integer,Edge>());
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(!adjList.get(src).containsKey(dest)){
            adjList.get(src).put(dest,new Edge(src,w,dest));
            size++;
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {

//        if(adjList)
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        if(adjList.get(src).containsKey(dest)){
            Edge edge=adjList.get(src).get(dest);
            adjList.get(src).remove(dest);
            size--;
            return edge;
        }
        else
            return null;
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return size;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
