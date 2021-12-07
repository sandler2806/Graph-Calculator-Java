package tests;

import api.EdgeData;
import imp.Edge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    EdgeData edgeData=new Edge(0,2,8);
    EdgeData edgeData2=new Edge(8,7,5);
    EdgeData edgeData3=new Edge(0,9,4);

    @Test
    void getSrc() {
        assertEquals(edgeData.getSrc(),0);
        assertEquals(edgeData2.getSrc(),8);
        assertEquals(edgeData3.getSrc(),0);
    }

    @Test
    void getDest() {
        assertEquals(edgeData.getDest(),8);
        assertEquals(edgeData2.getDest(),5);
        assertEquals(edgeData3.getDest(),4);
    }

    @Test
    void getWeight() {
        assertEquals(edgeData.getWeight(),2);
        assertEquals(edgeData2.getWeight(),7);
        assertEquals(edgeData3.getWeight(),9);
    }

    @Test
    void getInfo() {
    }

    @Test
    void setInfo() {
    }
}