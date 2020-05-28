import javafx.scene.paint.Color;

public class Algorithms {

    public static void paintCutLine(LineSegment cutbox, LineSegment line, XYplane plane) {
        double  yMin = cutbox.getY0(),
                yMax = cutbox.getY1(),
                xMin = cutbox.getX0(),
                xMax = cutbox.getX1(),
                p2 = line.getX1() - line.getX0(),
                p1 = -p2,
                p4 = line.getY1() - line.getY0(),
                p3 = -p4,
                q1 = line.getX0() - xMin,
                q2 = xMax - line.getX0(),
                q3 = line.getY0() - yMin,
                q4 = yMax - line.getY0(),
                tIn = 0.0,
                tOut = 1.0;

        double Q [] = {q1, q2, q3, q4};
        double P [] = {p1, p2, p3, p4};
        boolean inBox = true;

        for (int i = 0; i < 4; i++) {
            if (P[i] > 0) {
                tOut = Math.min(Q[i] / P[i], tOut);
            } else if (P[i] < 0) {
                tIn = Math.max(Q[i] / P[i], tIn);
            } else {
                if (Q[i] >= 0.0)
                    continue;
                else { // line not inside box
                    inBox = false;
                    break;
                }
            }
        }

        plane.paintLine(line, Color.BLACK);

        log(String.format("[] tIn = %f, tOut = %f", tIn, tOut));

        if (inBox) {

            LineSegment cutLine = new LineSegment(line.getX(tIn), line.getY(tIn),
                    line.getX(tOut), line.getY(tOut));

            log("Cut line: " + cutLine);
            plane.paintLine(cutLine, Color.YELLOW);
        }
    }

    static private void log(String s) {
        System.out.println("[Algorithms]: " + s);
    }




}
