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

    int mc;

    static class container{
        Node node;
        HashMap<Integer,EdgeData> outEdges,inEdges;
//        HashMap<Integer>

        container(NodeData n){
            node = (Node) n;
        outEdges = new HashMap<>();
        inEdges = new HashMap<>();
        }
    }

    private HashMap<Integer,container> adjList;
    int edgeNum; // nu

    //empty constructor
    public Digraph(){
        mc = 0;
        adjList=new HashMap<>();
    }

    public Digraph(String path) throws IOException {
        mc = 0;
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
            //add to source node
            adjList.get(src).outEdges.put(dest,edge);
            //add to the destination node
            adjList.get(dest).inEdges.put(src,edge);


        }

    }

    @Override
    public NodeData getNode(int key) {
        if(adjList.get(key) == null)
            return null;

        return adjList.get(key).node;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        if(adjList.get(src) == null)
            return null;

        return adjList.get(src).outEdges.get(dest);
    }

    @Override
    public void addNode(NodeData n) {

        adjList.put(n.getKey(),new container(n));
        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(!adjList.containsKey(src) || !adjList.containsKey(dest))
            return;

        adjList.get(src).outEdges.put(dest, new Edge(src, w, dest));
        edgeNum++;
        mc++;

    }

    @Override
    public Iterator<NodeData> nodeIter()throws RuntimeException {

        ArrayList<NodeData> nodes = new ArrayList<>();
        for(container c : adjList.values())
            nodes.add(c.node);

        return nodes.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() throws RuntimeException{
        ArrayList<EdgeData> outEdges = new ArrayList<>();
        for (container c : adjList.values())
            outEdges.addAll(c.outEdges.values());

        return outEdges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id)throws RuntimeException {

        return adjList.get(node_id).outEdges.values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {

        //remove the out edges
        ArrayList<Integer> out = new ArrayList<>(adjList.get(key).outEdges.keySet());
        ArrayList<Integer> in = new ArrayList<>(adjList.get(key).inEdges.keySet());

        for (int i: out)
            removeEdge(key,i);
        for (int i: in)
            removeEdge(i,key);
        //remove in edges

        adjList.remove(key);
        mc++;
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        EdgeData d = adjList.get(src).outEdges.remove(dest) ;
        adjList.get(dest).inEdges.remove(src);
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

    public HashMap<Integer,container> getAdjList(){return adjList;}


}
