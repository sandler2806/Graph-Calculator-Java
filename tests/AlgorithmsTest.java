package tests;

import api.NodeData;
import imp.Algorithms;
import imp.Digraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    Algorithms algorithms=new Algorithms();
    Algorithms algorithms1=new Algorithms();
    Algorithms algorithms2=new Algorithms();
    Algorithms algorithms3=new Algorithms();
    Algorithms algorithms1000=new Algorithms();
    Algorithms algorithms10000=new Algorithms();
    Algorithms algorithms10000n=new Algorithms();

    @BeforeEach
    public void start() throws IOException {
        algorithms.init(new Digraph("data/Gtest.json"));
        algorithms1.init(new Digraph("data/G1.json"));
        algorithms2.init(new Digraph("data/G2.json"));
        algorithms3.init(new Digraph("data/G3.json"));
        algorithms1000.init(new Digraph("data/1000Nodes.json"));
        algorithms10000.init(new Digraph("data/10000Nodes.json"));
        algorithms10000n.init(new Digraph("data/10000Nodes_notcon.json"));

    }


    @Test
    void init() throws IOException {
//        algorithms.init(new Digraph("data/Gtest.json"));
//        algorithms1.init(new Digraph("data/G1.json"));
//        algorithms2.init(new Digraph("data/G2.json"));
//        algorithms3.init(new Digraph("data/G3.json"));
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        algorithms.copy();
    }

    @Test
    void isConnected() throws IOException {
        assertTrue(algorithms.isConnected());
        assertTrue(algorithms1.isConnected());
        assertTrue(algorithms2.isConnected());
        assertTrue(algorithms3.isConnected());
        assertTrue(algorithms1000.isConnected());
        assertTrue(algorithms10000.isConnected());
        assertFalse(algorithms10000n.isConnected());
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

    }

    @Test
    void shortestPath() {
        assertEquals(algorithms.shortestPath(1,6).toString(),"[Node{id=1}, Node{id=4}, Node{id=5}, Node{id=6}]");
        assertEquals(algorithms.shortestPath(5,1).toString(),"[Node{id=5}, Node{id=2}, Node{id=4}, Node{id=1}]");
        assertEquals(algorithms.shortestPath(2,5).toString(),"[Node{id=2}, Node{id=4}, Node{id=5}]");
        assertEquals(algorithms.shortestPath(4,1).toString(),"[Node{id=4}, Node{id=1}]");
    }

    @Test
    void center() {
//        assertEquals(algorithms.center().getKey(),4);
//        assertEquals(algorithms1.center().getKey(),8);
//        assertEquals(algorithms2.center().getKey(),0);
//        assertEquals(algorithms3.center().getKey(),40);
        assertEquals(algorithms1000.center().getKey(),362);
//        assertEquals(algorithms10000.center().getKey(),3846);

    }

    @Test
    void tsp() {
        List<NodeData>nodes=new ArrayList<>();
        nodes.add(algorithms.getGraph().getNode(4));
        nodes.add(algorithms.getGraph().getNode(2));
        nodes.add(algorithms.getGraph().getNode(6));
        assertEquals(algorithms.tsp(nodes).toString(),"[Node{id=4}, Node{id=5}, Node{id=2}, Node{id=4}, Node{id=5}, Node{id=6}]");
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
        assertEquals(algorithms.tsp(nodes).toString(),"[Node{id=1}, Node{id=4}, Node{id=5}, Node{id=2}, Node{id=3}, Node{id=5}, Node{id=6}]");
        System.out.println(algorithms1000.tsp(nodes));

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
}