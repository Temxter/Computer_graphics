import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class XYplane {
    private Canvas canvas;
    private double height, width, centerX, centerY;

    private void log(String msg) {
        System.out.println(msg);
    }

    public XYplane(Canvas canvas) {
        this.canvas = canvas;
        height = canvas.getWidth();
        width = canvas.getHeight();
        centerX = width / 2.0;
        centerY = height / 2.0;
        paintXY();
        log("Height = " + height + ", width = " + width);
    }

    public void paintXY() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        // x axis
        g.fillRect(0.0, centerY, width, 1.0);
        int scaleNum = 10;
        double pixelsInSegmentX = width / scaleNum;
        double dashSize = 5.0;
        // x axis: text + dashes
        for (int i = 0; i < scaleNum; i++) {
            g.fillRect(pixelsInSegmentX * i, centerY - dashSize, 1.0, dashSize * 2.0);
            g.fillText(String.valueOf( i * pixelsInSegmentX - centerX), pixelsInSegmentX * i + dashSize,
                    centerY + dashSize * 3.0);
        }

        // y axis
        g.fillRect(centerX, 0.0, 1.0, height);
        double pixelsInSegmentY = height / scaleNum;
        for (int i = 0; i < scaleNum; i++) {
            // point y == 0.0
            if (i == scaleNum/2)
                continue;
            g.fillRect(centerX - dashSize, pixelsInSegmentY * i, dashSize * 2.0, 1.0);
            g.fillText(String.valueOf(centerY - i * pixelsInSegmentY), centerX + dashSize * 2.0, pixelsInSegmentY * i);
        }
    }

    /**
     * Paint point on canvas relative to the start of coordinates
     *
     * @param x
     * @param y
     */
    public void paintPoint(double x, double y) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.fillRect(centerX + x, centerY - y, 1.0, 1.0);
    }
}
