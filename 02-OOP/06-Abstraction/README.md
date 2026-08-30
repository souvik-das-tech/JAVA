# Abstraction

Abstraction means exposing only the essential behavior (the "what") while hiding implementation details (the "how"). Java gives you two tools for it: **abstract classes** and **interfaces**.

## Abstract classes

```java
abstract class Shape {
    abstract double area();          // no body — subclass must implement

    void describe() {                // concrete method — shared by all subclasses
        System.out.println("Area = " + area());
    }
}

class Circle extends Shape {
    double radius;
    Circle(double radius) { this.radius = radius; }
    double area() { return Math.PI * radius * radius; }
}
```

- Cannot be instantiated directly (`new Shape()` is a compile error), even if it has zero abstract methods.
- Can mix abstract methods (no body — subclass must implement) with concrete methods (shared implementation) and fields (including non-`final`, non-`static` state).
- A subclass must implement **all** abstract methods, or itself be declared `abstract`.

## Abstract class vs interface

| | Abstract class | Interface |
|---|---|---|
| Instance fields (mutable state) | ✅ Yes | ❌ No (only `public static final` constants) |
| Constructors | ✅ Yes | ❌ No |
| Multiple inheritance | ❌ Single (`extends` one class) | ✅ A class can `implements` many |
| Method bodies | Any mix of abstract/concrete | Abstract by default; `default`/`static` methods can have bodies (Java 8+) |
| Use when | Sharing common state + behavior across closely related classes | Defining a capability/contract unrelated classes can plug into |

## Practice Questions / Exercises

- Write an abstract `Shape` class with an abstract `area()` method and a concrete `describe()` method that uses it — implement `Circle` and `Rectangle` subclasses.
- Try `new Shape()` directly and observe the compiler error, confirming abstract classes can't be instantiated.
- Create a subclass of `Shape` that doesn't implement `area()`, and observe that it must itself be declared `abstract`.
- Rewrite the same example using an interface `Shape` with a `default describe()` method instead of an abstract class — compare what you lose (no instance fields, no constructor) and what you gain (a `Circle` could now also extend another class).

## Interview Questions

**Q: What is abstraction, and how do abstract classes achieve it?**
A: Abstraction means exposing only the essential "what" (a contract of behavior) while hiding the "how" (implementation). Abstract classes achieve it by declaring abstract methods with no implementation — forcing subclasses to provide the "how" — while still allowing shared state and concrete helper methods in the base class itself.

**Q: Can an abstract class have a constructor, if it can never be instantiated directly?**
A: Yes. Its constructor can't be called via `new AbstractClass()`, but it *is* called implicitly whenever a concrete subclass is instantiated (via the implicit or explicit `super()` call), so it's useful for initializing shared state that every subclass needs.

**Q: When would you choose an abstract class over an interface, and vice versa?**
A: Choose an abstract class when related classes share common state and some common implementation, and a single-inheritance "is-a" relationship makes sense. Choose an interface when you're defining a capability/contract that unrelated classes might implement, especially if a class might need to satisfy multiple such contracts (interfaces support multiple implementation) or already extends another class.

**Q: Can an abstract class have zero abstract methods? What's the point of that?**
A: Yes — a class can be declared `abstract` purely to prevent direct instantiation, even with all methods fully implemented. This is used when a class only makes sense as a base to extend (e.g. it represents an incomplete concept on its own), not as a standalone object.

**Q: What happens if a subclass of an abstract class fails to implement one of the abstract methods?**
A: The subclass itself must also be declared `abstract` — the compiler will not allow a concrete (non-abstract) class to leave any inherited abstract method unimplemented, since that would mean some of its instances couldn't provide a real implementation for that method.

**Q: Is abstraction only about abstract classes and interfaces, or is encapsulation also a form of abstraction?**
A: They're related but distinct: encapsulation is about hiding an object's *data* and controlling access to it. Abstraction is a broader concept about hiding *complexity* and exposing only relevant behavior — abstract classes/interfaces are the main OOP mechanism for it, but encapsulation is one of the techniques that supports abstraction by hiding implementation detail from consumers.
