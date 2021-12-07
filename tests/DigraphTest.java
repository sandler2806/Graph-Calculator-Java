package tests;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import imp.Digraph;
import imp.Edge;
import imp.Location;
import imp.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

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

        assertNull(graph.getNode(17));
        graph.addNode(new Node(17, new Location("36 , 36, 0")));
        assertEquals(17 , graph.getNode(17).getKey());

        assertNull(graph.getNode(40));
        graph.addNode(new Node(40, new Location("36 , 36, 0")));
        assertEquals(40 , graph.getNode(40).getKey());
    }

    @Test
    void connect() {
        //connect 2 node not previously connected
        assertNull(graph.getEdge(15,1));
        graph.connect(15, 1, 5);
        assertNotNull(graph.getEdge(15,1));

        assertNull(graph.getEdge(13,3));
        graph.connect(13, 3, 1.5);
        assertNotNull(graph.getEdge(13,3));

    }

    @Test
    void nodeIter() {

        Iterator<NodeData> nd = graph.nodeIter();
        int i = 0;
        while (nd.hasNext())
            assertSame(nd.next(), graph.getNode(i++));
    }

    @Test
    void edgeIter() {
        Iterator<EdgeData> ed = graph.edgeIter();
        while(ed.hasNext()){
            EdgeData e = ed.next();
            assertNotNull(graph.getEdge(e.getSrc(),e.getDest()));
        }
    }

    @Test
    void testEdgeIter() {

        Iterator<Edge> ed = graph.edgeIter(0);

        while(ed.hasNext()){
            EdgeData e = ed.next();
            assertNotNull(graph.getEdge(e.getSrc(),e.getDest()));
        }
    }

    @Test
    void removeNode() {

        graph.removeNode(0);
        graph.removeNode(1);
        graph.removeNode(15);
        assertNull(graph.getNode(0));
        assertNull(graph.getNode(1));
        assertNull(graph.getNode(15));
        assertNull(graph.getEdge(0,1));
    }

    @Test
    void removeEdge() {

//        graph.()

    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }
}