public class LinearEquation {

    private double x0, x1, k, b;

    public LinearEquation(double x0, double x1, double k, double b) {
        if (x1 > x0) {
            this.x0 = x0;
            this.x1 = x1;
        }
        else {
            this.x1 = x0;
            this.x0 = x1;
        }
        this.k = k;
        this.b = b;
    }

    public double getY(double x) {
        return k * x + b;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public double getK() {
        return k;
    }

    public double getB() {
        return b;
    }
}
