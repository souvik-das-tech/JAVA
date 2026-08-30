# String, StringBuilder, StringBuffer

## `String` immutability & the string pool

A `String` is **immutable** — once created, its contents can never change. Every "modifying" operation (`concat`, `substring`, `replace`, ...) returns a **new** `String` object instead.

```java
String s = "hello";
s.concat(" world");   // returns a new String — s is unchanged!
s = s.concat(" world"); // must reassign to keep the result
```

- String literals (`"hello"`) are interned in the **string pool** — a special area of the heap. Two literals with the same content refer to the **same** pooled object: `"abc" == "abc"` is `true`.
- `new String("abc")` explicitly creates a **new** object outside the pool: `new String("abc") == "abc"` is `false`, even though `.equals()` is `true`.
- `String.intern()` manually adds/looks up a string in the pool.
- Immutability is why `String` is safe as a `HashMap` key and safe to share across threads without synchronization.

## `StringBuilder` / `StringBuffer`

Mutable alternatives for building strings efficiently, avoiding the "new object per concatenation" cost of repeated `String` concatenation in a loop.

```java
StringBuilder sb = new StringBuilder();
sb.append("hello").append(" ").append("world");   // mutates in place, chainable
String result = sb.toString();
```

| | Mutable | Thread-safe | Speed |
|---|---|---|---|
| `String` | ❌ | ✅ (immutable ⇒ inherently safe) | Slow for repeated concatenation |
| `StringBuilder` | ✅ | ❌ | Fast |
| `StringBuffer` | ✅ | ✅ (synchronized methods) | Slower than `StringBuilder` due to synchronization overhead |

- Use `StringBuilder` by default (single-threaded context — almost always the case); use `StringBuffer` only if the same builder instance is genuinely shared and mutated across multiple threads.

## Practice Questions / Exercises

- Concatenate strings in a loop 10,000 times using `+=` on a `String`, then do the same using `StringBuilder.append`, and compare (conceptually or by timing) why one is much slower.
- Show that two string literals with the same content are `==` equal, but a `new String(...)` with the same content is not, while `.equals()` is true for both.
- Use `StringBuilder` methods `insert`, `delete`, `reverse`, and `replace` on a sample string.
- Write a method that checks if a string is a palindrome using `StringBuilder.reverse()`.

## Interview Questions

**Q: Why is `String` immutable in Java?**
A: For security (Strings are used for class names, file paths, network connections — mutability would be exploitable), thread-safety (immutable objects can be freely shared across threads with no synchronization), safe use as hash keys (an immutable object's hash code can be cached and never goes stale), and to make string-pool sharing/interning safe (pooled literals must never change under any reference holding them).

**Q: What is the string constant pool, and how does `new String("abc")` interact with it?**
A: A special region where string literals are stored so identical literals share one object. `new String("abc")` explicitly bypasses the pool, creating a distinct heap object even if `"abc"` is already pooled — so `new String("abc") == "abc"` is `false`, though `.equals()` is `true` since content matches.

**Q: What is the difference between `StringBuilder` and `StringBuffer`?**
A: They're functionally identical mutable string-building APIs; the only difference is `StringBuffer`'s methods are `synchronized` (thread-safe at the cost of overhead), while `StringBuilder`'s are not. Use `StringBuilder` unless the same instance is genuinely shared across threads.

**Q: Why is using `+` to concatenate strings in a loop considered bad practice?**
A: Each `+` on `String`s creates a brand-new `String` object (since `String` is immutable), so concatenating in a loop of N iterations creates roughly N intermediate objects — O(n²) total character copying in the worst case. `StringBuilder.append()` mutates an internal buffer in place, giving O(n) total work.

**Q: Does the compiler do anything special for a single `String` expression built entirely from `+` outside a loop, e.g. `String s = "a" + "b" + "c";`?**
A: Yes — for compile-time-constant concatenations the compiler folds them into a single literal at compile time (equivalent to `"abc"`, potentially pooled). For concatenations that aren't all constants (e.g. involving a variable), the compiler typically translates the `+` chain into a single `StringBuilder` internally rather than one `String` per `+`, so a single non-loop concatenation expression isn't as costly as looped `+=`.

**Q: What does `.intern()` do?**
A: It returns the canonical pooled representation of a string's content — if an equal string already exists in the pool, it returns that reference; otherwise it adds this string's content to the pool and returns a reference to it. Useful for deliberately forcing reference equality between equal-content strings, though excessive use for very many unique strings can pressure the pool.
