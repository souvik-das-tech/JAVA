# `Optional`

`Optional<T>` is a container that either **holds a value** or is **empty** — a type-safe, explicit alternative to returning (and forgetting to check for) `null`.

```java
Optional<String> present = Optional.of("hello");     // must be non-null, or throws NPE immediately
Optional<String> empty = Optional.empty();
Optional<String> maybeNull = Optional.ofNullable(getValueThatMightBeNull());
```

## Why it exists

Returning `null` for "no value" relies entirely on the caller *remembering* to check — nothing in the method signature signals that possibility, and a forgotten check becomes a `NullPointerException` far from its actual cause. `Optional<T>` as a **return type** makes "this might not have a value" part of the visible contract, and its API nudges you toward handling the empty case explicitly.

## Consuming an `Optional`

```java
Optional<String> opt = Optional.of("hello");

opt.isPresent();                        // true — check without extracting (older style)
opt.isEmpty();                          // false (Java 11+)

opt.get();                              // "hello" — throws NoSuchElementException if empty; avoid calling blindly

opt.orElse("default");                  // returns the value, or "default" if empty
opt.orElseGet(() -> computeDefault());  // like orElse, but the default is computed LAZILY (only if actually needed)
opt.orElseThrow();                       // throws NoSuchElementException if empty (Java 10+)
opt.orElseThrow(() -> new IllegalStateException("missing"));  // custom exception if empty

opt.ifPresent(value -> System.out.println(value));                    // run only if present
opt.ifPresentOrElse(value -> use(value), () -> System.out.println("empty"));  // Java 9+ — both branches

opt.map(String::toUpperCase);           // transforms the value if present, stays empty otherwise
opt.filter(s -> s.length() > 3);        // keeps the value only if it matches, else becomes empty
```

## What `Optional` is *not* for

- **Not for fields** — an `Optional` field adds serialization/memory overhead for no real benefit; just use `null` (or better, avoid nullable fields) for class fields.
- **Not for method parameters** — forces every caller to wrap arguments in `Optional.of(...)`; use method overloading instead.
- **Not for collections** — an empty `List`/`Map` is already a perfectly good "no results" signal; wrapping a collection in `Optional` is redundant (`Optional<List<T>>` — just return an empty `List<T>` directly).
- It's intended specifically as a **return type** for methods where "no result" is a legitimate, expected outcome (e.g. `findById` in a repository).

## Practice Questions / Exercises

- Write a method `Optional<String> findUser(int id)` that returns `Optional.empty()` for unknown IDs, and use `orElse`, `orElseGet`, and `orElseThrow` at three different call sites.
- Chain `.map()` and `.filter()` on an `Optional<String>` to transform and conditionally keep a value.
- Use `ifPresentOrElse` to print either the value or a fallback message.
- Call `.get()` on an empty `Optional` and catch the `NoSuchElementException`, to see why blindly calling `.get()` is discouraged.

## Interview Questions

**Q: What problem does `Optional` solve that plain `null` doesn't?**
A: With `null`, nothing in a method's signature signals that "no value" is a possible outcome — callers must remember to check, and a forgotten check surfaces as a `NullPointerException` far from where the actual missing-value decision was made. `Optional<T>` as a return type makes that possibility part of the visible API contract, and its methods (`map`, `orElse`, `ifPresent`, ...) encourage handling the empty case explicitly rather than accidentally skipping it.

**Q: Why is calling `.get()` on an `Optional` without checking `isPresent()` first considered bad practice?**
A: It defeats the entire purpose of using `Optional` — you're back to a runtime exception (`NoSuchElementException` instead of `NullPointerException`) if the value is absent, with no compile-time or API-level nudge to handle the empty case. Prefer `orElse`, `orElseGet`, `orElseThrow` (with a meaningful exception), or `ifPresent`/`map`, which build the empty-handling into the call itself.

**Q: What's the difference between `orElse()` and `orElseGet()`?**
A: `orElse(value)` always evaluates its argument eagerly, even if the `Optional` is present (the value is simply computed and discarded in that case). `orElseGet(supplier)` only invokes the supplier lazily, if the `Optional` is actually empty. This matters if computing the default is expensive or has side effects — `orElseGet` avoids unnecessary work when a value is already present.

**Q: Why is it discouraged to use `Optional` as a method parameter type or a class field?**
A: As a parameter, it forces every caller to explicitly wrap arguments in `Optional.of(...)`/`Optional.empty()` even for a simple optional argument, when method overloading (or just accepting `null`, or a nullable annotation) is simpler. As a field, `Optional` isn't `Serializable` and adds an unnecessary wrapper-object allocation for something that could just be a nullable field — `Optional`'s design intent (per its own Javadoc) is specifically as a return type communicating "this call might not produce a value."

**Q: What does `Optional.map()` do if the `Optional` is empty?**
A: It short-circuits — the mapping function is never invoked, and `map()` simply returns an empty `Optional` of the target type. This mirrors how `Stream` operations behave on an empty stream: transformations chain safely without needing explicit null/empty checks at every step.

**Q: Is `Optional` intended to replace `null` everywhere in Java code?**
A: No — it's specifically designed for return types where "no result" is expected and meaningful (e.g. a repository lookup that might not find anything). It's not meant to eliminate `null` universally from Java (which still allows `null` everywhere `Optional` isn't explicitly used), and overusing it — for fields, parameters, or collections — adds needless wrapping without real benefit.
