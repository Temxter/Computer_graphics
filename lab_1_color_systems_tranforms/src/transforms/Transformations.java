package transforms;

import javafx.scene.transform.Transform;

public class Transformations {

    private void log(String s) {
        System.out.println("[" + Transformations.class.getSimpleName() + "]: " + s);
    }

    private CMYKModel cmyk;
    private HSLModel hsl;
    private RGBModel rgb;

    /**
     * @param red   the range from 0..255
     * @param green the range from 0..255
     * @param blue  the range from 0..255
     */
    public Transformations(double red, double green, double blue) {
        rgb = new RGBModel(red, green, blue);
        cmyk = new CMYKModel(0.0, 0.0, 0.0, 0.0);
        hsl = new HSLModel(0.0, 0.0, 0.0);
        setRGB(red, green, blue);
    }

    public void setRGB(double red, double green, double blue) {
        rgb.setRGB(red, green, blue);
        toCMYK();
        toHSL();
    }

    /**
     * @param cyan    in range[0.0-1.0]
     * @param magenta in range[0.0-1.0]
     * @param yellow  in range[0.0-1.0]
     * @param key     in range[0.0-1.0]
     */
    public void setCMYK(double cyan, double magenta, double yellow, double key) {
        cmyk.setCMYK(cyan, magenta, yellow, key);
        double red, green, blue;
        red = (1.0 - cyan) * (1.0 - key);
        green = (1.0 - magenta) * (1.0 - key);
        blue = (1.0 - yellow) * (1.0 - key);
        //rgb in [0, 1]
        rgb.setRGB(red, green, blue);
        toHSL();
    }

    private void setHSL2(double h, double s, double l) {
        hsl.setHSL(h, s, l);
        double red = 0.0, green = 0.0, blue = 0.0;
        double c, x, m;
        c = (1.0 - Math.abs(2.0 * l - 1.0)) * s;
        x = c * (1 - Math.abs(h / 60.0) % 2 - 1.0);
        m = l - c / 2.0;
        if (0.0 <= h && h < 60.0) {
            red = c;
            green = x;
        } else if (h < 120.0) {
            red = x;
            green = c;
        } else if (h < 180.0) {
            green = c;
            blue = x;
        } else if (h < 240.0) {
            green = x;
            blue = c;
        } else if (h < 300.0) {
            red = x;
            blue = c;
        } else if (h <= 360.0) {
            red = c;
            blue = x;
        }
        red = (red + m) * 255.0;
        green = (green + m) * 255.0;
        blue = (blue + m) * 255.0;

        rgb.setRGB(red, green, blue);
        toCMYK();
    }

    /**
     *
     * @param h the range from 0..360 degrees
     * @param s the range from 0..1
     * @param l the range from 0..1
     */
    public void setHSL(double h, double s, double l) {
        hsl.setHSL(h, s, l);
        double red = 0.0, green = 0.0, blue = 0.0;
        double q, p, hk, tr, tg, tb;

        if (l < 0.5) {
            q = l * (1.0 + s);
        } else {
            q = l + s - (l * s);
        }
        p = 2.0 * l - q;
        hk = h / 360.0; //(приведение к интервалу [0,1])
        tr = hk + 1.0 / 3.0;
        tg = hk;
        tb = hk - 1.0 / 3.0;

        tr = HSLTCProcessing(tr);
        tg = HSLTCProcessing(tg);
        tb = HSLTCProcessing(tb);

        red = HSLColorProcessing(p, q, tr);
        green = HSLColorProcessing(p, q, tg);
        blue = HSLColorProcessing(p, q, tb);

        //rgb in [0, 1]
        rgb.setRGB(red, green, blue);
        toCMYK();
    }

    private double HSLTCProcessing(double tc) {
        if (tc < 0.0)
            tc += 1.0;
        else if (tc > 1.0)
            tc -= 1.0;
        return tc;
    }

    private double HSLColorProcessing(double p, double q, double tc) {
        double color;
        if (tc < 1.0 / 6.0) {
            color = p + (q - p) * 6.0 * tc;
        } else if (tc < 0.5) {
            color = q;
        } else if (tc < 2.0 / 3.0) {
            color = p + (q - p) * (2.0 / 3.0 - tc) * 6.0;
        } else {
            color = p;
        }

        return color;
    }

