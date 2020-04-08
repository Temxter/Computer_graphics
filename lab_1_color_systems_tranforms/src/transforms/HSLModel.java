package transforms;

public class HSLModel {

    private double hue, saturation, lightness;

    public HSLModel(double hue, double saturation, double lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    public void setHSL(double hue, double saturation, double lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public double getLightness() {
        return lightness;
    }

    public void setLightness(double lightness) {
        this.lightness = lightness;
    }
}
