# Nested, Inner, Static Nested & Anonymous Classes

Java allows classes to be declared inside another class. There are four flavors:

## Static nested class

Declared `static` inside another class. Doesn't hold an implicit reference to an instance of the outer class — behaves like a regular top-level class, just namespaced inside the outer one.

```java
class Outer {
    static class Nested {
        void greet() { System.out.println("Hi from nested"); }
    }
}
Outer.Nested n = new Outer.Nested();   // no Outer instance needed
```

## Inner (non-static) class

Declared without `static`. Holds an implicit reference to the **enclosing instance** — cannot exist without one, and can directly access the outer object's instance members (even `private` ones).

```java
class Outer {
    int value = 10;
    class Inner {
        void show() { System.out.println("Outer value = " + value); }  // implicit outer reference
    }
}
Outer o = new Outer();
Outer.Inner in = o.new Inner();   // needs an Outer instance to create
```

## Local class

Declared inside a method body — scoped to that method, can access effectively-final local variables of the enclosing method.

## Anonymous class

A local class with no name, declared and instantiated in a single expression — typically used to provide a one-off implementation of an interface or to extend a class inline.

```java
Runnable r = new Runnable() {
    @Override
    public void run() { System.out.println("Running anonymously"); }
};
```

- An anonymous class can implement one interface or extend one class, never both, and can't have a named constructor.
- Since Java 8, lambdas usually replace anonymous classes for single-abstract-method (functional) interfaces — anonymous classes are still needed when implementing an interface with more than one method, or when extending a class.

## Practice Questions / Exercises

- Write an `Outer` class with a `static Nested` class — instantiate `Nested` without ever creating an `Outer` instance.
- Write an `Outer` class with a non-static `Inner` class that reads a private field of `Outer` — instantiate it via `outer.new Inner()`.
- Inside a method, declare a local class that implements an interface, using an effectively-final local variable from the method inside the local class's method body.
- Create an anonymous class implementing `Runnable` (or `Comparator<Integer>`) inline, and separately do the same task with a lambda — compare the two.

## Interview Questions

**Q: What is the key difference between a static nested class and a (non-static) inner class?**
A: A static nested class has no reference to an instance of the outer class and can be instantiated independently (`Outer.Nested n = new Outer.Nested();`). An inner class implicitly holds a reference to the specific outer instance that created it and cannot exist without one (`outer.new Inner()`), which lets it directly access that outer instance's members.

**Q: Why would you use a static nested class instead of just a separate top-level class?**
A: For namespacing/organization — grouping a helper class tightly with the outer class it's logically part of (e.g. `Map.Entry` is a static nested interface inside `Map`), signaling it's not meant to be used independently of the outer class's context, without paying the cost of holding an outer-instance reference.

**Q: Can a local class or anonymous class access local variables of the enclosing method? Under what condition?**
A: Yes, but only variables that are `final` or "effectively final" (never reassigned after initialization) — because the local/anonymous class instance may outlive the method's stack frame, so the compiler captures a copy of the variable's value at creation time rather than a live reference.

**Q: What's the main practical difference between an anonymous class and a lambda expression?**
A: An anonymous class can implement an interface with multiple methods, extend a concrete/abstract class, and introduce its own instance fields or additional methods. A lambda can only implement a functional interface's single abstract method and has no fields of its own — it's more concise but strictly more limited in what it can express.

**Q: Does `this` inside an inner class refer to the inner or the outer instance? How do you reference the outer instance explicitly?**
A: `this` inside an inner class refers to the inner class instance itself, same as in any class. To explicitly reference the enclosing outer instance (e.g. if a field name is shadowed), use `OuterClassName.this` — e.g. `Outer.this.value`.

**Q: Why can't a static nested class directly access the outer class's non-static (instance) members?**
A: Because a static nested class isn't tied to any particular `Outer` instance — there's no implicit enclosing-instance reference for it to use, just like a regular static method can't access instance members without an explicit object reference.