    private void toCMYK() {
        double red, green, blue;
        double c, m, y, k;
        //rgb in [0, 1]
        red = rgb.getRed();
        green = rgb.getGreen();
        blue = rgb.getBlue();
        double max = getMax(red, green, blue);
        k = 1.0 - max;
        c = (1.0 - red - k) / (1.0 - k);
        m = (1.0 - green - k) / (1.0 - k);
        y = (1.0 - blue - k) / (1.0 - k);
        cmyk.setCMYK(c, m, y, k);
    }

    private void toHSL() {
        double red, green, blue;
        double h, s = 0.0, l;
        //rgb in [0, 1]
        red = rgb.getRed();
        green = rgb.getGreen();
        blue = rgb.getBlue();
        double max = getMax(red, green, blue);
        double min = getMin(red, green, blue);
        double delta = max - min;

        log(String.format("toHSL(): r %f, g %f, b %f. max = %f, min = %f",
                red, green, blue, max, min));
        //hue calculation
        if (delta == 0.0) {
            h = 0.0;
            s = 0.0;
        } else if (max == red) {
            if (green >= blue) {
                h = (green - blue) / delta;
            } else {
                h = (green - blue) / delta + 6.0;
            }
        } else if (max == green) {
            h = (blue - red) / delta + 2.0;
        } else {
            h = (red - green) / delta + 4.0;
        }
        h *= 60.0;

        //lightness calculation:
        l = (max + min) / 2.0;

        //saturation calculation
        if (delta != 0.0)
            s = delta / (1.0 - Math.abs(1.0 - (max + min)));

        hsl.setHSL(h, s, l);
    }

    private double getMax(double a, double b, double c) {
        if (a > b)
            if (a > c)
                return a;
            else if (c > b)
                return c;
        return b;
    }

    private double getMin(double a, double b, double c) {
        if (a < b)
            if (a < c)
                return a;
            else if (c < b)
                return c;
        return b;
    }

    public boolean toCorrectValues(){
        boolean isCorrect = true;
        if (cmyk.getCyan() < 0.0) {
            cmyk.setCyan(0.0);
            isCorrect = false;
        } else if (cmyk.getCyan() > 1.0){
            cmyk.setCyan(1.0);
            isCorrect = false;
        } if (cmyk.getYellow() < 0.0) {
            cmyk.setYellow(0.0);
            isCorrect = false;
        } else if (cmyk.getYellow() > 1.0){
            cmyk.setYellow(1.0);
            isCorrect = false;
        }  if (cmyk.getMagenta() < 0.0) {
            cmyk.setMagenta(0.0);
            isCorrect = false;
        } else if (cmyk.getMagenta() > 1.0){
            cmyk.setMagenta(1.0);
            isCorrect = false;
        }  if (cmyk.getKey() < 0.0) {
            cmyk.setKey(0.0);
            isCorrect = false;
        } else if (cmyk.getKey() > 1.0){
            cmyk.setKey(1.0);
            isCorrect = false;
        }

         if (hsl.getHue() < 0.0){
            hsl.setHue(0.0);
            isCorrect = false;
        } else if (hsl.getHue() > 360.0){
            hsl.setHue(360.0);
            isCorrect = false;
        } if (hsl.getLightness() < 0.0){
            hsl.setLightness(0.0);
            isCorrect = false;
        } else if (hsl.getLightness() > 1.0){
            hsl.setLightness(1.0);
            isCorrect = false;
        } if (hsl.getSaturation() < 0.0){
            hsl.setSaturation(0.0);
            isCorrect = false;
        } else if (hsl.getSaturation() > 1.0){
            hsl.setSaturation(1.0);
            isCorrect = false;
        }


        if (rgb.getRed() < 0.0){
            rgb.setRed(0.0);
            isCorrect = false;
        } else if (rgb.getRed() > 1.0){
            rgb.setRed(1.0);
            isCorrect = false;
        }  if (rgb.getGreen() < 0.0){
            rgb.setGreen(0.0);
            isCorrect = false;
        } else if (rgb.getGreen() > 1.0){
            rgb.setGreen(1.0);
            isCorrect = false;
        }  if (rgb.getBlue() < 0.0){
            rgb.setBlue(0.0);
            isCorrect = false;
        } else if (rgb.getBlue() > 1.0){
            rgb.setBlue(1.0);
            isCorrect = false;
        }


        return isCorrect;
    }

    public CMYKModel getCmyk() {
        return cmyk;
    }

    public HSLModel getHsl() {
        return hsl;
    }

    public RGBModel getRgb() {
        return rgb;
    }
}
