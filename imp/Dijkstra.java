package imp;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class Dijkstra {

    DirectedWeightedGraph graph;

    public Dijkstra(DirectedWeightedGraph g){
        graph = g;

    }


    public HashMap<Integer,Double>[] shp(int src){

        HashMap<Integer,Double> dist = new HashMap<>();
        HashMap<Integer,Double> prev = new HashMap<>();

        dist.put(src,0.0);

        FibonacciHeap<Integer> que = new FibonacciHeap<>();
        HashMap<Integer, FibonacciHeap.Entry<Integer>> map = new HashMap<>();


        Iterator<NodeData> it = graph.nodeIter();

        while(it.hasNext()){
            Node n = (Node) it.next();
            if(n.getKey() != src){
                dist.put(n.getKey(),Double.MAX_VALUE);
                prev.put(n.getKey(),-1.0);
            }
            FibonacciHeap.Entry<Integer> e = que.enqueue(n.getKey(), dist.get(n.getKey()));
            map.put(n.getKey(),e);
        }



        while(!que.isEmpty()){

            FibonacciHeap.Entry<Integer> min = que.dequeueMin();
            Iterator<EdgeData> nei = graph.edgeIter(min.getValue());
            while(nei.hasNext()){
                EdgeData e = nei.next();
                double alt = dist.get(e.getSrc()) + e.getWeight();
                if( alt < dist.get(e.getDest())){
                    dist.put(e.getDest(), alt);
                    prev.put(e.getDest(), (double) e.getSrc());
                    que.decreaseKey(map.get(e.getDest()), alt);
                }
            }
        }
        HashMap<Integer,Double>[] ans=new HashMap[2];
        ans[0]=dist;
        ans[1]=prev;
        return ans;
    }

}
