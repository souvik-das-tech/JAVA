# Wildcards (`?`, `? extends`, `? super`)

A wildcard (`?`) represents an **unknown** type, used at a usage site (a variable/parameter type), not as a type parameter declaration. It's the tool for writing methods that accept a generic type "family" without caring about the exact type argument.

## The core problem wildcards solve

Generics are **invariant** — `List<Integer>` is *not* a subtype of `List<Number>`, even though `Integer` is a subtype of `Number`. So a method declared `void printAll(List<Number> list)` can't accept a `List<Integer>` at all, even though that seems like it should be safe to read from.

```java
List<Integer> ints = List.of(1, 2, 3);
printNumbers(ints);   // compile error if the parameter is List<Number> — but works with List<? extends Number>
```

## `? extends T` — upper bounded wildcard ("producer")

```java
void printNumbers(List<? extends Number> list) {   // accepts List<Integer>, List<Double>, List<Number>, ...
    for (Number n : list) {
        System.out.println(n);
    }
}
```

- You can **read** elements out (as `Number`, since every element is guaranteed to be *some* subtype of `Number`).
- You **cannot add** elements (other than `null`) — the compiler doesn't know the list's *exact* type (could be `List<Integer>` or `List<Double>`), so it can't verify any specific object you'd add is safe for it.

## `? super T` — lower bounded wildcard ("consumer")

```java
void addIntegers(List<? super Integer> list) {   // accepts List<Integer>, List<Number>, List<Object>, ...
    list.add(1);
    list.add(2);
}
```

- You **can add** `T` (or its subtypes) — safe, since the list is guaranteed to accept `T` or anything above it.
- Reading gives you only `Object` back (the compiler only knows the list holds *something* that is `T` or a supertype — no more specific guarantee than `Object`).

## PECS — "Producer Extends, Consumer Super"

A mnemonic for choosing: if the structure only **produces** (you read `T` out of it), use `? extends T`. If it only **consumes** (you put `T` into it), use `? super T`. If both, don't use a wildcard — use the exact type `T`.

## Unbounded wildcard `?`

```java
void printSize(List<?> list) {   // don't care what's in it — just checking size
    System.out.println(list.size());
}
```

Used when the method genuinely doesn't care about the element type at all — only calls methods that don't depend on it (`size()`, `isEmpty()`, `clear()`).

## Practice Questions / Exercises

- Write a method `double sumAll(List<? extends Number> list)` that sums a list of any `Number` subtype, and call it with `List<Integer>` and `List<Double>`.
- Write a method `void addNumbers(List<? super Integer> list)` that adds a few `Integer`s to it, and call it with `List<Integer>`, `List<Number>`, and `List<Object>`.
- Try adding an element to a `List<? extends Number>` parameter and observe the compiler error.
- Write `void printAll(List<?> list)` and call it with lists of several different element types to show it accepts anything.

## Interview Questions

**Q: Why is `List<Integer>` not considered a subtype of `List<Number>` in Java, even though `Integer` is a subtype of `Number`?**
A: Generics are invariant by design — if `List<Integer>` were treated as a `List<Number>`, you could then legally call `list.add(3.14)` (a `Double`, which is a valid `Number`) on what is actually an `Integer`-only list at runtime, silently corrupting it. Java disallows this by making `List<Integer>` and `List<Number>` unrelated types; wildcards (`? extends`/`? super`) exist specifically to allow safe, more flexible usage without this hole.

**Q: What is the PECS mnemonic, and how do you apply it?**
A: "Producer Extends, Consumer Super" — if a generic parameter only **produces** values you read out of it, bound it with `? extends T`; if it only **consumes** values you put into it, bound it with `? super T`. If the same parameter needs to do both, use the plain type `T` with no wildcard, since neither wildcard alone supports both safely.

**Q: Why can't you `add()` to a `List<? extends Number>` (aside from `null`)?**
A: The compiler only knows the list holds *some* specific-but-unknown subtype of `Number` — it could be a `List<Integer>`, `List<Double>`, etc. Since it can't verify at compile time which one, it can't guarantee any given object you try to add actually matches that unknown specific type, so it disallows all additions except `null` (which is trivially assignable to any reference type).

**Q: Why can you only read a `List<? super Integer>`'s elements as `Object`?**
A: The compiler only knows the list's element type is `Integer` or some **supertype** of it (`Number`, `Object`, ...) — it can't know exactly which, so the only type it can safely guarantee for anything read out is the common supertype of all possibilities: `Object`.

**Q: What's the difference between an unbounded wildcard `List<?>` and a raw type `List`?**
A: `List<?>` is still fully type-checked — you just don't know (or care about) the specific element type, and the compiler enforces the "no unsafe add" restriction just like any wildcard. A raw `List` disables generic type checking entirely for that usage (a legacy escape hatch), letting you call `add(Object)` with no compile-time safety at all — `List<?>` is strictly safer.

**Q: Can you have a wildcard as a type parameter declaration, e.g. `class Box<?> { }`?**
A: No — wildcards are only valid at usage sites (a variable type, parameter type, return type), never in a type parameter *declaration*. A class/method declares an actual named type parameter (`class Box<T>`, `<T> void method(...)`); wildcards are how *callers* of a parameterized type describe an unknown type argument when using it.
