// Q: Write a Point class with fields x and y. Give it a parameterized
// constructor Point(int x, int y), and a no-arg constructor Point() that
// chains to it via this(0, 0) to default to the origin. In main, create one
// Point using each constructor and print both.

class Point {
    int x, y;

    Point() {
        this(0, 0);
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }
}

public class ConstructorsDemo {
    public static void main(String[] args) {
        Point origin = new Point();
        Point p = new Point(3, 4);

        System.out.println(origin);
        System.out.println(p);
    }
}
