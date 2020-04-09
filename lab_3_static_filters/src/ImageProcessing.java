import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {

    private void log(String s) {
        System.out.println(s);
    }

    private int height;
    private int width;
    private BufferedImage bufferedImage;
    private String imageFormat = "bmp";
    private String filename;

    public ImageProcessing(String filename) throws IOException {
        this.filename = truncFilename(filename);
        this.imageFormat = getExtension(filename);
        bufferedImage = ImageIO.read(new File(filename));
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
        log(String.format("width: %d, height: %d", width, height));
    }

    private String truncFilename(String filename){
        return filename.substring(filename.lastIndexOf("\\") + 1);
    }

    private ImageProcessing(String filename, BufferedImage bufferedImage) throws IOException {
        this.filename = filename;
        this.imageFormat = getExtension(filename);
        this.bufferedImage = bufferedImage;
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
    }

    public String saveImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            filename = "New" + this.filename;
        File newFile = new File(filename);
        ImageIO.write(bufferedImage, imageFormat, new File(filename));
        return newFile.getAbsolutePath();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color getPixel(int x, int y, Color defaultColor) {
        if (x >= width || x < 0 || y >= height || y < 0)
            return defaultColor;
        return new Color(bufferedImage.getRGB(x, y));
    }

    //FILTERS

    public enum StaticFilters {
        Max, Min, Mean
    }

    public ImageProcessing applyStaticFilter(StaticFilters filterType, int filterSize) throws IOException {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //log(String.format("width - filterSize: %d, height - filterSize: %d", width - filterSize, height - filterSize));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color newColor = pixelStaticFilter(x, y, filterSize, filterType);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return new ImageProcessing("Filter" + filterType.name() + filename, newImage);
    }

    private Color pixelStaticFilter(int pixelX, int pixelY, int filterSize, StaticFilters filterType) {
        Color c = null;
        switch (filterType) {
            case Min:
                c = pixelStaticMin(pixelX, pixelY, filterSize);
                break;
            case Max:
                c = pixelStaticMax(pixelX, pixelY, filterSize);
                break;
            case Mean:
                c = pixelStaticMean(pixelX, pixelY, filterSize);
                break;
        }
        return c;
    }

    private Color pixelStaticMax(int pixelX, int pixelY, int filterSize) {
        Color mainPixel = getPixel(pixelX, pixelY, null); // not null always if algorithm correct
        for (int x = -filterSize / 2; x < filterSize / 2 + 1; x++)
            for (int y = -filterSize / 2; y < filterSize / 2 + 1; y++) {
                Color c = getPixel(pixelX + x, pixelY + y, null);
                if (c == null)
                    continue;
                if (compareColors(mainPixel, c) > 0)
                    mainPixel = c;
            }

        return mainPixel;
    }

    private Color pixelStaticMin(int pixelX, int pixelY, int filterSize) {
        Color mainPixel = getPixel(pixelX, pixelY, null); // not null always if algorithm correct
        //log(String.format("pixelStaticMin: pixelX: %d, pixelY: %d, Color: %s", pixelX, pixelY, mainPixel));
        for (int x = -filterSize / 2; x < filterSize / 2 + 1; x++)
            for (int y = -filterSize / 2; y < filterSize / 2 + 1; y++) {
                Color c = getPixel(pixelX + x, pixelY + y, null);
                //log(String.format("pixelStaticMin: pixelX: %d, pixelY: %d, Color c: %s", pixelX, pixelY, c));
                if (c == null)
                    continue;
                if (compareColors(mainPixel, c) < 0)
                    mainPixel = c;
            }
        //log(String.format("pixelStaticMin: pixelX: %d, pixelY: %d, Color: %s", pixelX, pixelY, mainPixel));
        return mainPixel;
    }

    private Color pixelStaticMean(int pixelX, int pixelY, int filterSize) {
        int counter = 0;
        int rgb[] = {0, 0, 0};
        for (int x = -filterSize / 2; x < filterSize / 2 + 1; x++)
            for (int y = -filterSize / 2; y < filterSize / 2 + 1; y++) {
                Color c = getPixel(pixelX + x, pixelY + y, null);
                if (c == null)
                    continue;
                rgb[0] += c.getRed();
                rgb[1] += c.getGreen();
                rgb[2] += c.getBlue();
                counter++;
            }
        for (int i = 0; i < 3; i++)
            rgb[i] /= counter;
        Color c = new Color(toRgbFromArray(rgb[0], rgb[1], rgb[2]));
        return c;
    }

    private int toRgbFromArray(int r, int g, int b) {
        int clr = 0;
        clr = r;
        clr = (clr << 8) + g;
        clr = (clr << 8) + b;
        return clr;
    }

    private int compareColors(Color a, Color b) {
        int moduleA = a.getRed() * a.getRed() + a.getBlue() * a.getBlue() + a.getGreen() * a.getGreen();
        int moduleB = b.getRed() * b.getRed() + b.getBlue() * b.getBlue() + b.getGreen() * b.getGreen();
        return moduleA - moduleB;
    }

    static private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot > 0 && dot < filename.length() - 1) {
            return filename.substring(dot + 1).toLowerCase();
        }
        return "";
    }

}
