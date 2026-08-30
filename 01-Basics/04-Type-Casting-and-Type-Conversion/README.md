# Type Casting & Type Conversion

## Implicit conversion (widening)

Java automatically converts a smaller type to a larger, compatible type — no data loss, no cast needed:

```java
int i = 100;
long l = i;      // int -> long, automatic
double d = l;     // long -> double, automatic
```

Widening order (each can auto-convert to anything to its right):

```
byte -> short -> int -> long -> float -> double
              char -> int
```

- `char` widens directly to `int` (and beyond), but nothing auto-widens *into* `char`.

## Explicit conversion (narrowing) — casting

Going from a larger type to a smaller one risks losing data, so Java requires an explicit cast:

```java
double d = 3.99;
int i = (int) d;      // 3 - truncates the decimal part, does NOT round
long l = 130L;
byte b = (byte) l;    // overflow if out of byte range (-128 to 127)
```

- Casting a `double`/`float` to an integer type **truncates** (chops the decimal), it does not round.
- Casting a value outside the target type's range wraps around (overflow), it doesn't clamp or throw.

```java
int big = 130;
byte b = (byte) big;  // -126, due to overflow wraparound
```

## String <-> primitive conversion

```java
String s = String.valueOf(42);     // int -> String
String s2 = "" + 42;               // int -> String, via concatenation
int i = Integer.parseInt("42");    // String -> int
double d = Double.parseDouble("3.14"); // String -> double
```

- `parseXxx` throws `NumberFormatException` if the string isn't a valid number.

## Autoboxing / unboxing (primitive <-> wrapper)

```java
Integer obj = 5;        // autoboxing: int -> Integer
int prim = obj;         // unboxing: Integer -> int
```

- Covered in more depth later (Core APIs — Wrapper classes), but it's a form of type conversion too: the compiler inserts the `Integer.valueOf()` / `.intValue()` calls for you.

## Practice Questions / Exercises

- Write a program that widens a `byte` all the way to a `double`, printing the value after each step.
- Write a program that narrows a `double` like `9.999` to an `int` and observe the truncation (not rounding).
- Deliberately overflow a `byte` by casting a large `int` (e.g. `300`) into it, print the result, and explain the wraparound.
- Convert a `String` to an `int` using `Integer.parseInt`, then try parsing an invalid string like `"12a"` and observe the exception.
- Write a method that takes a `double` and an `int` parameter, add them together without an explicit cast, and identify what implicit conversion happens to make that work.

## Interview Questions

**Q: What is the difference between implicit and explicit type conversion in Java?**
A: Implicit (widening) conversion happens automatically when converting a smaller type to a larger, compatible one (e.g. `int` to `long`) — no data loss occurs. Explicit conversion (casting) is required when narrowing from a larger type to a smaller one (e.g. `double` to `int`), since it risks losing data, and the programmer must acknowledge that with a cast.

**Q: Does casting a `double` to an `int` round or truncate?**
A: It truncates — the fractional part is simply discarded, not rounded. `(int) 9.99` gives `9`, not `10`. To round, you'd use `Math.round()` first.

**Q: What happens when you cast a value that's too large for the target type, e.g. `(byte)(300)`?**
A: It overflows and wraps around using the target type's bit representation, rather than clamping to the max value or throwing an exception. `(byte) 300` gives `44` because only the lowest 8 bits are kept.

**Q: Why does `char` widen to `int` but `int` doesn't widen to `char`?**
A: `char` is an unsigned 16-bit type representing a Unicode code point, and every `char` value fits safely within `int`'s range, so that widening is always safe. Going the other way isn't guaranteed safe — an arbitrary `int` might be negative or too large to represent as a `char`, so it requires an explicit cast.

**Q: What's the difference between `Integer.parseInt("42")` and `Integer.valueOf("42")`?**
A: `parseInt` returns a primitive `int`. `valueOf` returns an `Integer` object (and internally uses a cache for small values, `-128` to `127`, so repeated calls can return the same cached object).

**Q: What exception is thrown when parsing an invalid numeric string, and when is it thrown?**
A: `NumberFormatException`, thrown at runtime by `parseInt`/`parseDouble`/etc. when the string doesn't represent a valid number of that type (e.g. `Integer.parseInt("12a")`).

**Q: If you add an `int` and a `double` together without any explicit cast, what type is the result?**
A: `double`. Java implicitly widens the `int` operand to `double` before performing the addition, since one of the operands is already a `double` — this is called binary numeric promotion.
