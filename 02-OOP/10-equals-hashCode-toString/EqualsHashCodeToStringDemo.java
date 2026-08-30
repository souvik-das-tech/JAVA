// Q: Write a Point class with int x, y, overriding toString(), equals(), and
// hashCode() (use Objects.hash for hashCode). In main: print a Point directly
// to show toString() runs automatically; create two separately-constructed
// but equal Points and show `==` is false while equals() is true; add both
// to a HashSet<Point> and show only one element remains.

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return this.x == p.x && this.y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class EqualsHashCodeToStringDemo {
    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        System.out.println(p1);

        Point p2 = new Point(1, 2);
        System.out.println("p1 == p2: " + (p1 == p2));
        System.out.println("p1.equals(p2): " + p1.equals(p2));

        Set<Point> points = new HashSet<>();
        points.add(p1);
        points.add(p2);
        System.out.println("HashSet size: " + points.size());
    }
}
