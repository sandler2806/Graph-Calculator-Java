package tests;

import api.NodeData;
import imp.Location;
import imp.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    NodeData nodeData=new Node(4,new Location("35.205764353510894,32.106326494117646,0.0"));
    NodeData nodeData2=new Node(8,new Location("35.405764353510894,32.506326494117646,0.0"));


    @Test
    void getKey() {
        assertEquals(4, nodeData.getKey());
        assertEquals(8, nodeData2.getKey());
    }

    @Test
    void getLocation() {
        assertEquals(new Location("35.205764353510894,32.106326494117646,0.0").x(), nodeData.getLocation().x());
        assertEquals(new Location("35.405764353510894,32.506326494117646,0.0").y(), nodeData2.getLocation().y());
    }

    @Test
    void setLocation() {
        nodeData.setLocation(new Location("35.105764353510894,32.706326494117646,0.0"));
        assertEquals(new Location("35.105764353510894,32.706326494117646,0.0").x(), nodeData.getLocation().x());
        nodeData2.setLocation(new Location("34.105764353510894,33.706326494117646,0.0"));
        assertEquals(new Location("34.105764353510894,33.706326494117646,0.0").y(), nodeData2.getLocation().y());
    }

    @Test
    void getInfo() {
    }

    @Test
    void setInfo() {
    }

    @Test
    void getTag() {
        assertEquals(nodeData.getTag(),0);
        nodeData.setTag(2);
        assertEquals(nodeData.getTag(),2);
        nodeData.setTag(5);
        assertEquals(nodeData.getTag(),5);
    }

    @Test
    void setTag() {
        assertEquals(nodeData.getTag(),0);
        nodeData.setTag(2);
        assertEquals(nodeData.getTag(),2);
        nodeData.setTag(5);
        assertEquals(nodeData.getTag(),5);
    }
}