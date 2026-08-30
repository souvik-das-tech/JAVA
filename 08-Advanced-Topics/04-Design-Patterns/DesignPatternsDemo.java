// Q: Implement an eager Singleton and confirm two getInstance() calls return
// the same object via ==. Implement a ShapeFactory creating Circle/Square
// from a string. Implement a Pizza with a fluent Builder, building two
// different pizzas. Implement an EventPublisher/Observer pair with two
// observers subscribed, and publish an event to both.

import java.util.ArrayList;
import java.util.List;

class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    static Singleton getInstance() {
        return INSTANCE;
    }
}

interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Square implements Shape {
    public void draw() {
        System.out.println("Drawing Square");
    }
}

class ShapeFactory {
    static Shape create(String type) {
        return switch (type) {
            case "circle" -> new Circle();
            case "square" -> new Square();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

class Pizza {
    private final String size;
    private final boolean cheese, pepperoni;

    private Pizza(Builder b) {
        this.size = b.size;
        this.cheese = b.cheese;
        this.pepperoni = b.pepperoni;
    }

    @Override
    public String toString() {
        return "Pizza(size=" + size + ", cheese=" + cheese + ", pepperoni=" + pepperoni + ")";
    }

    static class Builder {
        private String size = "medium";
        private boolean cheese, pepperoni;

        Builder size(String size) {
            this.size = size;
            return this;
        }

        Builder cheese(boolean v) {
            this.cheese = v;
            return this;
        }

        Builder pepperoni(boolean v) {
            this.pepperoni = v;
            return this;
        }

        Pizza build() {
            return new Pizza(this);
        }
    }
}

interface Observer {
    void update(String event);
}

class EventPublisher {
    private final List<Observer> observers = new ArrayList<>();

    void subscribe(Observer o) {
        observers.add(o);
    }

    void publish(String event) {
        for (Observer o : observers) o.update(event);
    }
}

public class DesignPatternsDemo {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println("s1 == s2: " + (s1 == s2));

        ShapeFactory.create("circle").draw();
        ShapeFactory.create("square").draw();

        Pizza p1 = new Pizza.Builder().size("large").cheese(true).build();
        Pizza p2 = new Pizza.Builder().pepperoni(true).build();
        System.out.println(p1);
        System.out.println(p2);

        EventPublisher publisher = new EventPublisher();
        publisher.subscribe(event -> System.out.println("Observer A received: " + event));
        publisher.subscribe(event -> System.out.println("Observer B received: " + event));
        publisher.publish("order placed");
    }
}
