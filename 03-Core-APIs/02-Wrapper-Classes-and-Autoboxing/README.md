# Wrapper Classes & Autoboxing/Unboxing

Every primitive type has a corresponding wrapper class: `int`→`Integer`, `double`→`Double`, `boolean`→`Boolean`, `char`→`Character`, etc. Wrappers let primitives be used where an `Object` is required (generics, collections, `null`ability).

## Autoboxing / unboxing

Automatic conversion the compiler inserts between a primitive and its wrapper.

```java
Integer boxed = 5;         // autoboxing: int -> Integer, really Integer.valueOf(5)
int unboxed = boxed;       // auto-unboxing: Integer -> int, really boxed.intValue()

List<Integer> list = new ArrayList<>();
list.add(10);               // autoboxed
int x = list.get(0);        // auto-unboxed
```

- Collections (`List<Integer>`, not `List<int>`) can't hold primitives directly — generics work only with reference types, so autoboxing is what makes `list.add(10)` compile.

## The `Integer` cache trap

```java
Integer a = 100, b = 100;
System.out.println(a == b);   // true — both come from the cached pool (-128 to 127)

Integer c = 200, d = 200;
System.out.println(c == d);   // false — outside the cache range, two distinct objects
```

- `Integer.valueOf(int)` (which autoboxing calls) caches instances for values **-128 to 127**. Values in that range share objects; outside it, each autoboxing creates a new `Integer` object.
- This is a classic interview/bug trap: always use `.equals()`, never `==`, to compare wrapper objects by value.

## NullPointerException trap

```java
Integer count = null;
int x = count;   // NullPointerException at auto-unboxing — count.intValue() is called on null
```

- Auto-unboxing a `null` wrapper throws `NullPointerException` — a common bug when a `Map<String,Integer>` lookup misses and returns `null`, which then gets silently auto-unboxed.

## Practice Questions / Exercises

- Demonstrate the `Integer` cache: compare `Integer` objects with `==` for values inside and outside `-128..127`, and explain the difference in output.
- Write code that triggers a `NullPointerException` via auto-unboxing a `null` `Integer`, then fix it with a null check.
- Store primitives in a `List<Integer>` via autoboxing, then sum them by unboxing each element in a loop.
- Use wrapper class utility methods: `Integer.parseInt`, `Integer.toBinaryString`, `Integer.MAX_VALUE`/`MIN_VALUE`, `Character.isDigit`.

## Interview Questions

**Q: What is autoboxing and unboxing?**
A: Autoboxing is the compiler's automatic conversion of a primitive value into its wrapper object (e.g. `int` → `Integer`, via `Integer.valueOf`) when a reference type is expected. Auto-unboxing is the reverse — automatically extracting the primitive value from a wrapper object (e.g. via `.intValue()`) when a primitive is expected.

**Q: Why does `Integer a = 100, b = 100;` give `a == b` as true, but `Integer a = 200, b = 200;` gives false?**
A: `Integer.valueOf(int)`, which autoboxing calls, caches and reuses `Integer` instances for values from -128 to 127 (the range most likely to be reused, per the JLS-mandated cache). 100 falls in that range so both variables get the same cached object; 200 is outside it, so each autoboxing call creates a distinct new `Integer` object.

**Q: Why can auto-unboxing throw a `NullPointerException`?**
A: Because unboxing a wrapper calls a method on it (e.g. `.intValue()`) to extract the primitive value — if the wrapper reference is `null`, that method call throws `NullPointerException`. This commonly bites when a `Map<K,Integer>` lookup misses (returning `null`) and the result is used directly in primitive arithmetic or assigned to an `int`.

**Q: Why can't you use a primitive type directly as a generic type parameter, e.g. `List<int>`?**
A: Java generics are implemented via type erasure and only work with reference types — generic type parameters are erased to `Object` (or a bound) internally, and primitives aren't objects. This is exactly why wrapper classes exist: `List<Integer>` works because `Integer` is a reference type, with autoboxing bridging the gap when you `add(5)`.

**Q: What's the performance implication of autoboxing inside a tight loop, e.g. summing into an `Integer` accumulator instead of `int`?**
A: Each arithmetic operation on the boxed accumulator unboxes it, computes, then reboxes the result into a brand-new `Integer` object (outside the -128..127 cache range) — meaning a loop can allocate one object per iteration, causing unnecessary garbage collection pressure compared to using a primitive `int` accumulator directly.

**Q: Should you compare two `Integer` objects with `==` or `.equals()`, and why?**
A: Always `.equals()` for value comparison — `==` compares object references, and due to the Integer cache, `==` may accidentally appear correct for small values (-128..127) while silently failing for larger ones, which makes it an easy, hard-to-catch bug if relied upon.
