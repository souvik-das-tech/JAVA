# `try-with-resources`

Automatically closes resources (files, streams, DB connections) at the end of the `try` block, without needing a manual `finally { resource.close(); }`. Any resource used must implement `AutoCloseable` (or its subinterface `Closeable`).

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    String line = br.readLine();
    System.out.println(line);
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
}
// br.close() is called automatically here — even if an exception was thrown
```

- Multiple resources can be declared, separated by `;` — they're closed in **reverse** order of declaration.
- Resources declared in the `try(...)` parentheses are implicitly `final` (or effectively final) and scoped only to the `try` block.

## Before Java 7 (manual cleanup, error-prone)

```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("data.txt"));
    // ...
} finally {
    if (br != null) {
        try { br.close(); } catch (IOException e) { /* swallow or log */ }
    }
}
```

Verbose and easy to get wrong (nested try/finally per resource, forgetting a null check). `try-with-resources` eliminates all of this boilerplate.

## Suppressed exceptions

If **both** the `try` block body and the automatic `close()` throw, the exception from the body is the one propagated; the `close()`'s exception is attached to it as a **suppressed exception**, retrievable via `getSuppressed()` — nothing is silently lost.

## Practice Questions / Exercises

- Write a class implementing `AutoCloseable` with a `close()` method that prints "Closed", and use it in a `try-with-resources` block — show `close()` runs automatically even if the block completes normally.
- Trigger an exception inside the `try` block of the above and confirm `close()` still runs before the exception propagates (print statements make the order visible).
- Declare two resources in one `try(...)` and print inside each `close()` to show they close in reverse declaration order.
- Read a text file's contents using `try-with-resources` with a `BufferedReader`.

## Interview Questions

**Q: What interface must a resource implement to be usable in `try-with-resources`?**
A: `AutoCloseable` (which declares `close() throws Exception`), or its more specific subinterface `Closeable` (used by I/O classes, `close() throws IOException`). Any class implementing either can be declared inside the `try(...)` parentheses.

**Q: In what order are multiple resources closed when several are declared in one `try(...)`?**
A: In reverse order of their declaration — the last resource declared is closed first, mirroring how you'd want nested/dependent resources unwound (e.g. closing a `BufferedReader` before the underlying `FileReader` it wraps, if declared in that order).

**Q: If both the `try` block and the automatic `close()` call throw exceptions, which one is actually propagated to the caller?**
A: The exception from the `try` block body is the one that propagates; the exception thrown by `close()` is attached to it as a "suppressed exception" (accessible via `getSuppressed()` on the primary exception) rather than replacing or being silently dropped.

**Q: How does `try-with-resources` improve on manually closing resources in a `finally` block?**
A: It eliminates the boilerplate/error-prone pattern of null-checking the resource and wrapping `close()` in its own nested `try/catch` inside `finally`, guarantees `close()` is called (even on exception, even with multiple resources, in the correct reverse order), and preserves exceptions properly via suppression instead of one silently masking another.

**Q: Can you use `try-with-resources` without a `catch` or `finally` block at all?**
A: Yes — `try (Resource r = ...) { ... }` alone is valid; the `catch`/`finally` are optional exactly as with a regular `try`. The resource is still automatically closed at the end of the block even with no `catch`/`finally` present.

**Q: Since Java 9, can you use a resource variable declared outside the `try(...)` parentheses?**
A: Yes — Java 9 added support for using an existing effectively-final variable directly in `try (resourceVar) { ... }` without redeclaring it, as long as the variable is already `final` or effectively final and refers to an `AutoCloseable`.
