import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

public class Algorithms implements DirectedWeightedGraphAlgorithms {

    DirectedWeightedGraph graph;
    final int WHITE=0,GRAY=1,BLACK=2;

    public static void main(String[] args) throws IOException {


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
        if(graph.edgeSize()<graph.nodeSize()-1)return false;

        Iterator<NodeData> iterator=graph.nodeIter();
        NodeData src=iterator.next();
        HashMap<Integer,Double> distance=bfs(graph,src);
        if(distance.containsValue(Double.MAX_VALUE))return false;
        DirectedWeightedGraph r=reverse(graph);
        return true;
    }

    private DirectedWeightedGraph reverse(DirectedWeightedGraph graph) {
        return null;

    }

    private HashMap<Integer,Double> bfs(DirectedWeightedGraph g,NodeData src){
        Iterator<NodeData> iterator=g.nodeIter();
        HashMap<Integer,Double> distance=new HashMap<>();
        //color to white and dist to max
        while (iterator.hasNext()){
            NodeData n=iterator.next();
            n.setTag(WHITE);
            distance.put(n.getKey(),Double.MAX_VALUE);
        }
        //chose first node
        src.setTag(GRAY);
        distance.replace(src.getKey(),0.0);
        Queue<NodeData> queue=new LinkedList<>();
        queue.add(src);

        while (!queue.isEmpty()){
            NodeData node=queue.poll();
            Iterator<EdgeData> edgeDataIterator=g.edgeIter(node.getKey());
            //iterate all his adj
            while (edgeDataIterator.hasNext()){
                EdgeData edge=edgeDataIterator.next();
                NodeData adj=g.getNode(edge.getDest());
                //if the adj is white
                if(adj.getTag()==WHITE){
                    adj.setTag(GRAY);
                    distance.replace(adj.getKey(),distance.get(node.getKey())+edge.getWeight());
                    queue.add(adj);
                }
            }
            node.setTag(BLACK);
        }
        return distance;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Iterator<NodeData> iterator=graph.nodeIter();
        HashMap<Integer,Double> distance=new HashMap<>();
        //color to white and dist to max
        while (iterator.hasNext()){
            NodeData nodeData=iterator.next();
            nodeData.setTag(WHITE);
            distance.put(nodeData.getKey(),Double.MAX_VALUE);
        }



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