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

    ArrayList<HashMap<String, Edge>>  adjList = new ArrayList<>();
    ArrayList<Node> nodes=new ArrayList<>();

    public Digraph() throws IOException {

        String path = "data/G1.json";
        Gson g = new Gson();


        Map<String,ArrayList<Map<String,?>>> map= g.fromJson(new FileReader(path), Map.class);
        for (Map<String,?> x:map.get("Nodes")){
            Location l=new Location(""+x.get("pos"));
            Node node=new Node(((Double)x.get("id")).intValue(),l);
            nodes.add(node);
            adjList.add(new HashMap<String,Edge>());
        }
        for (Map<String,?> x:map.get("Edges")){

            int src=((Double)x.get("src")).intValue();
            int dest=((Double)x.get("dest")).intValue();
            double w= (Double) x.get("w");

            Edge edge=new Edge(src,w,dest);
            adjList.get(src).put(""+dest,edge);

        }
    }

    public static void main(String[] args) throws IOException {

        Digraph g = new Digraph();
        System.out.println("");
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(NodeData n) {

    }

    @Override
    public void connect(int src, int dest, double w) {

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
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
