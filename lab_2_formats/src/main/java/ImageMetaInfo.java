import javafx.beans.property.*;

public class ImageMetaInfo {
    /**
     * fields for calculate dpi (default A4 paper format)
     */
    private static SimpleDoubleProperty paperWidth = new SimpleDoubleProperty(8.27),
            paperHeight = new SimpleDoubleProperty(11.69);
    /**
     * name with extension
     */
    private SimpleStringProperty name;
    /**
     * in bytes, kb, mb...
     */
    private SimpleLongProperty size;
    /**
     * Bits per inch
     */
    private SimpleIntegerProperty bpi;
    /**
     * is image compressed?
     */
    private SimpleStringProperty compressed;

    /**
     * width in pixels
     */
    private SimpleIntegerProperty width;
    /**
     * height in pixels
     */
    private SimpleIntegerProperty height;

    public static double getPaperWidth() {
        return paperWidth.get();
    }

    public static SimpleDoubleProperty paperWidthProperty() {
        return paperWidth;
    }

    public static void setPaperWidth(double paperWidth) {
        ImageMetaInfo.paperWidth.set(paperWidth);
    }

    public static double getPaperHeight() {
        return paperHeight.get();
    }

    public static SimpleDoubleProperty paperHeightProperty() {
        return paperHeight;
    }

    public static void setPaperHeight(double paperHeight) {
        ImageMetaInfo.paperHeight.set(paperHeight);
    }

    public ImageMetaInfo(String name, long size, int width, int height, int bpi, String compressed) {
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleLongProperty(size);
        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
        this.bpi = new SimpleIntegerProperty(bpi);
        this.compressed = new SimpleStringProperty(compressed);
    }


    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public long getSize() {
        return size.get();
    }

    public SimpleLongProperty sizeProperty() {
        return size;
    }

    public void setSize(long size) {
        this.size.set(size);
    }

    /**
     *
     * @return dpi depending ratio (stretched image on paper) with rounding
     */
    public double getDpi() {
        double dpi = 0.0;
        if (paperHeight.getValue()/paperWidth.getValue() >
                (double)height.getValue()/(double)width.getValue())
            dpi = (double)width.getValue()/paperWidth.getValue();
        else
            dpi = (double)height.getValue()/paperHeight.getValue();
        return Math.round(dpi * 10.0)/10.0;
    }


    public int getBpi() {
        return bpi.get();
    }

    public SimpleIntegerProperty bpiProperty() {
        return bpi;
    }

    public void setBpi(int bpi) {
        this.bpi.set(bpi);
    }

    public String getCompressed() {
        return compressed.get();
    }

    public SimpleStringProperty compressedProperty() {
        return compressed;
    }

    public void setCompressed(String compressed) {
        this.compressed.set(compressed);
    }

    public String getExtension() {
        String filename = name.getValue();
        int dot = filename.lastIndexOf('.');
        if (dot > 0 && dot < filename.length() - 1) {
            return filename.substring(dot+1).toLowerCase();
        }
        return "";
    }

    public String getResolution(){
        return String.format("%dx%d", width.get(), height.get());
    }
}
