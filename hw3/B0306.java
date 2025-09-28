import java.util.HashMap;  // дозволяють коду використовувати класи з інших пакетів, не вказуючи повний шлях до них щоразу
import java.util.Map;  // для зберігання даних

class Point {
    private double x;
    private double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public static double distance(Point p1, Point p2) { 
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

class Quad {
    private Point a;
    private Point b;
    private Point c;
    private Point d;
    public Quad(Point a, Point b, Point c, Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public double getP() {
        double sideAB = Point.distance(a, b);
        double sideBC = Point.distance(b, c);
        double sideCD = Point.distance(c, d);
        double sideDA = Point.distance(d, a);
        return sideAB + sideBC + sideCD + sideDA;
    }
    public String getType() {
        double sideAB = Point.distance(a, b);
        double sideBC = Point.distance(b, c);
        double sideCD = Point.distance(c, d);
        double sideDA = Point.distance(d, a);
        double diagAC = Point.distance(a, c);
        double diagBD = Point.distance(b, d);
        final double EPSILON = 1e-9;  // !
        if (Math.abs(sideAB - sideBC) < EPSILON &&
            Math.abs(sideBC - sideCD) < EPSILON &&
            Math.abs(sideCD - sideDA) < EPSILON &&
            Math.abs(diagAC - diagBD) < EPSILON) {
            return "square";
        }
        if (Math.abs(sideAB - sideCD) < EPSILON &&
            Math.abs(sideBC - sideDA) < EPSILON &&
            Math.abs(diagAC - diagBD) < EPSILON) {
            return "rectangle";
        }
        if (Math.abs(sideAB - sideBC) < EPSILON &&
            Math.abs(sideBC - sideCD) < EPSILON &&
            Math.abs(sideCD - sideDA) < EPSILON) {
            return "diamond";
        }
        return "some";
    }
}

public class B0306 {
    public static Map<String, Integer> countQuadTypes(Quad[] quads) {
        Map<String, Integer> counts = new HashMap<>();
        
        counts.put("square", 0);
        counts.put("rectangle", 0);
        counts.put("diamond", 0);
        counts.put("some", 0);

        for (Quad q : quads) {
            String type = q.getType();
            counts.put(type, counts.get(type) + 1);
        }
        return counts;
    }

    public static Quad getLargestPQuad(Quad[] quads) {
        if (quads == null || quads.length == 0) {
            return null;
        }

        Quad largest = quads[0];
        double maxP = largest.getP();

        for (int i = 1; i < quads.length; i++) {
            double currentP = quads[i].getP();
            if (currentP > maxP) {
                maxP = currentP;
                largest = quads[i];
            }
        }
        return largest;
    }

    public static void main(String[] args) {
        Quad square = new Quad(
            new Point(0, 0), new Point(2, 0), new Point(2, 2), new Point(0, 2)
        );
        Quad rectangle = new Quad(
            new Point(0, 0), new Point(4, 0), new Point(4, 2), new Point(0, 2)
        );
        Quad diamond = new Quad(
            new Point(0, 0), new Point(3, 4), new Point(6, 0), new Point(3, -4)
        );
        Quad some = new Quad(
            new Point(1, 1), new Point(5, 2), new Point(6, 6), new Point(2, 5)
        );
        Quad[] myQuads = {square, rectangle, diamond, some};
        Map<String, Integer> counts = countQuadTypes(myQuads);
        System.out.println("number:");
        counts.forEach((type, count) -> System.out.println(type + ": " + count));
        System.out.println("\n---");
        Quad largest = getLargestPQuad(myQuads);
        if (largest != null) {
            System.out.println("largest perimeter");
            System.out.printf("type: %s\n", largest.getType());
            System.out.printf("perimeter: %.2f\n", largest.getP());
        }
    }
}