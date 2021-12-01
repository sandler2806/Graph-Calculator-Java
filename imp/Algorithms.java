package imp;

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
        HashMap<Integer,Double>[]dijkstra=dijkstra(src);
        HashMap<Integer,Double>distance =dijkstra[0];
        return distance.get(dest);
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {

        ArrayList<NodeData>ans=new ArrayList<>();
        HashMap<Integer,Double>[]dijkstra=dijkstra(src);
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
        while (nodeDataIterator.hasNext()){
            NodeData node=nodeDataIterator.next();
            HashMap<Integer,Double>[]dijkstra=dijkstra(node.getKey());
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

    public HashMap<Integer,Double>[] dijkstra(int src){

        Set<NodeData> vertexes = new HashSet<>();
        Iterator<NodeData> it = graph.nodeIter();
        HashMap<Integer,Double>dist=new HashMap<>();
        HashMap<Integer,Double>prev=new HashMap<>();

        while (it.hasNext()){
            NodeData temp = it.next();
            vertexes.add(temp);
            dist.put(temp.getKey(),Double.MAX_VALUE);
            prev.put(temp.getKey(),(double)temp.getKey());
        }

        dist.replace(src,0.0);

        while (!vertexes.isEmpty()){
            //removing the node with the smallest distance
            int a=getMin(vertexes,dist);
            NodeData min = graph.getNode(getMin(vertexes,dist));
            vertexes.remove(graph.getNode(min.getKey()));

            Iterator<EdgeData> minNeighbors = graph.edgeIter(min.getKey());

            while (minNeighbors.hasNext()){

                NodeData nei = graph.getNode(minNeighbors.next().getDest());
                if(!vertexes.contains(nei))
                    continue;

                double tempDist = dist.get(min.getKey()) + graph.getEdge(min.getKey(), nei.getKey()).getWeight();
                if(tempDist < dist.get(nei.getKey())){
                    dist.replace(nei.getKey(),tempDist);
                    prev.replace(nei.getKey(),(double)min.getKey());
                }

            }

        }
        int[]a=new int[2];
        HashMap<Integer,Double>[]ans=new HashMap[2];
        ans[0]=dist;
        ans[1]=prev;
        return ans;
    }

    //return the index of the min value in array
    int getMin(Set<NodeData> s, HashMap<Integer,Double> dist){

        double min = Double.MAX_VALUE;
        int index = 0;
        for (NodeData nodeData:s){
            if(dist.get(nodeData.getKey())<min){
                min = dist.get(nodeData.getKey());
                index=nodeData.getKey();
            }
        }
        return index;
    }

}