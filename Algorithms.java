import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class Algorithms implements DirectedWeightedGraphAlgorithms {

    DirectedWeightedGraph graph;

    public static void main(String[] args) throws IOException {

        Algorithms al = new Algorithms();
        Digraph g = new Digraph("data/G1.json");
        al.init(g);
        DirectedWeightedGraph d = al.copy();
        g.nodes.get(1).addEdge(new Edge(1,3,4));
        System.out.println("");


    }

    @Override
    public void init(DirectedWeightedGraph g) {

        graph = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    @Override
    public DirectedWeightedGraph copy() {

        String g ;
        Gson gson = new Gson();
        g = gson.toJson(graph);

        Digraph d = gson.fromJson(g, Digraph.class);
        return d;
    }

    @Override
    public boolean isConnected() {
        return graph.edgeSize()==graph.nodeSize()*(graph.nodeSize()-1);
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
