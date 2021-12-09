package tests;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import imp.Digraph;
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
        assertEquals(graph.getNode(8).getKey(), 8);
        assertEquals(graph.getNode(2).getKey(), 2);

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

        Iterator<EdgeData> ed = graph.edgeIter(0);

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

        graph.removeEdge(0,1);
        graph.removeEdge(2,6);
        graph.removeEdge(5,4);
        assertNull(graph.getEdge(0,1));
        assertNull(graph.getEdge(2,6));
        assertNull(graph.getEdge(5,4));

    }

    @Test
    void nodeSize() {
        assertEquals(graph.nodeSize(),17);
        graph.removeNode(0);
        assertEquals(graph.nodeSize(),16);
        graph.removeNode(1);
        assertEquals(graph.nodeSize(),15);
        graph.addNode(new Node(0,new Location("32.123,34.12,0.0")));
        assertEquals(graph.nodeSize(),16);
    }

    @Test
    void edgeSize() {
        assertEquals(graph.edgeSize(),36);
        graph.removeEdge(0,1);
        assertEquals(graph.edgeSize(),35);
        graph.removeEdge(2,6);
        assertEquals(graph.edgeSize(),34);
        graph.removeEdge(5,4);
        assertEquals(graph.edgeSize(),33);
        graph.connect(5,4,3);
        assertEquals(graph.edgeSize(),34);
    }
}