package transforms;

public class CMYKModel {

    private double cyan, magenta, yellow, key;

    public CMYKModel(double cyan, double magenta, double yellow, double key) {
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.key = key;
    }

    public void setCMYK(double cyan, double magenta, double yellow, double key) {
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.key = key;
    }

    public double getCyan() {
        return cyan;
    }

    public void setCyan(double cyan) {
        this.cyan = cyan;
    }

    public double getMagenta() {
        return magenta;
    }

    public void setMagenta(double magenta) {
        this.magenta = magenta;
    }

    public double getYellow() {
        return yellow;
    }

    public void setYellow(double yellow) {
        this.yellow = yellow;
    }

    public double getKey() {
        return key;
    }

    public void setKey(double key) {
        this.key = key;
    }
}
