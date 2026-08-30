# Method References

A method reference is shorthand for a lambda that does nothing but call an **existing** method — same functional-interface typing rules as lambdas apply, just terser syntax.

## The four forms

```java
// 1. Static method reference
Function<String, Integer> f1 = Integer::parseInt;         // same as: s -> Integer.parseInt(s)

// 2. Instance method reference on a PARTICULAR object
String greeting = "Hello";
Supplier<Integer> f2 = greeting::length;                  // same as: () -> greeting.length()

// 3. Instance method reference on an ARBITRARY object of a type (most common with streams)
Function<String, Integer> f3 = String::length;             // same as: s -> s.length()
Comparator<String> f4 = String::compareTo;                  // same as: (a, b) -> a.compareTo(b)

// 4. Constructor reference
Supplier<ArrayList<String>> f5 = ArrayList::new;            // same as: () -> new ArrayList<>()
Function<String, StringBuilder> f6 = StringBuilder::new;    // same as: s -> new StringBuilder(s)
```

## Form 3 explained (the trickiest one)

`String::length` looks like it takes zero arguments, but as a `Function<String, Integer>` it actually takes **one** — the `String` itself becomes the receiver the method is called *on*, not a value passed to it. `s -> s.length()` and `String::length` are exactly equivalent; the "arbitrary object" (`s`) is implicitly the first parameter of the functional interface's abstract method.

## When you can (and can't) use a method reference

You can replace a lambda with a method reference **only if the lambda does nothing but call one existing method**, forwarding its arguments unchanged.

```java
Function<Integer, Integer> ok = x -> Math.abs(x);      // -> Math::abs
Function<Integer, Integer> ok2 = Math::abs;              // equivalent, terser

Function<Integer, Integer> notEligible = x -> Math.abs(x) + 1;   // extra logic — can't be a method reference
```

## Practice Questions / Exercises

- Rewrite `s -> s.toUpperCase()` as a method reference, and use it in `list.stream().map(...)`.
- Use a static method reference (`Integer::parseInt`) to convert a `List<String>` of numbers into a `List<Integer>` via `stream().map(...)`.
- Use a constructor reference (`ArrayList::new`) as a `Supplier` passed to `Collectors.toCollection(...)`.
- Write a bound instance method reference on a specific object (e.g. a `Predicate` calling `someString::startsWith` isn't quite right due to arity — instead try `System.out::println` as a `Consumer<String>`).

## Interview Questions

**Q: What are the four kinds of method references in Java?**
A: (1) Static method reference — `ClassName::staticMethod`. (2) Bound instance method reference — `particularObject::instanceMethod`. (3) Unbound instance method reference — `ClassName::instanceMethod`, where the first parameter of the functional interface becomes the receiver the method is called on. (4) Constructor reference — `ClassName::new`.

**Q: In `String::length` used as a `Function<String, Integer>`, where does the `String` argument actually go?**
A: It becomes the implicit receiver the `length()` method is called *on*, not an argument passed *to* it — `String::length` is equivalent to `s -> s.length()`. This "unbound instance method reference" form is why a method reference to an instance method can still satisfy a functional interface with one parameter, even though the referenced method itself takes zero explicit arguments.

**Q: When can a lambda be replaced with a method reference, and when can't it?**
A: Only when the lambda's entire body is a single call to an existing method (static or instance), forwarding all its parameters unchanged and in the same order, with no additional logic. If the lambda does anything extra (extra computation, multiple statements, reordered/transformed arguments), it can't be expressed as a plain method reference.

**Q: Is a method reference compiled any differently than an equivalent lambda?**
A: No — both use the same `invokedynamic`/`LambdaMetafactory` mechanism under the hood; a method reference is purely syntactic sugar for the equivalent lambda, with no meaningful runtime behavior difference (aside from occasionally slightly different bytecode shape, which isn't something application code needs to care about).

**Q: How does `ArrayList::new` work as a `Supplier<List<String>>`?**
A: It's a constructor reference — the compiler generates the equivalent of `() -> new ArrayList<>()`, matching `Supplier<T>`'s no-argument `get()` method by invoking the no-arg constructor each time `get()` is called. Constructor references can also match multi-argument functional interfaces if a matching constructor overload exists (e.g. `Function<Integer, ArrayList<String>>` mapping to `ArrayList(int initialCapacity)`).

**Q: Why are method references generally preferred over an equivalent lambda when available?**
A: Purely for readability/conciseness — `String::toUpperCase` immediately communicates "call this existing, presumably well-understood method" more clearly than `s -> s.toUpperCase()`, especially when chained repeatedly in stream pipelines. There's no functional or performance difference; it's a style preference the Java community has broadly converged on for simple pass-through cases.
