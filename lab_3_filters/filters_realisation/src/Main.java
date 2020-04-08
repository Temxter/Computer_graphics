

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String testImgName = "256_picture_50x50_test.bmp";
        String smileImgName = "256_picture_50x50.bmp";
        String bitImgName = "1bit_picture_50x50_test.bmp";
        String xrayName = "rentgen.bmp";
        String noiseMotherBoardName = "noise_motherboard.bmp";
        String catName = "cat.png";
        String piramidaName = "piramida.gif";
        String morphFilter = "NewMorphologicalFilterDilation256_picture_50x50.bmp";

        ByteImage byteImage = new ByteImage(smileImgName);

        byte [][] filterArray = new byte[][] {
                {0, 0, 0},
                {-1, -1, -1},
                {-1, -1, -1},
        };

        byte [][] filterArrayErosion = new byte[][] {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
        };

        int maxX = 3, maxY = 3;
        int centralX = 1, centralY = 1;
        Filter filter = new Filter(maxX, maxY, centralX, centralY, filterArray);
        Filter filterErosion = new Filter(maxX, maxY, centralX, centralY, filterArrayErosion);

        ByteImage imageWithFilterMin = byteImage.applyStaticFilter(ByteImage.StaticFilters.Min,2);
        ByteImage imageWithFilterMax = byteImage.applyStaticFilter(ByteImage.StaticFilters.Max,2);
        ByteImage imageWithFilterMean = byteImage.applyStaticFilter(ByteImage.StaticFilters.Mean,2);

        imageWithFilterMin.saveImage();
        imageWithFilterMax.saveImage();
        imageWithFilterMean.saveImage();

//        ByteImage imageWithMorphFilterDilation = byteImage.applyMorphologicalFilter(filter, ByteImage.MorphologicalFilters.Dilation);
//        ByteImage imageWithMorphFilterErosion = byteImage.applyMorphologicalFilter(filterErosion, ByteImage.MorphologicalFilters.Erosion);
//        imageWithMorphFilterDilation.saveImage();
//        imageWithMorphFilterErosion.saveImage();
        //byteImage.saveImage("new_image.bmp");

    }

}
