package imp;

import api.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Algorithms implements DirectedWeightedGraphAlgorithms {

    DirectedWeightedGraph graph;
    final int WHITE=0,GRAY=1,BLACK=2;

    /**
     *
     * @param g - the graph we want to assign to the algorithms
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        graph = g;
    }

    /**
     *
     * @return the graph we operate on
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    /**
     * @return a deep copy of the graph
     */
    @Override
    public DirectedWeightedGraph copy() {

        save("graph");
        Digraph d=null;
        try {
            d=new Digraph("graph");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * check if the graph is strongly connected
     * @return true if it does, false if not
     */
    @Override
    public boolean isConnected() {
        //connected graph must have at least n-1 edges
        if(graph.edgeSize()<graph.nodeSize()-1)return false;

        //getting iterator of the nodes
        Iterator<NodeData> iterator = graph.nodeIter();
        NodeData src=iterator.next();
        //calling to BFS - first time
        HashMap<Integer,Double> distance = bfs(graph,src);
        //checking for unvisited nodes
        if(distance.containsValue(Double.MAX_VALUE)) return false;
        //reversing the graph, and calling BFS again
        DirectedWeightedGraph reverse=reverse(graph);
        distance=bfs(reverse,src);
        //if again, non of the nodes are white we return true
        return !distance.containsValue(Double.MAX_VALUE);
    }

    /**
     *
     * @param g - graph
     * @param src - the vertex to start the algorithm from
     * @return array of two hashmaps, the first containing the distances
     * and the second the previous nodes
     */
    private HashMap<Integer,Double> bfs(DirectedWeightedGraph g,NodeData src){
        Iterator<NodeData> iterator=g.nodeIter();
        HashMap<Integer,Double> distance=new HashMap<>();
        //Color all the nodes of the graph with white
        while (iterator.hasNext()){
            NodeData n=iterator.next();
            n.setTag(WHITE);
            distance.put(n.getKey(),Double.MAX_VALUE);
        }
        //choose first node
        src.setTag(GRAY);
        distance.replace(src.getKey(),0.0);
        Queue<NodeData> queue=new LinkedList<>();
        queue.add(src);

        while (!queue.isEmpty()){
            NodeData node=queue.poll();
            Iterator<EdgeData> edgeDataIterator=g.edgeIter(node.getKey());
            //iterate over the node's neighbours
            while (edgeDataIterator.hasNext()){
                EdgeData edge=edgeDataIterator.next();
                NodeData adj=g.getNode(edge.getDest());
                //if the neighbour is white
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

    /**
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return the distance
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Dijkstra D = new Dijkstra(graph);
        HashMap<Integer,Double>[]dijkstra=D.shp(src);
        HashMap<Integer,Double>distance =dijkstra[0];
        return distance.get(dest);
    }

    /**
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Dijkstra D = new Dijkstra(graph);
        ArrayList<NodeData>ans=new ArrayList<>();

        HashMap<Integer,Double>[]dijkstra=D.shp(src);
        HashMap<Integer,Double>path =dijkstra[1];
        ans.add(graph.getNode(dest));
        double node=path.get(dest);

        //build the path from the "previous node" array returned from Dijkstra
        while (node!=src){
            ans.add(0,graph.getNode((int)node));
            node=path.get((int)node);
        }
        ans.add(0,graph.getNode(src));
        return ans;
    }

    /**
     *
     * @return the central vertex
     */
    @Override
    public NodeData center() {
        //make sure first that the graph is connected
        if(!isConnected()) return null;

        HashMap<Integer,Double>eccentricity=new HashMap<>();
        Iterator<NodeData> nodeDataIterator=graph.nodeIter();
        Dijkstra D = new Dijkstra(graph);
        //calculating the eccentricity of each node
        HashMap<Integer,Double>[]dijkstra;
        HashMap<Integer,Double> distance;
        //
        while (nodeDataIterator.hasNext()){
            NodeData node=nodeDataIterator.next();
            dijkstra=D.shp(node.getKey());
            distance=dijkstra[0];

            double max=Double.MIN_VALUE;
            for(int i:distance.keySet()){
                if(distance.get(i)>max)max=distance.get(i);
            }
            eccentricity.put(node.getKey(),max);
        }
        //finding the minimum eccentricity
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

    /**
     *
     * @param cities - List of nodes to visit
     * @return a path containing all the nodes in cities
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        HashMap<Integer,HashMap<Integer,Double>[]>dijkstraMap=new HashMap<>();
        Dijkstra D = new Dijkstra(graph);
        double dist,minDist=Double.MAX_VALUE;
        List<NodeData>minAns=new ArrayList<>();
        HashMap<Integer,NodeData>citiesMap=new HashMap<>();

        //we construct several paths, each time starting from different node in cities
        for (NodeData src:cities){
            //we add all the cities to temp hashmap
            for (NodeData nodeData:cities){
                citiesMap.put(nodeData.getKey(),nodeData);
            }
            //
            dist=0;
            List<NodeData>ans=new ArrayList<>();
            ans.add(src);
            citiesMap.remove(src.getKey());
            //when we visit a city we remove it from the temp list
            while (!citiesMap.isEmpty()){
                NodeData minNei=null;
                double minWeight=Double.MAX_VALUE;

                HashMap<Integer,Double>[] dijkstra;
                //
                if(dijkstraMap.containsKey(src.getKey())){
                    dijkstra=dijkstraMap.get(src.getKey());
                }
                else {
                    dijkstra=D.shp(src.getKey());
                    dijkstraMap.put(src.getKey(),dijkstra);
                }
                HashMap<Integer,Double> distance=dijkstra[0];
                HashMap<Integer,Double> path=dijkstra[1];
                for (NodeData nodeData:citiesMap.values()){
                    if(distance.get(nodeData.getKey())<minWeight){
                        minNei=nodeData;
                        minWeight=distance.get(nodeData.getKey());
                    }
                }
                if(minNei==null)return null;
                dist+=distance.get(minNei.getKey());
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
            if(dist<minDist){
                minAns=ans;
                minDist=dist;
            }
        }

        return minAns;
    }

    /**
     * @param file - the file name (may include a relative path).
     * @return true if the file was saved successfully
     */
    @Override
    public boolean save(String file) {

        try {
            Writer writer = new FileWriter(file);
            Iterator<NodeData>nodeDataIterator=graph.nodeIter();
            Iterator<EdgeData>edgeDataIterator=graph.edgeIter();
            StringBuilder str= new StringBuilder();
            str.append("{\n" + "\"Edges\": [");
            boolean first=true;
            while (edgeDataIterator.hasNext()) {
                EdgeData edgeData = edgeDataIterator.next();
                if (first){
                    str.append("{\n" + "\"src\": ").append(edgeData.getSrc()).append(",\n").append("\"w\":").append(edgeData.getWeight()).append(",\n").append("\"dest\":").append(edgeData.getDest()).append(" \n").append("}");
                }
                else {
                    str.append(",{\n" + "\"src\": ").append(edgeData.getSrc()).append(",\n").append("\"w\":").append(edgeData.getWeight()).append(",\n").append("\"dest\":").append(edgeData.getDest()).append(" \n").append("}");
                }
                first=false;
            }
            str.append("],\n\"Nodes\":[\n");
            first=true;
            while (nodeDataIterator.hasNext()){
                NodeData nodeData = nodeDataIterator.next();
                GeoLocation l=nodeData.getLocation();
                if(first){
                    str.append("{\n\"pos\":\"").append(l.x()).append(",").append(l.y()).append(",0.0\",\"id\":").append(nodeData.getKey()).append("\n}");

                }
                else {
                    str.append(",{\n\"pos\":\"").append(l.x()).append(",").append(l.y()).append(",0.0\",\"id\":").append(nodeData.getKey()).append("\n}");
                }
                first=false;
            }
            str.append("]}");

            writer.write(str.toString());
            writer.flush(); //flush data to file
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param file - file name of JSON file
     * @return true if the file was read successfully
     */
    @Override
    public boolean load(String file) {

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

    /**
     * @param g - a Directed graph
     * @return
     */
    private DirectedWeightedGraph reverse(DirectedWeightedGraph g){
        DirectedWeightedGraph r=new Digraph();
        //adding all the nodes to the new graph
        Iterator<NodeData> nodeDataIterator=g.nodeIter();
        while (nodeDataIterator.hasNext()){
            NodeData nodeData=nodeDataIterator.next();
            r.addNode(nodeData);
        }
        //adding a reverse copy of each edge to the new graph
        Iterator<EdgeData> edgeDataIterator=g.edgeIter();
        while (edgeDataIterator.hasNext()){
            EdgeData edgeData=edgeDataIterator.next();
            r.connect(edgeData.getDest(),edgeData.getSrc(),edgeData.getWeight());
        }
        return r;

    }

}