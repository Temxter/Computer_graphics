import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ByteImage {

    private void log(String s) {
        System.out.println(s);
    }

    private int height;
    private int width;
    private BufferedImage bufferedImage;
    private String imageFormat = "bmp";
    private String filename;

    public ByteImage(String filename) throws IOException {
        this.filename = filename;
        this.imageFormat = getExtension(filename);
        bufferedImage = ImageIO.read(new File(filename));
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
        log(String.format("width: %d, height: %d, pixels.length: %d", width, height));
    }

    private ByteImage(String filename, BufferedImage bufferedImage) throws IOException {
        this.filename = filename;
        this.imageFormat = getExtension(filename);
        this.bufferedImage = bufferedImage;
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPixel(int x, int y, int defaultColorRGB){
        if (x > width || y > height)
            return defaultColorRGB;
        return bufferedImage.getRGB(x, y);
    }

    private int[] getRGB(int x, int y, int defaultColorRGB) {
        int clr = 0;

        if (x > width || y > height)
            clr = defaultColorRGB;
        else
            clr = bufferedImage.getRGB(x, y);

        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        return new int[]{red, green, blue};
    }

    private int toRGB(int r, int g, int b) {
        int clr = 0;
        clr = r  >> 16;
        clr |= g >> 8;
        clr |= b;
        return clr;
    }

    public void setPixel(int x, int y, int rgb) {
        if (x > width || y > height)
            throw new IndexOutOfBoundsException();
        bufferedImage.setRGB(x, y, rgb);
    }

    public void saveImage() throws IOException {
        saveImage(null);
    }

    public void saveImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            filename = "New" + this.filename;
        ImageIO.write(bufferedImage, imageFormat, new File(filename));
    }


    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    //FILTERS

    public enum StaticFilters {
        Max, Min, Mean
    }

    public ByteImage applyStaticFilter(StaticFilters filterType, int filterSize) throws IOException {
        BufferedImage newBufferedImage = deepCopy(bufferedImage);
        byte[] newPixels = ((DataBufferByte) newBufferedImage.getRaster().getDataBuffer()).getData();

        //log(String.format("width - filterSize: %d, height - filterSize: %d", width - filterSize, height - filterSize));
        for (int x = 0; x < width - filterSize; x++) {
            for (int y = 0; y < height - filterSize; y++) {
                newPixels[x * height + y] = pixelStaticFilter(x, y, filterSize, filterType);
            }
        }

        return new ByteImage("Filter" + filterType.name() + filename, newBufferedImage);
    }

    //update method!!!!!!!!!!!!!!!
    private int pixelStaticFilter(int pixelX, int pixelY, int filterSize, StaticFilters filterType) {
        int pixel = getPixel(pixelX, pixelY, 0);
        int[] rgbMean = new int [] {0,0,0};
        int [] localRGB;
        for (int x = -filterSize / 2; x < filterSize / 2 + 1; x++)
            for (int y = -filterSize / 2; y < filterSize / 2 + 1; y++) {
                switch (filterType) {
                    case Min:
                        if (x > getModule(pixelX + x, pixelY + y))
                            pixel = getPixel(pixelX + x, pixelY + y, pixel);
                        break;
                    case Max:
                        if (x < getModule(pixelX + x, pixelY + y))
                            pixel = getPixel(pixelX + x, pixelY + y, pixel);
                        break;
                    case Mean:
                        localRGB = getRGB(pixelX + x, pixelY + y, pixel);
                        for (int i = 0; i < 3; i++)
                            rgbMean[i] += localRGB[i];
                        break;
                }
            }

        if (filterType.equals(StaticFilters.Mean))
            // cut information: solution - float value
        {
            for (int i = 0; i < 3; i++)
                rgbMean[i] /= filterSize * filterSize;
           return toRGB(rgbMean[0], rgbMean[1], rgbMean[2]);
        }
        return pixel;
    }
    private int getModule(int x, int y){
        int[] rgb = getRGB(x, y, 0);
        int module = (int)Math.sqrt(rgb[0]*rgb[0] + rgb[1]*rgb[1] + rgb[2]*rgb[2]);
        return module;
    }


    private byte getByte(int x, int y) {
        return pixels[x * height + y];
    }


    static private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot > 0 && dot < filename.length() - 1) {
            return filename.substring(dot + 1).toLowerCase();
        }
        return "";
    }

}
