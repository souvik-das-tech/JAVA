# Bounded Type Parameters

A bound restricts what types can be substituted for a type parameter — `<T extends SomeType>` means `T` must be `SomeType` or a subtype of it (works for classes *and* interfaces — `extends` is used for both in a bound, never `implements`).

```java
class NumberBox<T extends Number> {   // T must be Number or a subclass (Integer, Double, ...)
    T value;
    double doubled() {
        return value.doubleValue() * 2;   // legal — the compiler knows T has Number's methods
    }
}

NumberBox<Integer> nb = new NumberBox<>();   // OK
NumberBox<String> sb = new NumberBox<>();    // compile error — String is not a Number
```

- Without a bound, `T` is treated as `Object` — you can't call any method on it besides what `Object` provides (`equals`, `toString`, ...).
- A bound gives the compiler (and you) access to the bound type's methods on values of type `T`, while still being generic over any qualifying subtype.

## Multiple bounds

```java
<T extends Comparable<T> & Cloneable> void process(T item) { ... }
```

- Only **one** class bound is allowed (Java has single inheritance), but any number of **interface** bounds — the class bound (if any) must come first.

## A common pattern: bounded generic methods

```java
static <T extends Comparable<T>> T max(List<T> list) {
    T max = list.get(0);
    for (T item : list) {
        if (item.compareTo(max) > 0) max = item;
    }
    return max;
}
```

- This is essentially how `Collections.max(List<T>)` is actually declared in the JDK.

## Practice Questions / Exercises

- Write a `NumberBox<T extends Number>` class with a `doubled()` method using `T`'s `doubleValue()` — show it rejects `NumberBox<String>` at compile time.
- Write a generic `<T extends Comparable<T>> T max(List<T> list)` method and test it with `List<Integer>` and `List<String>`.
- Write a method bounded by two interfaces, e.g. `<T extends Comparable<T> & Cloneable>`, and explain why the class bound (if present) must come first in the list.
- Try writing `<T extends Number>` and calling `.doubleValue()` vs. writing plain `<T>` and trying the same — observe the compiler error in the unbounded case.

## Interview Questions

**Q: What does `<T extends Number>` mean, and why "extends" even when the bound could be an interface?**
A: It restricts `T` to `Number` or any subtype of it, so only types compatible with `Number` (like `Integer`, `Double`) can be used as the type argument. `extends` is the keyword used for *all* generic bounds regardless of whether the bound is a class or an interface — Java doesn't use `implements` in this context, even for interface bounds.

**Q: Why would you bound a type parameter at all, instead of leaving it as plain `<T>`?**
A: An unbounded `T` is treated as `Object` inside the generic code, so you can only call `Object`'s methods on values of type `T`. Bounding it (e.g. `<T extends Number>`) tells the compiler `T` is guaranteed to have `Number`'s methods too, letting you call things like `.doubleValue()` directly on a `T` value while still being generic over any qualifying subtype.

**Q: Can a type parameter have more than one bound? What's the restriction?**
A: Yes — using `&` to combine bounds, e.g. `<T extends Comparable<T> & Cloneable>`. You can have at most **one** class bound (since Java only supports single class inheritance) plus any number of interface bounds, and if a class bound is present, it must be listed first.

**Q: How is `<T extends Comparable<T>>` used in practice, and why is it a common pattern?**
A: It's used whenever a generic method/class needs to compare instances of `T` to each other (e.g. finding a max/min, or sorting) — bounding by `Comparable<T>` guarantees `T` has a `compareTo(T)` method available, which is exactly how the JDK itself declares methods like `Collections.max(Collection<? extends T> coll)`.

**Q: Does a bounded type parameter like `<T extends Number>` affect anything at runtime, given type erasure?**
A: The bound mainly affects compile-time checking (the compiler enforces `T` must be a `Number` subtype at every usage site) and determines what `T` erases to — instead of erasing to `Object`, a bounded type parameter erases to its bound (`Number` here), which is what lets the compiler insert the correct implicit casts and allow calling the bound's methods.

**Q: What's the difference between `<T extends Number>` on a class/method's type parameter, and a wildcard bound like `List<? extends Number>`?**
A: `<T extends Number>` names an actual, specific (though still generic) type that's used consistently throughout the method/class — you can still, for example, return a `T` or assign new `T` values. `List<? extends Number>` is a wildcard describing *a* list of some unknown-but-Number-compatible type, used for a specific variable/parameter — since the exact type is unknown, you generally can't add elements to it (see wildcards).
