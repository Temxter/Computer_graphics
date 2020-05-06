public class Algorithms {

    public static void paintStepByStep(LinearEquation eq, XYplane plane) {
        for (double x = eq.getX0(); x < eq.getX1(); x++) {
            double y = eq.getK() * x + eq.getB();
            plane.paintPoint(x, y);
        }
    }

    public static void paintCDA(LinearEquation eq, XYplane plane) {
        double y1 = eq.getY(eq.getX1());
        double y0 = eq.getY(eq.getX0());
        double l = Math.abs(eq.getK()) > 1.0 ? y1 - y0 : eq.getX1() - eq.getX0();
        l = Math.abs(l);

        double x = eq.getX0();
        double y = y0;
        plane.paintPoint(x, y);
        for (double i = 0.0; i < l; i++) {
            x += (eq.getX1() - eq.getX0()) / l;
            y += (y1 - y0) / l;
            plane.paintPoint(x, y);
        }
    }

    //work only if x1 > x0
    public static void paintBresenham(LinearEquation eq, XYplane plane) {
        double x0 = eq.getX0();
        double y0 = eq.getY(x0);
        final double kAbs = (Math.abs(eq.getK()) < 1.0) ?
                Math.abs(eq.getK())
                : Math.abs(1.0 / eq.getK());

        double error = kAbs - 0.5;


        double y1 = eq.getY(eq.getX1());

        plane.paintPoint(x0, y0);

        if (Math.abs(eq.getK()) <= 1.0) {
            final double yStep = y1 - y0 > 0.0 ? 1.0 : -1.0; // direction y: (y1 - y0) > 0.0
            for (; x0 < eq.getX1(); x0++) {
                if (error >= 0.0) {
                    y0 += yStep;
                    error -= 1.0;
                }
                error += kAbs;
                plane.paintPoint(x0, y0);
            }
        } else {
            double xStep = 1.0;
            if (y1 < y0) {
                // swap
                double temp = y0;
                y0 = y1;
                y1 = temp;
                x0 = eq.getX1();
                xStep = -1.0;
            }
            for (; y0 < y1; y0++) {
                if (error >= 0.0) {
                    x0 += xStep;
                    error -= 1.0;
                }
                error += kAbs;
                plane.paintPoint(x0, y0);
            }
        }

    }

    public static void paintBresenhamCircle(CircleEquation eq, XYplane plane) {
        double x = -1.0; // -1.0 because x++;
        double y = eq.getR() + 1.0;
        double e = 3.0 - 2.0 * eq.getR();
        double x0 = eq.getX0(), y0 = eq.getY0();

        while (x < y) {

            if (e >= 0.0) {
                e += 4.0 * (x - y) + 10.0;
                x++;
                y--;
            } else {
                e += 4.0 * x + 6.0;
                x++;
            }
            plane.paintPoint(x + x0, y + y0);
            plane.paintPoint(-x + x0, y + y0);
            plane.paintPoint(x + x0, -y + y0);
            plane.paintPoint(-x + x0, -y + y0);
            plane.paintPoint(y + x0, x + y0);
            plane.paintPoint(-y + x0, x + y0);
            plane.paintPoint(y + x0, -x + y0);
            plane.paintPoint(-y + x0, -x + y0);

        }
    }
}
