package transforms;

public class RGBModel {

    private double red, green, blue;

    public RGBModel(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setRGB(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public RGBModel norm() {
        double r, g, b;
        r = red / 255.0;
        g = green / 255.0;
        b = blue / 255.0;
        return new RGBModel(r, g, b);
    }
}
