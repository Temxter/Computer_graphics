import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class ByteImage {

    private void log(String s) {
        System.out.println(s);
    }

    private int height;
    private int width;
    private byte[] pixels;
    private BufferedImage bufferedImage;
    private String imageFormat = "bmp";
    private String filename;

    public ByteImage(String filename) throws IOException {
        this.filename = filename;
        this.imageFormat = getExtension(filename);
        bufferedImage = ImageIO.read(new File(filename));
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
        pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        log(String.format("width: %d, height: %d, pixels.length: %d", width, height, pixels.length));
    }

    private ByteImage(String filename, BufferedImage bufferedImage) throws IOException {
        this.filename = filename;
        this.imageFormat = getExtension(filename);
        this.bufferedImage = bufferedImage;
        height = bufferedImage.getHeight();
        width = bufferedImage.getWidth();
        pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPixel(int x, int y) {
        if (x > width || y > height)
            throw new IndexOutOfBoundsException();
        return pixels[x * height + y] & 0xFF;
    }

    public void setPixel(int x, int y, int value) {
        if (x > width || y > height)
            throw new IndexOutOfBoundsException();
        pixels[x * height + y] = (byte) (value & 0xFF);
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

    private byte pixelStaticFilter(int pixelX, int pixelY, int filterSize, StaticFilters filterType) {
        int value = getByte(pixelX, pixelY);

        for (int x = 0; x < filterSize; x++)
            for (int y = 1; y < filterSize; y++)
                switch (filterType) {
                    case Min:
                        if ((value & 0xFF) > (getByte(pixelX + x, pixelY + y) & 0xFF))
                            value = getByte(pixelX + x, pixelY + y);
                        break;
                    case Max:
                        if ((value & 0xFF) < (getByte(pixelX + x, pixelY + y) & 0xFF))
                            value = getByte(pixelX + x, pixelY + y);
                        break;
                    case Mean:
                        value += getByte(pixelX + x, pixelY + y);
                        break;
                }

        if (filterType.equals(StaticFilters.Mean))
            // cut information: solution - float value
            return (byte) (value / (filterSize * filterSize));
        return (byte) value;
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

    public enum MorphologicalFilters {
        Dilation, Erosion
    }

    public ByteImage applyMorphologicalFilter(Filter filter, MorphologicalFilters morphologicalFilter) throws IOException {
        BufferedImage newBufferedImage = deepCopy(bufferedImage);
        byte[] newPixels = ((DataBufferByte) newBufferedImage.getRaster().getDataBuffer()).getData();

        if (morphologicalFilter.equals(MorphologicalFilters.Erosion))
            for (int i = 0; i < newPixels.length; i++)
                newPixels[i] = -1;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                switch (morphologicalFilter) {
                    case Dilation:   pixelMorphologicalFilter(x, y, newPixels, filter, morphologicalFilter);
                    break;
                    case Erosion:
                        if (isNotErosion(x, y, filter))
                            pixelMorphologicalFilter(x, y, newPixels, filter, morphologicalFilter);
                    break;
                }

        return new ByteImage("MorphologicalFilter" + morphologicalFilter.name() + filename, newBufferedImage);
    }

    private void pixelMorphologicalFilter(int x, int y, byte[] newPixels,
                                          Filter filter, MorphologicalFilters morphologicalFilter) {
        // if not white than combine
        //log(String.format("getPixel(x, y): %d", getPixel(x, y)));
            for (int i = 0; i < filter.getMaxX(); i++)
                for (int j = 0; j < filter.getMaxY(); j++) {
                    int newX = x + i - filter.getCentralX();
                    int newY = y + j - filter.getCentralY();
                    // if in bound of picture
                    if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                        //log(String.format("newX: %d, newY: %d", newX, newY));
                        if (morphologicalFilter.equals(MorphologicalFilters.Dilation)) {
                            if (getPixel(x, y) < 128)
                                setByteToArray(newX, newY, newPixels, filter.getByteValue(i, j));
                        }
                        else
                            setByteToArray(newX, newY, newPixels, getByte(newX, newY));
                    }
                }
    }

    private boolean isNotErosion(int x, int y, Filter filter) {
        log(String.format("getPixel(x, y): %d", getPixel(x, y)));
        boolean isNotErosion = true;
        for (int i = 0; i < filter.getMaxX(); i++)
            for (int j = 0; j < filter.getMaxY(); j++) {
                int newX = x + i - filter.getCentralX();
                int newY = y + j - filter.getCentralY();
                // if in bound of picture
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    log(String.format("(getPixel(newX, newY) >= (filter.getValue(i, j) & 0xFF) - 25 ||\n" +
                            "getPixel(newX, newY) <= (filter.getValue(i, j) & 0xFF) + 25)\n" +
                            (getPixel(newX, newY) + " >= " + ((filter.getValue(i, j) & 0xFF) - 25) + "\n" +
                                    getPixel(newX, newY) + " <= " + ((filter.getValue(i, j) & 0xFF) + 25))));



                    // if not in bound of 51 shades
                    //TODO error with || => &&
                    if (!(getPixel(newX, newY) >= (filter.getValue(i, j) & 0xFF) - 25 &&
                            getPixel(newX, newY) <= (filter.getValue(i, j) & 0xFF) + 25)) {
                        isNotErosion = false;
                    }
                }
            }
        // if is not erosion then fill values on picture by old values
        return isNotErosion;
    }


    private byte getByteFromArray(int x, int y, byte[] byteArray) {
        return byteArray[x * height + y];
    }

    private void setByteToArray(int x, int y, byte[] byteArray, byte value) {
        byteArray[x * height + y] = value;
    }

}
