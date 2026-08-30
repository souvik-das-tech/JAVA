# Lambda Expressions

A lambda is a concise, anonymous implementation of a functional interface's single abstract method — syntax for "here's a chunk of behavior," without the boilerplate of an anonymous class.

```java
// Before Java 8:
Runnable r1 = new Runnable() {
    public void run() { System.out.println("Running"); }
};

// With a lambda:
Runnable r2 = () -> System.out.println("Running");
```

## Syntax forms

```java
() -> 42                                   // no params, expression body
x -> x * 2                                 // single param, parens optional
(x, y) -> x + y                            // multiple params
(int x, int y) -> x + y                    // explicit types (usually omitted, inferred)
(x, y) -> { int sum = x + y; return sum; } // block body — needs explicit `return` and `;`
```

- A lambda's type is inferred from the **target context** — a variable declaration, method parameter, or return type expecting a specific functional interface. The same lambda literal `() -> {}` could implement `Runnable`, or a custom `@FunctionalInterface`, depending only on where it's used.
- A lambda can only implement an interface with **exactly one** abstract method (a functional interface — see [[../02-OOP/07-Interfaces]]).

## Variable capture

```java
int factor = 10;
Function<Integer, Integer> multiply = x -> x * factor;   // captures `factor`
factor = 20;   // compile error — factor must be final or effectively final
```

- Lambdas can only capture local variables that are `final` or **effectively final** (never reassigned after initialization) — the lambda captures a *copy* of the value at creation time, not a live reference, so allowing reassignment afterward would be ambiguous about which value the lambda actually "sees."
- Unlike a local/anonymous class, a lambda does **not** introduce its own `this` — `this` inside a lambda refers to the enclosing instance (unlike an anonymous class, where `this` refers to the anonymous class instance itself).

## Practice Questions / Exercises

- Rewrite an anonymous `Runnable` and an anonymous `Comparator<Integer>` as lambdas, and compare the verbosity.
- Write a lambda capturing a local variable, and try reassigning that variable after the lambda is defined — observe the compiler error.
- Store a `Function<Integer, Integer>` lambda in a variable and pass it to a method expecting that functional interface type.
- Compare `this` inside a lambda vs. inside an anonymous class defined in the same method, by printing `this.getClass()` (or similar) from each.

## Interview Questions

**Q: What is a lambda expression, and what determines its type?**
A: A lambda is a concise anonymous implementation of a functional interface's single abstract method. Its type isn't stated explicitly in the lambda itself — the compiler infers it from the *target type* at the point of use (the declared variable type, method parameter type, or expected return type), so the same lambda syntax can implement different functional interfaces in different contexts.

**Q: Why can a lambda only capture local variables that are final or effectively final?**
A: Because the lambda (and its captured state) can outlive the method call that created it — the JVM captures a *copy* of the variable's value at the time the lambda is created, not a live reference to the stack variable (which would no longer exist once the method returns). If reassignment were allowed afterward, it would be unclear whether the lambda should see the old or new value, so Java disallows it entirely by requiring effective finality.

**Q: What is the difference between how `this` behaves inside a lambda vs. inside an anonymous inner class?**
A: Inside a lambda, `this` refers to the *enclosing* instance (lambdas don't introduce their own instance/scope for `this`) — behaving like ordinary code written directly in the enclosing method. Inside an anonymous class, `this` refers to the anonymous class's own instance, and you'd need `EnclosingClass.this` to reach the outer instance explicitly.

**Q: Can a lambda expression throw a checked exception?**
A: Only if the functional interface's abstract method signature declares that checked exception in its `throws` clause. Most standard functional interfaces (`Runnable`, `Function`, `Supplier`, etc.) don't declare any checked exceptions, so a lambda implementing them can't throw one directly — you'd need to catch it inside the lambda body or use a custom functional interface that declares `throws`.

**Q: Are lambdas compiled to anonymous inner classes under the hood?**
A: No — despite the conceptual similarity, lambdas are compiled using the `invokedynamic` bytecode instruction and the `LambdaMetafactory`, which generates the actual implementing class lazily at runtime (the first time that particular lambda expression is executed), rather than the compiler generating a separate `.class` file per lambda at compile time the way it does for anonymous classes. This was a deliberate design choice for better startup performance and smaller class-file footprint.

**Q: What's the benefit of lambdas over anonymous classes beyond just conciseness?**
A: Besides less boilerplate, lambdas don't introduce a new scope for `this`/variable shadowing (reducing a common source of anonymous-class confusion), and their `invokedynamic`-based compilation avoids generating a new `.class` file per usage site — reducing the number of loaded classes in JAR-heavy applications with many callback-style lambdas.
