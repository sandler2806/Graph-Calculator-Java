package tests;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import imp.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AlgorithmsTest {

    static Algorithms algorithms=new Algorithms();
    static Algorithms algorithms1=new Algorithms();
    static Algorithms algorithms2=new Algorithms();
    static Algorithms algorithms3=new Algorithms();
    static Algorithms algorithms1000=new Algorithms();
    static Algorithms algorithms10000=new Algorithms();
    static Algorithms algorithms10000n=new Algorithms();
//    static Algorithms algorithms100000=new Algorithms();

    @BeforeEach
    void start() throws IOException {
        algorithms.init(new Digraph("data/Gtest.json"));
        algorithms1.init(new Digraph("data/G1.json"));
        algorithms2.init(new Digraph("data/G2.json"));
        algorithms3.init(new Digraph("data/G3.json"));
        algorithms1000.init(new Digraph("data/1000Nodes.json"));
        algorithms10000.init(new Digraph("data/10000Nodes.json"));
        algorithms10000n.init(new Digraph("data/10000Nodes_notcon.json"));
//        algorithms100000.init(new Digraph("data/100000.json"));

    }

    @Test
    void getGraph() {
        assertNotNull(algorithms.getGraph());
        assertNotNull(algorithms1.getGraph());
        assertNotNull(algorithms1000.getGraph());
        assertNotNull(algorithms10000.getGraph());
//        assertNotNull(algorithms100000.getGraph());

    }

    @Test
    void copy() {
        DirectedWeightedGraph c1000=algorithms1000.copy();
        algorithms1000.getGraph().removeNode(1);
        assertNotNull(c1000.getNode(1));

        DirectedWeightedGraph c10000=algorithms10000.copy();
        algorithms10000.getGraph().removeNode(5);
        assertNotNull(c10000.getNode(5));

//        DirectedWeightedGraph c100000=algorithms100000.copy();
//        algorithms100000.getGraph().removeNode(15);
//        assertNotNull(c100000.getNode(15));
    }

    @Test
    void isConnected() {
        assertTrue(algorithms.isConnected());
        assertTrue(algorithms1.isConnected());
        assertTrue(algorithms2.isConnected());
        assertTrue(algorithms3.isConnected());
        assertTrue(algorithms1000.isConnected());
        assertTrue(algorithms10000.isConnected());
        assertFalse(algorithms10000n.isConnected());
//        assertTrue(algorithms100000.isConnected());

    }

    @Test
    void shortestPathDist() {
        assertEquals(algorithms.shortestPathDist(1,2),45);
        assertEquals(algorithms.shortestPathDist(1,3),45);
        assertEquals(algorithms.shortestPathDist(1,4),10);
        assertEquals(algorithms.shortestPathDist(3,6),60);
        assertEquals(algorithms.shortestPathDist(1,5),25);
        assertEquals(algorithms.shortestPathDist(1,6),55);
        assertEquals(algorithms.shortestPathDist(4,3),45);
        assertEquals(algorithms1000.shortestPathDist(1,100),1090.6025677384555);
        assertEquals(algorithms10000.shortestPathDist(14,2345),830.6354147999858);
//        assertEquals(algorithms100000.shortestPathDist(37489,63724),532.4621267718071);
    }

    @Test
    void shortestPath() {
        assertEquals(algorithms.shortestPath(1,6).toString(),"[Node{id=1}, Node{id=4}, Node{id=5}, Node{id=6}]");
        assertEquals(algorithms.shortestPath(5,1).toString(),"[Node{id=5}, Node{id=2}, Node{id=4}, Node{id=1}]");
        assertEquals(algorithms.shortestPath(2,5).toString(),"[Node{id=2}, Node{id=4}, Node{id=5}]");
        assertEquals(algorithms.shortestPath(4,1).toString(),"[Node{id=4}, Node{id=1}]");
        assertEquals(algorithms1000.shortestPath(34,234).toString(),"[Node{id=34}, Node{id=870}, Node{id=306}, Node{id=861}, Node{id=818}, Node{id=337}, Node{id=992}, Node{id=64}, Node{id=119}, Node{id=275}, Node{id=619}, Node{id=234}]");
        assertEquals(algorithms10000.shortestPath(1647,9375).toString(),"[Node{id=1647}, Node{id=5955}, Node{id=5739}, Node{id=9115}, Node{id=7577}, Node{id=4677}, Node{id=9375}]");
//        assertEquals(algorithms100000.shortestPath(38492,94265).toString(),"[Node{id=38492}, Node{id=35554}, Node{id=7649}, Node{id=62340}, Node{id=44491}, Node{id=12957}, Node{id=47638}, Node{id=45880}, Node{id=22317}, Node{id=40217}, Node{id=42671}, Node{id=48711}, Node{id=11769}, Node{id=15980}, Node{id=65782}, Node{id=57572}, Node{id=98506}, Node{id=88000}, Node{id=94265}]");
    }

    @Test
    void center() {
        assertEquals(algorithms.center().getKey(),4);
        assertEquals(algorithms1.center().getKey(),8);
        assertEquals(algorithms2.center().getKey(),0);
        assertEquals(algorithms3.center().getKey(),40);
        assertEquals(algorithms1000.center().getKey(),362);
//        assertEquals(algorithms10000.center().getKey(),3846);

    }

    @Test
    void tsp() {
        List<NodeData>nodes=new ArrayList<>();
        nodes.add(algorithms.getGraph().getNode(4));
        nodes.add(algorithms.getGraph().getNode(2));
        nodes.add(algorithms.getGraph().getNode(6));
        assertEquals(algorithms.tsp(nodes).toString(),"[Node{id=6}, Node{id=5}, Node{id=2}, Node{id=4}]");
        nodes.clear();
        nodes.add(algorithms.getGraph().getNode(6));
        nodes.add(algorithms.getGraph().getNode(4));
        nodes.add(algorithms.getGraph().getNode(2));
        assertEquals(algorithms.tsp(nodes).toString(),"[Node{id=6}, Node{id=5}, Node{id=2}, Node{id=4}]");
        nodes.clear();
        nodes.add(algorithms.getGraph().getNode(1));
        nodes.add(algorithms.getGraph().getNode(6));
        nodes.add(algorithms.getGraph().getNode(3));
        nodes.add(algorithms.getGraph().getNode(4));
        assertEquals(algorithms.tsp(nodes).toString(),"[Node{id=6}, Node{id=5}, Node{id=2}, Node{id=3}, Node{id=5}, Node{id=2}, Node{id=4}, Node{id=1}]");

        nodes.clear();
        algorithms1.getGraph().removeNode(1);
        algorithms1.getGraph().removeNode(15);
        nodes.add(algorithms1.getGraph().getNode(16));
        nodes.add(algorithms1.getGraph().getNode(14));
        assertNull(algorithms1.tsp(nodes));

    }

    @Test
    void save() {
        Algorithms alg = new Algorithms();
        alg.load("data/Gtest.json");
        alg.save("data/newG.json");

    }

    @Test
    void load() {

        Algorithms alg = new Algorithms();
        alg.load("data/Gtest.json");
        alg.load("data/G1.json");

    }
    public static double calculator(Algorithms algorithm,List<NodeData>ans,List<NodeData>citis){
        double dist=0;
        for (int i = 0; i < ans.size()-1; i++) {
            dist+=algorithm.getGraph().getEdge(ans.get(i).getKey(),ans.get(i+1).getKey()).getWeight();
            citis.remove(ans.get(i));
        }
        citis.remove(ans.get(ans.size()-1));
        if(!citis.isEmpty())return -1;
        return dist;
    }
}