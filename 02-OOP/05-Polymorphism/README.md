# Polymorphism

Polymorphism ("many forms") lets the same method call behave differently depending on the object it acts on. Java has two kinds:

## Compile-time polymorphism — method overloading

Multiple methods in the same class with the **same name** but **different parameter lists** (count, type, or order). Resolved at **compile time**, based on the declared argument types.

```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
}
```

- Return type alone does **not** distinguish overloads — two methods differing only in return type won't compile.
- The compiler picks the "most specific" applicable overload; widening/autoboxing conversions are used only if no exact match exists.

## Runtime polymorphism — method overriding

A subclass redefines a method it inherited from its superclass, with the **same signature**. Resolved at **runtime**, based on the actual object type (dynamic dispatch) — not the reference type.

```java
class Animal {
    void sound() { System.out.println("..."); }
}
class Cat extends Animal {
    @Override
    void sound() { System.out.println("Meow"); }
}

Animal a = new Cat();
a.sound();   // prints "Meow" — decided by the actual object type at runtime, not the Animal reference type
```

- The `@Override` annotation isn't required but is strongly recommended — it makes the compiler verify the signature actually matches a parent method (catches typos that would otherwise silently create an *overload* instead of an override).
- Overriding rules: same method signature, same or covariant return type, same or narrower checked exceptions, same or wider access modifier.
- Only instance methods are dynamically dispatched — `static` methods are resolved at compile time based on reference type (this is "method hiding", not overriding), and fields are also resolved statically (no polymorphism for fields).

## Practice Questions / Exercises

- Write a `Calculator` class with three overloaded `add` methods (different parameter counts/types) and call each to show the compiler picks based on argument types.
- Write an `Animal` superclass and `Cat`/`Dog` subclasses, each overriding a `sound()` method — store them in an `Animal[]` array and call `sound()` on each to show runtime dispatch picks the right override.
- Declare a variable as the superclass type (`Animal a = new Cat();`) and show that you can only call methods declared on `Animal` through it, even though the actual object is a `Cat` (unless you cast).
- Demonstrate method hiding: give a superclass and subclass a `static` method with the same signature, call it through a superclass-typed reference holding a subclass object, and show it calls the superclass's version (unlike an overridden instance method).

## Interview Questions

**Q: What is the difference between method overloading and method overriding?**
A: Overloading is having multiple methods with the same name but different parameter lists in the same class — resolved at compile time based on argument types (static/early binding). Overriding is a subclass redefining an inherited method with the exact same signature — resolved at runtime based on the actual object type (dynamic/late binding).

**Q: Can you overload a method by changing only its return type?**
A: No. The compiler needs the parameter list alone to disambiguate which overload is being called at a call site; if only the return type differed, `int add(int,int)` and `double add(int,int)` called as `add(1,2)` would be ambiguous, so Java disallows it.

**Q: What is dynamic method dispatch, and why is it central to runtime polymorphism?**
A: It's the mechanism by which the JVM decides, at runtime, which overridden method implementation to actually execute — based on the real (runtime) type of the object the reference points to, not the reference's declared (compile-time) type. This is what lets `Animal a = new Cat(); a.sound();` call `Cat`'s version even though `a` is typed as `Animal`.

**Q: Why can't `static` methods be overridden?**
A: Static methods belong to the class, not to any object instance, and are resolved at compile time based on the reference's declared type — there's no object to dynamically dispatch on. A subclass defining a `static` method with the same signature merely *hides* the superclass's version rather than overriding it.

**Q: What are the rules a method must follow to validly override a superclass method?**
A: Same method name and parameter list; same return type or a covariant (subtype) return type; the same or a narrower set of checked exceptions than the overridden method; and the same or a wider (never narrower) access modifier.

**Q: If a subclass overrides `toString()`, and you call it through a superclass reference, which version runs?**
A: The subclass's overridden version — because `toString()` is an instance method and instance methods are always dynamically dispatched based on the actual object's runtime type, regardless of the reference's declared type.
