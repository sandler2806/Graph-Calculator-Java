package imp;

import api.EdgeData;
import com.google.gson.Gson;

public class Edge implements EdgeData {

    int src, dest;
    double weight;
    int tag;



    public Edge(int src, double weight, int dest){
        this.src = src;
        this.dest = dest;
        this.weight = weight;

    }


    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
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
