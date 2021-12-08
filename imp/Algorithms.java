package imp;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
        DirectedWeightedGraph reverse=reverse(graph);
        distance=bfs(reverse,src);
        return !distance.containsValue(Double.MAX_VALUE);
    }
    private DirectedWeightedGraph reverse(DirectedWeightedGraph g){
        DirectedWeightedGraph r=new Digraph();
        Iterator<NodeData> nodeDataIterator=g.nodeIter();
        while (nodeDataIterator.hasNext()){
            NodeData nodeData=nodeDataIterator.next();
            r.addNode(nodeData);
        }
        Iterator<EdgeData> edgeDataIterator=g.edgeIter();
        while (edgeDataIterator.hasNext()){
            EdgeData edgeData=edgeDataIterator.next();
            r.connect(edgeData.getDest(),edgeData.getSrc(),edgeData.getWeight());
        }
        return r;
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
            Iterator<Edge> edgeDataIterator=g.edgeIter(node.getKey());
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
        Dijkstra D = new Dijkstra(graph);
        HashMap<Integer,Double>[]dijkstra=D.shp(src);
        HashMap<Integer,Double>distance =dijkstra[0];
        return distance.get(dest);
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Dijkstra D = new Dijkstra(graph);
        ArrayList<NodeData>ans=new ArrayList<>();
        HashMap<Integer,Double>[]dijkstra=D.shp(src);
        HashMap<Integer,Double>path =dijkstra[1];
        ans.add(graph.getNode(dest));
        double node=path.get(dest);
        while (node!=src){
            ans.add(0,graph.getNode((int)node));
            node=path.get((int)node);
        }
        ans.add(0,graph.getNode(src));
        return ans;
    }

    @Override
    public NodeData center() {

        HashMap<Integer,Double>eccentricity=new HashMap<>();
        Iterator<NodeData> nodeDataIterator=graph.nodeIter();
        Dijkstra D = new Dijkstra(graph);
        while (nodeDataIterator.hasNext()){
            NodeData node=nodeDataIterator.next();
            HashMap<Integer,Double>[]dijkstra=D.shp(node.getKey());
            HashMap<Integer,Double> distance=dijkstra[0];

            double max=Double.MIN_VALUE;
            for(int i:distance.keySet()){
                if(distance.get(i)>max)max=distance.get(i);
            }
            eccentricity.put(node.getKey(),max);
        }
        double minEccentricity=Double.MAX_VALUE;
        int index=0;
        for (int x:eccentricity.keySet()){
            if(eccentricity.get(x)<minEccentricity)
            {
                minEccentricity=eccentricity.get(x);
                index=x;
            }
        }
        return graph.getNode(index);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        Dijkstra D = new Dijkstra(graph);
        HashMap<Integer,NodeData>citiesMap=new HashMap<>();
        List<NodeData>ans=new ArrayList<>();
        for (NodeData nodeData:cities){
            citiesMap.put(nodeData.getKey(),nodeData);
        }
        NodeData src=cities.get(0);
        ans.add(src);
        citiesMap.remove(src.getKey());
        while (!citiesMap.isEmpty()){
            Iterator<Edge> edgeDataIterator=graph.edgeIter(src.getKey());
            NodeData minNei=null;
            double minWeight=Double.MAX_VALUE;
            while (edgeDataIterator.hasNext()){
                EdgeData currentEdge=edgeDataIterator.next();
                NodeData currentNode=graph.getNode(currentEdge.getDest());
                if(citiesMap.containsKey(currentNode.getKey())&&currentEdge.getWeight()<minWeight){
                    minWeight=currentEdge.getWeight();
                    minNei=currentNode;
                }
            }
            if(minNei!=null){
                ans.add(minNei);
                citiesMap.remove(minNei.getKey());
                src=minNei;
            }
            else {
                HashMap<Integer,Double>[]dijkstra=D.shp(src.getKey());
                HashMap<Integer,Double> distance=dijkstra[0];
                HashMap<Integer,Double> path=dijkstra[1];
                for (NodeData nodeData:citiesMap.values()){
                    if(distance.get(nodeData.getKey())<minWeight){
                        minNei=nodeData;
                        minWeight=distance.get(nodeData.getKey());
                    }
                }
                ans.add(graph.getNode((minNei.getKey())));
                citiesMap.remove(minNei.getKey());
                double node=path.get((minNei.getKey()));
                int size=ans.size()-1;
                while (node!=src.getKey()){
                    ans.add(size,graph.getNode((int)node));
                    citiesMap.remove((int)node);
                    node=path.get((int)node);
                }
                src=minNei;
            }
        }
        return ans;
    }

    @Override
    public boolean save(String file) {

        Gson gson = new GsonBuilder().create();

        try {
            Writer writer = new FileWriter(file);
            gson.toJson(graph, writer);
            writer.flush(); //flush data to file   <---
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean load(String file) {

        Gson g = new Gson();
        try {
            Digraph d =new Digraph(file);
            this.init(d);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

}