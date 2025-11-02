import java.util.PriorityQueue;
import java.util.Queue;

class Point implements Comparable<Point> {
    final int x;
    final int y;
    final double distSq; // квадрат відстані до центру

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.distSq = (double) x * x + (double) y * y; 
    }
    
    @Override
    public int compareTo(Point other) {
        // this.distSq < other.distSq, повертаємо від'ємне число (вищий пріоритет)
        return Double.compare(this.distSq, other.distSq);
    }
    
    @Override
    public String toString() {
        return String.format("P(%d, %d) | distance^2: %.2f", x, y, distSq);
    }
}

public class B08_04 {
    public static void sortPointsByDistance() {
        // PriorityQueue автоматично створює min-heap (найменше значення має найвищий пріоритет)
        Queue<Point> pq = new PriorityQueue<>();

        pq.add(new Point(3, 4));  // 25
        pq.add(new Point(1, 1));  // 2
        pq.add(new Point(5, 0));  // 25
        pq.add(new Point(-2, 2)); // 8
        pq.add(new Point(0, 10)); // 100

        System.out.println("✅ points sorted by increasing distance to the center (0,0):");
        
        // вони будуть виходити у відсортованому порядку
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }

    public static void main(String[] args) {
        sortPointsByDistance();
    }
}