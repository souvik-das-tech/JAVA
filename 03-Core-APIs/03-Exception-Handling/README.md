# Exception Handling

An exception is an object representing an abnormal event that disrupts normal program flow. Java's exception hierarchy:

```
Throwable
├── Error              (serious JVM-level problems — don't catch: OutOfMemoryError, StackOverflowError)
└── Exception
    ├── RuntimeException (unchecked — NullPointerException, ArrayIndexOutOfBoundsException, ArithmeticException...)
    └── (everything else)  (checked — IOException, SQLException...)
```

## `try` / `catch` / `finally`

```java
try {
    int x = 10 / 0;                 // throws ArithmeticException
} catch (ArithmeticException e) {
    System.out.println("Caught: " + e.getMessage());
} finally {
    System.out.println("Always runs — cleanup goes here");
}
```

- `finally` runs **whether or not** an exception was thrown, and even if the `try`/`catch` block returns — the only ways to skip it are `System.exit()` or the JVM crashing.
- Multiple `catch` blocks are checked top-to-bottom; the first assignable match handles it — so put more specific exception types **before** more general ones (`Exception` last), or the compiler flags unreachable catch blocks.
- Java 7+ supports multi-catch: `catch (IOException | SQLException e) { ... }`.

## Checked vs unchecked

| | Checked | Unchecked (RuntimeException) |
|---|---|---|
| Compiler enforcement | Must be caught or declared (`throws`) | No enforcement |
| Represents | Recoverable, expected conditions (e.g. file not found) | Programming errors (e.g. null deref, bad array index) |
| Examples | `IOException`, `SQLException` | `NullPointerException`, `IllegalArgumentException` |

- A method that doesn't handle a checked exception must declare it: `void readFile() throws IOException { ... }` — forcing callers to handle or re-declare it.
- Unchecked exceptions need no such declaration; they usually indicate a bug rather than an expected recoverable condition.

## Practice Questions / Exercises

- Write code that throws `ArithmeticException` (divide by zero) and `ArrayIndexOutOfBoundsException`, each caught by its own `catch` block, with a shared `finally` that always prints.
- Write a method that declares `throws IOException` and doesn't catch it — write a caller that either catches it or re-declares `throws`, and observe the compiler error if neither is done.
- Demonstrate `finally` running even when the `try` block has a `return` statement inside it.
- Use multi-catch (`catch (A | B e)`) to handle two unrelated exception types with one block.

## Interview Questions

**Q: What is the difference between checked and unchecked exceptions?**
A: Checked exceptions (subclasses of `Exception` excluding `RuntimeException`) are enforced by the compiler — a method must either catch them or declare `throws`, since they represent expected, recoverable conditions. Unchecked exceptions (`RuntimeException` and its subclasses) need no such declaration and typically represent programming bugs (null dereference, bad index, etc.) rather than conditions the caller is expected to plan for.

**Q: Does `finally` always execute? Are there any exceptions to that?**
A: It executes whether the `try` block completes normally, throws, or hits a `return`/`break`/`continue` — even a `return` inside `try` is deferred until after `finally` runs. The only ways it's skipped are the JVM terminating abruptly, e.g. `System.exit()` being called inside `try`, or a crash/power loss.

**Q: If both the `try` block and its `finally` block have a `return` statement, which value is actually returned?**
A: The `finally` block's `return` wins — it discards/overrides the value from `try`'s `return` (or even a pending exception). This is considered bad practice precisely because it silently swallows exceptions and confuses control flow.

**Q: What is the difference between `Error` and `Exception`? Should you ever catch an `Error`?**
A: Both extend `Throwable`. `Exception` represents conditions an application might reasonably want to catch and handle. `Error` represents serious problems at the JVM/environment level (`OutOfMemoryError`, `StackOverflowError`) that applications generally can't meaningfully recover from — catching them is usually pointless or actively harmful (masking a fatal condition), so they're conventionally left uncaught.

**Q: What happens if an exception is thrown inside a `catch` block?**
A: The new exception propagates up, replacing the original as the "in-flight" exception for that call stack (unless the original is preserved via exception chaining, e.g. `new RuntimeException("...", originalException)`), and any `finally` block for that `try` still runs before propagation continues outward.

**Q: Why is it generally bad practice to catch `Exception` (or `Throwable`) broadly instead of specific exception types?**
A: It swallows exceptions you didn't anticipate (including bugs and `Error`s if catching `Throwable`), masking real problems and making debugging harder, and it can accidentally catch and mishandle exceptions unrelated to the specific failure you meant to handle. Catching the most specific applicable exception type keeps error handling intentional and makes truly unexpected failures surface loudly.
