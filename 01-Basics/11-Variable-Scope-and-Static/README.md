# Variable Scope & the `static` Keyword

## Variable scope

Java has three main kinds of variables, each with different scope and lifetime:

| Kind | Declared | Scope | Lifetime | Default value |
|---|---|---|---|---|
| Local variable | Inside a method/block | From declaration to end of the enclosing `{ }` block | Created on each method call, destroyed when it returns | None — must be explicitly initialized before use |
| Instance variable | Inside a class, not `static` | Whole class (all instance methods) | Lives as long as the object it belongs to | Type-specific default (`0`, `false`, `null`, ...) |
| Static (class) variable | Inside a class, marked `static` | Whole class, shared across **all** instances | Lives for the lifetime of the class (loaded once by the JVM) | Type-specific default |

```java
class Counter {
    static int totalCount = 0;   // static: one copy shared by all Counter objects
    int id;                       // instance: separate copy per object

    Counter() {
        int localTemp = 5;        // local: only exists inside this constructor call
        id = ++totalCount;
    }
}
```

- A local variable in an inner block (e.g. inside an `if`/`for`) is not visible outside that block, even within the same method.
- Instance variables are initialized to defaults automatically; local variables are not — the compiler rejects using an uninitialized local variable ("variable might not have been initialized").

## The `static` keyword

### Static variables
One copy shared across **all instances** of the class — changing it through one object is visible through every other object/reference.

### Static methods
Belong to the class, not an instance — callable as `ClassName.method()` without creating an object. Cannot use `this` and cannot directly access instance (non-static) members, since there's no guaranteed object to act on.

### Static blocks
```java
class Config {
    static final String VERSION;
    static {
        VERSION = loadVersion(); // runs once, when the class is first loaded
    }
}
```
Run once, when the class is loaded by the JVM — useful for one-time static initialization logic that's more complex than a single expression.

### Static nested elements
Classes, and even variables/methods declared `static` inside a class, aren't tied to any particular instance — `Math.PI`, `Math.sqrt()`, and `Integer.MAX_VALUE` are common examples of static members from the standard library.

## Practice Questions / Exercises

- Write a `Counter` class with a `static int totalCount` and an instance `int id`, incrementing `totalCount` in the constructor — create 3 objects and show that `totalCount` is shared while each object's `id` differs.
- Declare a local variable inside an `if` block and try to use it right after the block, outside it, to see the compiler error (then fix by moving the declaration).
- Write a static method and an instance method in the same class; from the static method, try to directly call the instance method and observe the compiler error, then fix it by creating an instance first.
- Add a `static` initialization block to a class that sets a `static final` field, and print that field from `main` to show it ran once at class-loading time.
- Demonstrate that a local variable must be initialized before use by writing code that fails to compile without initialization, and then fixing it.

## Interview Questions

**Q: What's the difference between an instance variable and a static variable?**
A: An instance variable has a separate copy per object — each object's value is independent. A static variable has exactly one copy shared by the entire class and all its instances — changing it through any one reference is visible everywhere.

**Q: Why can't a static method access instance variables or call instance methods directly?**
A: A static method belongs to the class and can be invoked without any object existing. Instance members belong to a specific object (accessed implicitly via `this`), so there's no guaranteed object for the static method to use — it would need an explicit object reference to access them.

**Q: What is the default value of a local variable, and what happens if you use it before initializing it?**
A: Local variables have **no default value** — the compiler performs definite-assignment analysis and refuses to compile code that reads a local variable before it's definitely been assigned ("variable might not have been initialized"). This differs from instance/static fields, which do get automatic type-specific defaults.

**Q: When does a static initializer block run, and how many times?**
A: It runs exactly once, when the JVM first loads the class (e.g. on first reference to it), before any static method is called or object is created. Multiple static blocks in a class run in the order they appear.

**Q: What is variable shadowing, and how does it relate to scope?**
A: Shadowing happens when a variable declared in an inner scope (e.g. a method parameter or local variable) has the same name as one in an outer scope (e.g. an instance field), temporarily hiding the outer one within that inner scope. Inside the inner scope, the name refers to the innermost declaration; the outer one can still be accessed explicitly (e.g. via `this.fieldName` for a shadowed instance field).

**Q: Can a `static` method be overridden the same way an instance method can?**
A: No — static methods are resolved at compile time based on the reference type (not the runtime object type), so a subclass "redefining" a static method with the same signature is technically **method hiding**, not overriding. Calling it through a superclass-typed reference invokes the superclass's version, unlike true (dynamically dispatched) overriding.
