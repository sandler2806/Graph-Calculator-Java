import api.GeoLocation;
import api.NodeData;

import java.util.HashMap;

public class Node implements NodeData {

    private int id;
    private Location pos;
    private int tag;
    private HashMap<Integer, Edge>  adjList;

    public Node(int id, Location gl){

        this.id = id;
        pos = gl;
        adjList=new HashMap<>();

    }



    public void addEdge(Edge edge) {
        adjList.put(edge.dest,edge);
    }

    public Edge removeEdge(int dest) {

        if(adjList.containsKey(dest)){
            Edge edge=adjList.get(dest);
            adjList.remove(dest);
            return edge;
        }
        else
            return null;
    }

    public Edge getEdge(int dest) {
        return adjList.getOrDefault(dest, null);
    }

    public boolean containEdge(int dest) {
        return adjList.containsKey(dest);
    }



    @Override
    public int getKey() {
        return id;
    }

    @Override
    public GeoLocation getLocation() {
        return pos;
    }

    @Override
    public void setLocation(GeoLocation p) {
        pos = (Location) p;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {

        tag = t;
    }
}
