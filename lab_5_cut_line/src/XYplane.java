import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    public void paintLine(LineSegment line, Color color) {
        paintLine(line.getX0(), line.getY0(), line.getX1(), line.getY1(), color);
    }

    public void paintLine(double x0, double y0, double x1, double y1, Color color) {
        if (color == null)
            color = Color.BLACK;
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setStroke(color);
        g.strokeLine(centerX + x0, centerY - y0,
                centerX + x1, centerY - y1);
        g.setStroke(Color.BLACK);
    }

    public void paintRectangle(LineSegment rect, Color color) {
        paintRectangle(rect.getX0(), rect.getY0(), rect.getX1(), rect.getY1(), color);
    }

    public void paintRectangle(double x0, double y0, double x1, double y1, Color color) {
        if (color == null)
            color = Color.BLACK;
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setStroke(color);
        g.strokeRect(centerX + x0, centerY - y1,
                x1 - x0, y1 - y0);
        g.setStroke(Color.BLACK);
    }
}
