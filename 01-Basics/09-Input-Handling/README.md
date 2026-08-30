# Input Handling (`Scanner`, `BufferedReader`)

## `Scanner`

```java
import java.util.Scanner;

Scanner sc = new Scanner(System.in);
System.out.print("Enter your name: ");
String name = sc.nextLine();
System.out.print("Enter your age: ");
int age = sc.nextInt();
sc.close();
```

- Lives in `java.util`, wraps an `InputStream` (typically `System.in`) and parses tokens for you: `nextInt()`, `nextDouble()`, `next()` (single token), `nextLine()` (rest of the line).
- **Classic pitfall**: calling `nextInt()` then `nextLine()` — `nextInt()` doesn't consume the trailing newline, so the following `nextLine()` immediately returns an empty string. Fix: add an extra `sc.nextLine()` to consume the leftover newline, or use `nextLine()` + `Integer.parseInt()` consistently.
- Simpler API, but slower than `BufferedReader` for large input volumes because of the parsing/tokenizing overhead.

## `BufferedReader`

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String line = br.readLine();       // reads a whole line as String
int n = Integer.parseInt(br.readLine()); // manual parsing needed
br.close();
```

- Reads raw lines of text efficiently (buffers input internally) — no built-in type parsing, you convert manually (`Integer.parseInt`, `Double.parseDouble`, `String.split` for multiple values on one line).
- `readLine()` throws a checked `IOException`, so it must be inside a `try/catch` or the enclosing method must `throws IOException`.
- Preferred for **performance-sensitive** input (e.g. competitive programming, large datasets) since it avoids `Scanner`'s regex-based tokenizing.

## When to use which

| | `Scanner` | `BufferedReader` |
|---|---|---|
| Ease of use | Simpler, built-in parsing (`nextInt`, etc.) | Manual parsing required |
| Performance | Slower | Faster |
| Exceptions | Unchecked (`InputMismatchException`) | Checked (`IOException`) |
| Typical use | Small programs, quick input | Large/performance-critical input |

## Practice Questions / Exercises

- Write a program using `Scanner` that reads a name (`nextLine`) and an age (`nextInt`), and demonstrate the `nextInt()`-then-`nextLine()` pitfall, then fix it.
- Rewrite the same program using `BufferedReader`, parsing the age manually with `Integer.parseInt`.
- Write a program that reads a single line of space-separated numbers and sums them, once using `Scanner` (`nextInt()` in a loop) and once using `BufferedReader` + `String.split(" ")`.
- Write a program that keeps reading lines with `Scanner.hasNextLine()`/`nextLine()` until a sentinel value (e.g. `"exit"`) is entered.
- Handle invalid input gracefully: catch `InputMismatchException` from `Scanner.nextInt()` when the user types text instead of a number.

## Interview Questions

**Q: What's the main practical difference between `Scanner` and `BufferedReader`?**
A: `Scanner` parses input into typed tokens for you (`nextInt`, `nextDouble`, etc.) using regex internally, which is convenient but slower. `BufferedReader` only reads raw lines of text efficiently and requires manual parsing (e.g. `Integer.parseInt`), making it faster and the usual choice for performance-sensitive or large-volume input.

**Q: What is the classic `Scanner` bug involving `nextInt()` followed by `nextLine()`?**
A: `nextInt()` reads only the numeric token and leaves the trailing newline character in the input buffer unconsumed. The very next `nextLine()` call then immediately reads that leftover newline as an empty string instead of waiting for new input. The fix is to insert an extra `sc.nextLine()` right after `nextInt()` to consume the leftover newline, or to consistently read everything with `nextLine()` and parse manually.

**Q: Why does `BufferedReader.readLine()` require exception handling but `Scanner.nextLine()` doesn't?**
A: `readLine()` declares a checked `IOException` (it can fail due to actual I/O problems), so the compiler forces you to catch it or propagate it. `Scanner`'s methods throw unchecked exceptions (like `InputMismatchException`, `NoSuchElementException`), so handling them is optional at compile time.

**Q: How would you read multiple whitespace-separated numbers from a single line using `BufferedReader`?**
A: Read the whole line with `readLine()`, split it on whitespace with `line.split("\\s+")` (or `" "`), then parse each resulting token with `Integer.parseInt()`/`Double.parseDouble()` in a loop.

**Q: Is `Scanner` thread-safe?**
A: No, `Scanner` is not synchronized/thread-safe — concurrent access from multiple threads requires external synchronization.

**Q: When reading from a file instead of console input, does the choice between `Scanner` and `BufferedReader` change?**
A: The same trade-off applies — `BufferedReader` (often wrapping a `FileReader`) is generally preferred for large files due to performance, while `Scanner` can still be convenient for smaller files needing typed parsing (`Scanner` can also wrap a `File` directly via its `Scanner(File)` constructor).
