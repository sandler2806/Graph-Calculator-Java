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
        double[][]distance=dijkstra(src);
        return distance[0][dest];
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {

        ArrayList<NodeData>ans=new ArrayList<>();
        double[][]path=dijkstra(src);
        ans.add(graph.getNode(dest));
        double node=path[1][dest];
        while (node!=src){
            ans.add(0,graph.getNode((int)node));
            node=path[1][(int)node];
        }
        ans.add(graph.getNode(src));
        return ans;
    }

    @Override
    public NodeData center() {

        HashMap<Integer,Double>eccentricity=new HashMap<>();
        Iterator<NodeData> nodeDataIterator=graph.nodeIter();
        while (nodeDataIterator.hasNext()){
            NodeData node=nodeDataIterator.next();
            double[][]distance=dijkstra(node.getKey());
            Arrays.sort(distance[0]);
            eccentricity.put(node.getKey(),distance[0][distance[0].length-1]);
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

    double[][] dijkstra(int src){

        //creating a set with all the nodes
        Set<NodeData> vertexes = new HashSet<>();
        Iterator<NodeData> it = graph.nodeIter();
        int maxId = Integer.MIN_VALUE;

        while (it.hasNext()){
            NodeData temp = it.next();
            vertexes.add(temp);
            maxId = Math.max(temp.getKey(), maxId);
        }

        //creating 2D array to hold the distances( array[0]) and parents (array[1])
        double[][] mat = new double[2][maxId + 1];
        double[] dist = mat[0];
        double[] prev = mat[1];

        //instantiate the distance array with infinity
        Arrays.fill(dist, Double.MAX_VALUE);

        dist[src] = 0;

        while (!vertexes.isEmpty()){
            //removing the node with the smallest distance
            NodeData min = graph.getNode(getMin(dist));
            vertexes.remove(min);

            Iterator<EdgeData> minNeighbors = graph.edgeIter(min.getKey());

            while (minNeighbors.hasNext()){

                NodeData nei = graph.getNode(minNeighbors.next().getDest());
                if(!vertexes.contains(nei))
                    continue;

                double tempDist = dist[min.getKey()] + graph.getEdge(min.getKey(), nei.getKey()).getWeight();
                if(tempDist < dist[nei.getKey()]){
                    dist[nei.getKey()] = tempDist;
                    prev[nei.getKey()] = min.getKey();
                }

            }

        }

        return mat;
    }

    //return the index of the min value in array
    int getMin(double[] arr){

        double min = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < arr.length; i++){

            if (arr[i] < min){
                min = arr[i];
                index = i;
            }
        }

        return index;
    }

}