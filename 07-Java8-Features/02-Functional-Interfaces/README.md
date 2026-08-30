# Functional Interfaces (`Function`, `Predicate`, `Supplier`, `Consumer`)

`java.util.function` provides a standard set of general-purpose functional interfaces so you don't need to declare your own for common shapes.

| Interface | Method | Signature | Use |
|---|---|---|---|
| `Function<T, R>` | `apply(T) -> R` | takes one, returns a (possibly different type) result | Transform a value |
| `Predicate<T>` | `test(T) -> boolean` | takes one, returns boolean | A yes/no check (filtering) |
| `Supplier<T>` | `get() -> T` | takes nothing, returns a value | Lazily produce/generate a value |
| `Consumer<T>` | `accept(T) -> void` | takes one, returns nothing | Perform a side effect on a value |
| `BiFunction<T,U,R>` | `apply(T,U) -> R` | takes two, returns a result | Combine two values |
| `UnaryOperator<T>` | `apply(T) -> T` | `Function<T,T>` specialization | Transform a value to the same type |
| `BinaryOperator<T>` | `apply(T,T) -> T` | `BiFunction<T,T,T>` specialization | Combine two values of the same type |

```java
Function<String, Integer> length = String::length;
Predicate<Integer> isEven = n -> n % 2 == 0;
Supplier<Double> random = Math::random;
Consumer<String> printer = System.out::println;

length.apply("hello");     // 5
isEven.test(4);            // true
random.get();               // some double
printer.accept("hi");       // prints "hi"
```

## Composition

Most of these interfaces provide default methods for chaining, avoiding manual nesting.

```java
Function<Integer, Integer> addOne = x -> x + 1;
Function<Integer, Integer> square = x -> x * x;

addOne.andThen(square).apply(3);   // square(addOne(3)) = square(4) = 16
addOne.compose(square).apply(3);   // addOne(square(3)) = addOne(9) = 10

Predicate<Integer> isPositive = x -> x > 0;
isEven.and(isPositive).test(4);    // true — both must hold
isEven.or(isPositive).test(-3);    // true — at least one holds
isEven.negate().test(4);           // false — inverts the result
```

- `andThen(after)` — runs `this` first, then feeds its result into `after`.
- `compose(before)` — runs `before` first, then feeds its result into `this` (reverse order from `andThen`).

## Practice Questions / Exercises

- Use `Predicate<String>` to filter a `List<String>` for words longer than 4 characters (combine with `Stream.filter` from the Streams topic, or just loop manually).
- Chain two `Function`s with `andThen` and the same two with `compose`, and show the different results.
- Combine two `Predicate`s with `.and()`, `.or()`, and `.negate()` and test each combination.
- Write a `Supplier<String>` that returns a random greeting from a fixed list, and call it a few times.

## Interview Questions

**Q: What's the difference between `Function<T, R>` and `UnaryOperator<T>`?**
A: `UnaryOperator<T>` is just `Function<T, T>` — a specialization where the input and output types are the same. It exists so that, for example, a method taking "a transformation that keeps the same type" can express that constraint directly in its signature rather than relying on the caller happening to supply matching types to a general `Function<T, R>`.

**Q: What's the difference between `andThen()` and `compose()` on `Function`?**
A: Both chain two functions, but in opposite order. `f.andThen(g)` applies `f` first, then passes its result into `g` — equivalent to `g(f(x))`. `f.compose(g)` applies `g` first, then passes its result into `f` — equivalent to `f(g(x))`.

**Q: What's the purpose of `Predicate.and()`, `.or()`, and `.negate()`?**
A: They let you compose simple predicates into more complex boolean logic without writing a new lambda by hand — `p1.and(p2)` requires both to be true, `p1.or(p2)` requires at least one, `p1.negate()` inverts the result — all returning a new `Predicate` you can further compose or pass around.

**Q: Why does `Supplier<T>` take no arguments, and when is it useful?**
A: It represents "a source of a value" rather than a transformation — useful for lazy or deferred computation (the value isn't computed until `get()` is actually called), such as providing a default value only if needed (`Optional.orElseGet(supplier)`), or generating fresh values on demand (factory methods, random values).

**Q: Can you write your own functional interface instead of using the standard `java.util.function` ones? When would you?**
A: Yes — any interface with exactly one abstract method qualifies. You'd write your own when a standard interface's generic method name (`apply`, `test`, `accept`) doesn't convey the domain meaning clearly, or when you need a method with a different arity/checked-exception signature the standard library doesn't provide (e.g. a `ThrowingFunction<T, R>` whose `apply` declares `throws Exception`).

**Q: What is the relationship between `BiFunction<T, U, R>` and `BinaryOperator<T>`?**
A: `BinaryOperator<T>` is a specialization of `BiFunction<T, T, T>` — both input types and the output type are the same `T`. It's used for operations that combine two values of the same type into one of that type, like `Integer::sum` or `(a, b) -> a.compareTo(b) > 0 ? a : b` for a max operation — exactly the shape `Stream.reduce()` expects for its accumulator.
