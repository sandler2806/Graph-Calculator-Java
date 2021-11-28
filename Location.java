import api.GeoLocation;

public class Location implements GeoLocation {

    private double x;
    private double y;
    private double z;



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

        return 0;
    }
}
