// Q: Write an abstract Shape class with an abstract double area() method and
// a concrete describe() method that prints "Area = " + area(). Implement
// Circle (radius) and Rectangle (width, height) subclasses. In main, create
// one of each and call describe() on both.

abstract class Shape {
    abstract double area();

    void describe() {
        System.out.println("Area = " + area());
    }
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double width, height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }
}

public class AbstractionDemo {
    public static void main(String[] args) {
        Shape circle = new Circle(3);
        Shape rectangle = new Rectangle(4, 5);

        circle.describe();
        rectangle.describe();
    }
}
