# Design Patterns (Singleton, Factory, Builder, Observer)

Design patterns are reusable, named solutions to recurring design problems — a shared vocabulary, not code to copy-paste blindly.

## Singleton

Ensures a class has **exactly one** instance, with a global access point.

```java
class Singleton {
    private static final Singleton INSTANCE = new Singleton();   // eager initialization — thread-safe by class-loading guarantees

    private Singleton() { }   // private constructor — prevents external instantiation

    static Singleton getInstance() {
        return INSTANCE;
    }
}
```

- Eager initialization (as above) is simplest and thread-safe (the JVM guarantees a class's static initializers run exactly once, before first use), at the cost of creating the instance even if it's never used.
- Lazy initialization needs care for thread-safety — the common correct approach is the "initialization-on-demand holder" idiom (a nested static class, lazily loaded on first access) rather than manual double-checked locking.

## Factory

Encapsulates object creation logic behind a method, so the caller doesn't need to know which concrete class to instantiate.

```java
interface Shape { void draw(); }
class Circle implements Shape { public void draw() { System.out.println("Circle"); } }
class Square implements Shape { public void draw() { System.out.println("Square"); } }

class ShapeFactory {
    static Shape create(String type) {
        return switch (type) {
            case "circle" -> new Circle();
            case "square" -> new Square();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}
```

- Decouples calling code from concrete classes — adding a new `Shape` type means changing only the factory, not every place that creates shapes.

## Builder

Constructs a complex object step-by-step, avoiding a constructor with many (often optional) parameters ("telescoping constructor" problem).

```java
class Pizza {
    private final String size;
    private final boolean cheese, pepperoni;

    private Pizza(Builder b) {
        this.size = b.size; this.cheese = b.cheese; this.pepperoni = b.pepperoni;
    }

    static class Builder {
        private String size = "medium";
        private boolean cheese, pepperoni;

        Builder size(String size) { this.size = size; return this; }
        Builder cheese(boolean v) { this.cheese = v; return this; }
        Builder pepperoni(boolean v) { this.pepperoni = v; return this; }
        Pizza build() { return new Pizza(this); }
    }
}

Pizza p = new Pizza.Builder().size("large").cheese(true).build();
```

## Observer

Defines a one-to-many dependency — when one object (the **subject**) changes state, all its registered **observers** are notified automatically.

```java
interface Observer { void update(String event); }

class EventPublisher {
    private final List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o) { observers.add(o); }
    void publish(String event) {
        for (Observer o : observers) o.update(event);
    }
}
```

- The foundation of GUI event listeners, pub-sub systems, and reactive programming.

## Practice Questions / Exercises

- Implement an eager `Singleton` and confirm (via `==`) that two calls to `getInstance()` return the exact same object.
- Implement a `ShapeFactory` creating `Circle`/`Square`/`Triangle` from a string, and add a new shape type by changing only the factory.
- Implement a `Pizza` with a fluent `Builder`, building two different pizzas with different combinations of options.
- Implement an `EventPublisher`/`Observer` pair with two different observer implementations subscribed to the same publisher, and publish an event to both.

## Interview Questions

**Q: Why is a private constructor essential to the Singleton pattern?**
A: It prevents any code outside the class from creating additional instances via `new Singleton()` — the only way to obtain an instance is through the class's own controlled access point (`getInstance()`), which is what actually enforces the "exactly one instance" guarantee.

**Q: What problem does the Factory pattern solve?**
A: It decouples the code that *needs* an object from the code that *decides which concrete class* to instantiate — callers depend only on an interface/abstract type and a factory method, so adding a new concrete implementation requires changing only the factory, not every call site that creates that kind of object.

**Q: What problem does the Builder pattern solve that a constructor with many parameters doesn't?**
A: It avoids the "telescoping constructor" problem — many overloaded constructors (or one constructor with many, often optional, positional parameters that are easy to mix up or misorder). A builder's fluent, named method calls (`.size("large").cheese(true)`) make each parameter's meaning explicit at the call site and let you omit ones that have sensible defaults, in any order.

**Q: How does the Observer pattern relate to how GUI frameworks or event systems work?**
A: It's the direct foundation of them — a button (the subject) maintains a list of registered click listeners (observers) and calls each one's callback method when clicked, without the button needing any compile-time knowledge of what those listeners actually do. The same subject/observer decoupling underlies pub-sub messaging systems and reactive streams.

**Q: What's a downside of the classic eager Singleton implementation?**
A: The instance is created as soon as the class is loaded, even if the application never actually ends up needing it — wasting the cost of construction (and any resources it allocates) for a potentially unused object. Lazy initialization avoids this but requires more careful handling to remain thread-safe (e.g. the initialization-on-demand holder idiom, which relies on the JVM's class-loading guarantees rather than manual locking).

**Q: Can these patterns be combined? Give an example.**
A: Yes, commonly — a Factory method might itself be implemented as (or live inside) a Singleton (one shared factory instance), and a Builder is often used *within* a Factory to assemble a complex object before returning it. Patterns describe reusable design shapes, not mutually exclusive choices, so real code frequently layers several together.
