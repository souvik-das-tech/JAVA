# Generic Classes & Methods

Generics let a class or method operate on a **type parameter** decided at the point of use, giving compile-time type safety without casting.

## Generic classes

```java
class Box<T> {
    private T content;
    void set(T content) { this.content = content; }
    T get() { return content; }
}

Box<String> box = new Box<>();
box.set("hello");
String s = box.get();      // no cast needed — compiler already knows it's a String
```

- `T` is a placeholder ("type parameter") — replaced with an actual type at each usage site (`Box<String>`, `Box<Integer>`, ...).
- Before generics (pre-Java 5), you'd use `Object` and manually cast on every `get()` — losing compile-time type checking and risking a runtime `ClassCastException`.
- A class can have multiple type parameters: `class Pair<K, V> { K key; V value; }`.

## Generic methods

A method can introduce its **own** type parameter, independent of (or in addition to) the class's — declared before the return type.

```java
class Utils {
    static <T> T firstElement(List<T> list) {
        return list.get(0);
    }
    static <T> void printAll(T[] array) {
        for (T item : array) System.out.println(item);
    }
}

Integer first = Utils.<Integer>firstElement(List.of(1, 2, 3));   // explicit type witness (usually omitted)
Integer first2 = Utils.firstElement(List.of(1, 2, 3));           // usually just inferred
```

- The `<T>` before the return type declares a fresh type parameter scoped to that method call — it doesn't need the enclosing class to be generic at all (works fine in a plain non-generic class, even a `static` method).
- Type inference means you almost never need to explicitly write `Utils.<Integer>firstElement(...)` — the compiler figures `T` out from the argument.

## Type erasure

Generics are a **compile-time-only** feature — the compiler erases type parameters after checking them, replacing `T` with `Object` (or its bound) in the compiled bytecode, and inserting casts where needed. This is why you can't do `new T()`, `T.class`, or check `instanceof T` at runtime — that type information simply doesn't exist anymore once compiled.

## Practice Questions / Exercises

- Write a generic `Box<T>` class with `set`/`get`, and create instances holding a `String` and an `Integer`.
- Write a generic `Pair<K, V>` class holding a key and a value, with a `toString()` printing both.
- Write a generic static method `<T> T firstElement(List<T> list)` and call it with lists of different types.
- Write a generic method `<T> boolean isEqual(T a, T b)` using `.equals()`, and call it with both reference types and (autoboxed) primitives.

## Interview Questions

**Q: What is the main benefit of generics over using `Object` and manual casting?**
A: Compile-time type safety — the compiler enforces that only the declared type can go in/out of the generic structure, catching type mismatches at compile time rather than risking a `ClassCastException` at runtime, and eliminating the need for explicit casts at every access point.

**Q: What is type erasure, and what practical restrictions does it impose?**
A: The compiler checks generic type usage at compile time, then "erases" the type parameters — replacing them with `Object` (or their bound) in the compiled `.class` file, inserting casts as needed. Because the actual type argument doesn't exist at runtime, you can't create a new instance of a type parameter (`new T()`), can't do `T.class`, and can't check `instanceof T` directly — only `instanceof Box<?>` (the raw/wildcard form) is legal.

**Q: Can a `static` method use the class's own type parameter directly?**
A: No — `static` members belong to the class itself, not to any particular parameterized instance (`Box<String>` vs `Box<Integer>` are the same class at the `static` level), so a `static` method can't reference the class's instance-level type parameter `T`. If it needs generics, it must declare its own, independent type parameter (e.g. `static <U> U someMethod(U arg)`).

**Q: What is the difference between a raw type (`Box` without `<T>`) and a parameterized type (`Box<Object>`)?**
A: A raw type disables all generic type checking for that usage (legacy pre-Java-5 compatibility, produces compiler warnings) — the compiler treats members as returning `Object` with no compile-time verification. `Box<Object>` is a proper parameterized type where `T` is explicitly `Object`, and generic type checking still fully applies (e.g. you couldn't assign a `Box<String>` to a `Box<Object>` variable, whereas a raw `Box` can hold anything with no such restriction).

**Q: Why can't you create an array of a generic type directly, e.g. `new T[10]`?**
A: Because of type erasure, the JVM would need to know `T`'s actual runtime type to correctly create and type-check the array at the bytecode level (arrays, unlike generics, are reified — they know and enforce their element type at runtime) — but `T` is erased, so the compiler disallows this to prevent an unsound, undetectable `ArrayStoreException`-like situation. The common workaround is creating an `Object[]` internally and casting, or using `java.lang.reflect.Array.newInstance()`.

**Q: How does the compiler decide the actual type of `T` when you call a generic method without an explicit type witness?**
A: Through type inference — it examines the types of the arguments you actually pass (and sometimes the target/assignment context) and deduces the most specific type parameter that satisfies all usages consistently, without you needing to write `Utils.<Integer>firstElement(...)` explicitly in almost all cases.
