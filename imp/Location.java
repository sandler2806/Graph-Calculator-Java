package imp;

import api.GeoLocation;

public class Location implements GeoLocation {

    private final double x;
    private final double y;
    private final double z;



    public Location(String s){
        String [] arr = s.split(",");
        x = Double.parseDouble(arr[0]);
        y = Double.parseDouble(arr[1]);
        z = Double.parseDouble(arr[2]);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(GeoLocation gl) {

        double x2 = gl.x(), y2 = gl.y();
        return Math.sqrt(Math.pow(y - y2,2) + Math.pow(x - x2, 2));

    }
}
