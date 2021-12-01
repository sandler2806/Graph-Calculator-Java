package tests;

import imp.Algorithms;
import imp.Digraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    Algorithms algorithms=new Algorithms();
    Algorithms algorithms1=new Algorithms();
    Algorithms algorithms2=new Algorithms();
    Algorithms algorithms3=new Algorithms();

    @BeforeEach
    public void start() throws IOException {
        algorithms.init(new Digraph("data/Gtest.json"));
        algorithms1.init(new Digraph("data/G1.json"));
        algorithms2.init(new Digraph("data/G2.json"));
        algorithms3.init(new Digraph("data/G3.json"));

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
    }

    @Test
    void isConnected() throws IOException {
        assertTrue(algorithms.isConnected());
        assertTrue(algorithms1.isConnected());
        assertTrue(algorithms2.isConnected());
        assertTrue(algorithms3.isConnected());
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
        assertEquals(algorithms.center().getKey(),4);
        assertEquals(algorithms1.center().getKey(),8);
        assertEquals(algorithms2.center().getKey(),0);
        assertEquals(algorithms3.center().getKey(),40);

    }

    @Test
    void tsp() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}