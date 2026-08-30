# Interfaces

An interface defines a contract — a set of methods a class promises to implement — without (traditionally) providing any implementation itself. A class uses `implements` to fulfill an interface, and can implement **multiple** interfaces (unlike single class inheritance).

```java
interface Drivable {
    void drive();               // implicitly public abstract
}

class Car implements Drivable {
    public void drive() { System.out.println("Vroom"); }
}
```

- All interface fields are implicitly `public static final` (constants) — you cannot have instance state in an interface.
- All abstract methods are implicitly `public abstract` — no need to write those modifiers.
- A class can `implements` any number of interfaces, resolving Java's lack of multiple class inheritance for behavior contracts.

## Default & static methods (Java 8+)

```java
interface Greeter {
    default void greet() { System.out.println("Hello!"); }   // has a body — subclass may override, or inherit as-is
    static Greeter simple() { return () -> System.out.println("Hi"); }  // called as Greeter.simple(), not on an instance
}
```

- **Default methods** let interfaces evolve (add new methods) without breaking every existing implementing class — implementers get the default behavior for free unless they choose to override it.
- **Static methods** on an interface belong to the interface itself, called as `InterfaceName.method()`, not through an implementing instance.
- If a class implements two interfaces with the same default method signature, it must **explicitly override** that method (the compiler won't guess which default to inherit) — it can still call a specific one via `InterfaceName.super.method()`.

## Functional interfaces

An interface with exactly **one** abstract method (default/static methods don't count) — eligible for lambda expressions and method references. Optionally annotated `@FunctionalInterface` (compiler then enforces the single-abstract-method rule).

```java
@FunctionalInterface
interface Calculator {
    int operate(int a, int b);
}

Calculator add = (a, b) -> a + b;   // lambda implementing Calculator
```

## Practice Questions / Exercises

- Write a `Drivable` interface with a `drive()` method, implement it in `Car` and `Bike` classes, and store both in a `Drivable[]` array to call `drive()` polymorphically.
- Add a `default` method `honk()` to `Drivable` that prints a generic message, and override it in just one implementing class.
- Create two interfaces with a same-signature default method, implement both in one class, and resolve the conflict with an explicit override that calls one via `InterfaceName.super.method()`.
- Write a functional interface `Calculator` with a single abstract method `operate(int,int)`, and implement it three different ways: an anonymous class, a lambda, and a method reference.

## Interview Questions

**Q: Why were default methods added to interfaces in Java 8?**
A: To let interface authors add new methods to an existing interface (e.g. the Collections Framework interfaces gaining stream-related methods) without breaking every class that already implements it — implementing classes automatically inherit the default implementation instead of failing to compile.

**Q: Can an interface have instance fields (mutable state)?**
A: No. Any field declared in an interface is implicitly `public static final` — a compile-time constant shared across all users of the interface, not per-instance state. This is a key distinction from abstract classes, which can hold real instance state.

**Q: What is a functional interface, and why does it matter for lambdas?**
A: An interface with exactly one abstract method (default/static methods don't count toward that). It matters because a lambda expression's type is inferred as an implementation of a functional interface's single abstract method — a lambda literally *is* an anonymous implementation of that one method.

**Q: If a class implements two interfaces that each declare a default method with the identical signature, what happens?**
A: It's a compile error unless the class explicitly overrides that method itself — Java refuses to arbitrarily pick one interface's default over the other. The override can still delegate to a specific interface's version using `InterfaceName.super.methodName()`.

**Q: What's the difference between an interface's `static` method and a `default` method?**
A: A `static` method belongs to the interface type itself and is called as `InterfaceName.method()` — it is not inherited by implementing classes and can't be overridden. A `default` method is an instance method with a body that implementing classes inherit automatically and may override.

**Q: Since Java 8 let interfaces have method bodies (default/static), what functional difference remains between an interface and an abstract class?**
A: An interface still can't hold instance state (only constants), can't have constructors, and a class can implement many interfaces but extend only one abstract class. So interfaces remain purely about contracts/capabilities (with optional shared default behavior), while abstract classes remain the tool for sharing actual object state plus behavior across a single-inheritance hierarchy.
