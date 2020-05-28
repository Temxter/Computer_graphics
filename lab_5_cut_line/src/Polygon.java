import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }


    public ArrayList<Point> getPoints() {
        return points;
    }
}
