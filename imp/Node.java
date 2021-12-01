package imp;

import api.GeoLocation;
import api.NodeData;

public class Node implements NodeData {

    private int id;
    private Location pos;
    private int tag;


    public Node(int id, Location gl){

        this.id = id;
        pos = gl;

    }



    @Override
    public int getKey() {
        return id;
    }

    @Override
    public GeoLocation getLocation() {
        return pos;
    }

    @Override
    public void setLocation(GeoLocation p) {
        pos = (Location) p;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {

        tag = t;
    }
}
