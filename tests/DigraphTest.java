package tests;

import api.DirectedWeightedGraph;
import imp.Digraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class DigraphTest {

    DirectedWeightedGraph graph;

    @BeforeEach
    public void init() throws IOException {

        graph = new Digraph("data/G1.json");

    }



    @Test
    void getNode() {

        assertEquals(graph.getNode(0).getKey(), 0);
        assertEquals(graph.getNode(5).getKey(), 5);


    }

    @Test
    void getEdge() {

        assertEquals(graph.getEdge(0,16).getDest(),16 );
        assertEquals(graph.getEdge(3,4).getDest(),4 );
        //check for non-existing edge on existing node
        assertNull(graph.getEdge(0, 910));
        assertNull(graph.getEdge(5, 1000));

    }

    @Test
    void addNode() {

    }

    @Test
    void connect() {
    }

    @Test
    void nodeIter() {
    }

    @Test
    void edgeIter() {
    }

    @Test
    void testEdgeIter() {
    }

    @Test
    void removeNode() {
    }

    @Test
    void removeEdge() {
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }
}