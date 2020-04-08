import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class FileProcessor {

    static private Logger logger = Logger.getLogger(FileProcessor.class.getName());

    static private void log(String s) {
        logger.log(Level.INFO, s);
    }

    /**
     * Processing image files by specified path
     *
     * @param path - folder with image files
     * @return
     */
    static public ObservableList<ImageMetaInfo> getImagesInfo(File path) {
        ObservableList<ImageMetaInfo> observableList = FXCollections.observableArrayList();
        ExtFilter fileFilter = new ExtFilter();
        File[] imageFilesArray = path.listFiles(fileFilter);
        for (File file : imageFilesArray) {
            ImageMetaInfo imageMetaInfo = getImageMetaInfo(file);
            observableList.add(imageMetaInfo);
        }
        return observableList;
    }

    /**
     * The method takes out the meta information from image file
     *
     * @param file image file
     * @return
     */
    static private ImageMetaInfo getImageMetaInfo(File file) {
        ImageMetaInfo imageMetaInfo = null;
        ImageParser imageParser = null;
        BufferedImage bufferedImage = null;
        ImageInfo imageInfo = null;
        ImageInfo.CompressionAlgorithm compressionAlgorithm = null;
        String compression = "";
        try {
            log("read file: " + file.getName());

            String extension = ExtFilter.getExtension(file);
            // unambiguous definition of extension
            if (extension.equals("jpg"))
                extension = "jpeg";
            else if (extension.equals("tif"))
                extension = "tiff";

            imageParser = imageParserMap.get(extension);
            bufferedImage = imageParser.getBufferedImage(file, null);
            //for know compression algorithm name
            imageInfo = imageParser.getImageInfo(file, null);
            compressionAlgorithm = imageInfo.getCompressionAlgorithm();
            compression = compressionAlgorithm.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bufferedImage == null){
            try {
                bufferedImage = ImageIO.read(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            ColorModel colorModel = bufferedImage.getColorModel();
            int bpi = colorModel.getPixelSize();
            //size in kb
            long size = (long) (file.length() / 1024.0);
            imageMetaInfo = new ImageMetaInfo(file.getName(), size, width, height, bpi, compression);
        } catch (Exception e){
            e.printStackTrace();
        }


        return imageMetaInfo;
    }

    //hashmap on enum? faster works
    static HashMap<String, ImageParser> imageParserMap = new HashMap<>();
    static {
        imageParserMap.put("pcx", new PcxImageParser());
        imageParserMap.put("jpeg", new JpegImageParser());
        imageParserMap.put("gif", new GifImageParser());
        imageParserMap.put("tiff", new TiffImageParser());
        imageParserMap.put("bmp", new BmpImageParser());
        imageParserMap.put("png", new PngImageParser());
    }

    /**
     * inner class for filter supported images
     */
    private static class ExtFilter implements FileFilter {

        private Set<String> extensions = new HashSet<String>(Arrays.asList(
                "jpg",
                "jpeg",
                "gif",
                "tif",
                "tiff",
                "bmp",
                "png",
                "pcx"));

        public boolean accept(File pathname) {
            String ext = getExtension(pathname);
            //log("[accept()]: " + pathname.getName() + " have extension '" + ext + "'");
            //log("accept: " + extensions.contains(ext));
            return extensions.contains(ext);
        }

        static public String getExtension(File pathname) {
            String name = pathname.getName();
            int dot = name.lastIndexOf('.');
            if (dot > 0 && dot < name.length() - 1) {
                return name.substring(dot + 1).toLowerCase();
            }
            return "";
        }

    }

}
