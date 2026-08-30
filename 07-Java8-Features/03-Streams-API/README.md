# Streams API (map, filter, reduce, collect)

A `Stream` is a **pipeline** for processing sequences of elements declaratively — describing *what* transformation you want, not *how* to loop and accumulate manually. Streams don't store data themselves; they operate on a source (a `Collection`, array, etc.) and are consumed exactly once.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Dave");

List<String> result = names.stream()
    .filter(name -> name.length() > 3)     // intermediate — keeps elements matching a predicate
    .map(String::toUpperCase)               // intermediate — transforms each element
    .sorted()                                // intermediate — sorts
    .collect(Collectors.toList());           // terminal — triggers execution, produces a result
```

## Intermediate vs terminal operations

- **Intermediate** (`filter`, `map`, `sorted`, `distinct`, `limit`, `skip`, ...) — return a new `Stream`, are **lazy** (nothing actually runs until a terminal operation is invoked), and can be chained.
- **Terminal** (`collect`, `forEach`, `reduce`, `count`, `anyMatch`, `findFirst`, ...) — triggers the pipeline to actually execute, produces a non-stream result (or a side effect), and **consumes** the stream — you cannot reuse a stream after a terminal operation (`IllegalStateException` if you try).

## `reduce`

Combines all elements into a single result.

```java
int sum = List.of(1, 2, 3, 4).stream()
    .reduce(0, Integer::sum);          // 0 is the identity/starting value

Optional<Integer> max = List.of(1, 2, 3).stream()
    .reduce(Integer::max);              // no identity — returns Optional (empty if the stream is empty)
```

## `collect`

The general-purpose terminal operation for accumulating stream elements into a result — usually via `Collectors` factory methods.

```java
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());
Map<String, Integer> map = stream.collect(Collectors.toMap(s -> s, String::length));
String joined = stream.collect(Collectors.joining(", "));
Map<Boolean, List<String>> partitioned = stream.collect(Collectors.partitioningBy(s -> s.length() > 3));
Map<Integer, List<String>> grouped = stream.collect(Collectors.groupingBy(String::length));
```

## Laziness matters

```java
Stream<Integer> s = List.of(1, 2, 3).stream()
    .peek(x -> System.out.println("filtering " + x))
    .filter(x -> x > 1);
// nothing printed yet — no terminal operation called
s.forEach(System.out::println);   // NOW the pipeline actually runs, element by element
```

- Each element flows through the *entire* pipeline (filter, then map, then...) before the next element starts — not "run filter on all elements, then map on all elements."

## Practice Questions / Exercises

- Given a `List<String>` of names, filter for those longer than 4 letters, uppercase them, sort alphabetically, and collect to a `List`.
- Use `reduce` to compute the sum and separately the max of a `List<Integer>`.
- Use `Collectors.groupingBy` to group a `List<String>` of words by their length.
- Use `Collectors.partitioningBy` to split a `List<Integer>` into evens and odds in one pass.
- Try calling a terminal operation twice on the same stream and observe `IllegalStateException`.

## Interview Questions

**Q: What's the difference between intermediate and terminal stream operations?**
A: Intermediate operations (`filter`, `map`, `sorted`, ...) return a new `Stream` and are lazily evaluated — they don't execute until a terminal operation is invoked. Terminal operations (`collect`, `forEach`, `reduce`, `count`, ...) trigger the entire pipeline to actually run, produce a final result or side effect, and consume the stream — it can't be reused afterward.

**Q: Why are streams lazy, and what's the practical benefit?**
A: Because intermediate operations just build up a description of the pipeline without executing anything, the JVM can optimize the whole chain once a terminal operation triggers it — e.g. short-circuiting operations like `findFirst()`/`anyMatch()` can stop processing as soon as a result is determined, without needing to fully materialize intermediate collections at each stage (unlike chaining separate loops that each fully process the whole list before the next starts).

**Q: What happens if you try to reuse a stream after a terminal operation has been called on it?**
A: It throws `IllegalStateException` ("stream has already been operated upon or closed") — a `Stream` represents a single-use pipeline over its source, not a reusable/replayable sequence like a `Collection`. If you need to run the pipeline again, you must create a fresh stream from the source.

**Q: What's the difference between `map()` and `flatMap()`?**
A: `map()` transforms each element into exactly one new element (a 1-to-1 transformation), potentially producing a `Stream<Stream<T>>` if the mapper itself returns a stream/collection. `flatMap()` transforms each element into a stream of zero-or-more elements and *flattens* all of them into a single combined stream — used when each input maps to multiple outputs, e.g. turning a `List<List<Integer>>` into a flat `Stream<Integer>`.

**Q: How does `reduce(identity, accumulator)` differ from `reduce(accumulator)` (no identity)?**
A: With an identity value, `reduce` always returns a plain result (`T`), using the identity as both the starting accumulator value and the fallback for an empty stream. Without an identity, `reduce` returns `Optional<T>`, since there's no sensible starting/fallback value to return if the stream turns out to be empty.

**Q: Are streams inherently parallel? What does `.parallelStream()` actually change?**
A: No — `.stream()` is sequential by default; you must explicitly call `.parallelStream()` (or `.parallel()` on an existing stream) to process elements across multiple threads using the common ForkJoinPool. Parallelism only pays off for CPU-intensive work on sufficiently large data sets — for small collections or I/O-bound operations, the coordination overhead often makes parallel streams *slower* than sequential ones, and operations must be stateless/non-interfering to be safe under parallel execution.
