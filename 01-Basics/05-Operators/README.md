# Operators

## Arithmetic

`+  -  *  /  %  ++  --`

```java
int a = 7, b = 2;
a + b; a - b; a * b; a / b; a % b; // 9, 5, 14, 3, 1
```

- `/` between two integers does **integer division** (truncates); use at least one `double`/`float` operand for a fractional result.
- `%` is remainder, also works on floating-point values.
- `++`/`--` (pre vs post) differ in *when* the value is read: `x++` returns the old value then increments; `++x` increments first then returns the new value.

## Relational

`==  !=  >  <  >=  <=` — compare two values, always produce a `boolean`.

- On primitives, compares actual values. On reference types, `==`/`!=` compare references (identity), not content — use `.equals()` for content comparison.

## Logical

`&&  ||  !` — combine boolean expressions.

- `&&` and `||` **short-circuit**: the right-hand side is not evaluated if the left side already determines the result (`false && x` never evaluates `x`; `true || x` never evaluates `x`).
- `&` and `|` also work on booleans but always evaluate both sides (no short-circuit) — rarely used for that purpose.

## Bitwise

`&  |  ^  ~  <<  >>  >>>` — operate on the binary representation of integer types.

- `&` AND, `|` OR, `^` XOR, `~` bitwise NOT (inverts all bits).
- `<<` left shift (multiply by 2 per shift), `>>` signed right shift (preserves sign bit — arithmetic shift), `>>>` unsigned right shift (fills with `0` regardless of sign).

## Assignment

`=  +=  -=  *=  /=  %=  &=  |=  ^=  <<=  >>=  >>>=`

- Compound assignment (`x += y`) is shorthand for `x = x + y`, but with an implicit cast back to `x`'s type — e.g. `byte b = 10; b += 5;` compiles even though `b + 5` is an `int`, because `+=` inserts a cast `b = (byte)(b + 5)`.

## Ternary

```java
int max = (a > b) ? a : b;
```

- Shorthand for a simple if/else that produces a value. `condition ? valueIfTrue : valueIfFalse`.

## Practice Questions / Exercises

- Write expressions showing integer division vs floating-point division with the same numbers.
- Demonstrate the difference between `x++` and `++x` by printing the expression result and the variable afterward.
- Show short-circuit evaluation: call a method with a side effect (e.g. prints something) on the right side of `&&`/`||` and prove it doesn't run when short-circuited.
- Use bitwise `&`, `|`, `^`, `<<`, `>>`, `>>>` on a couple of `int`s and print the binary results (`Integer.toBinaryString`).
- Use the ternary operator to find the max of two numbers, and to classify a number as even/odd.

## Interview Questions

**Q: What is the difference between `&&`/`||` and `&`/`|` when used with booleans?**
A: `&&`/`||` short-circuit — they skip evaluating the right operand if the result is already determined by the left. `&`/`|` always evaluate both operands, even on booleans, so they're used when side effects on the right side must always run, or in bitwise contexts on integers.

**Q: What's the difference between `x++` and `++x`?**
A: `x++` (post-increment) returns the current value of `x` *before* incrementing it; `++x` (pre-increment) increments `x` first and returns the new value. In a standalone statement they behave identically; the difference only matters when the expression's *value* is used immediately (e.g. `int y = x++;`).

**Q: What is the difference between `>>` and `>>>`?**
A: `>>` is a signed (arithmetic) right shift — it fills the vacated high bits with copies of the sign bit, preserving the sign of negative numbers. `>>>` is an unsigned (logical) right shift — it always fills with `0`, so a negative number shifted with `>>>` becomes a large positive number.

**Q: Why does `byte b = 10; b += 5;` compile, but `byte b = 10; b = b + 5;` doesn't?**
A: `b + 5` promotes to `int` (binary numeric promotion), and assigning an `int` back to a `byte` needs an explicit cast, so `b = b + 5;` fails to compile. `b += 5` is compound assignment, which implicitly inserts that narrowing cast: it's really `b = (byte)(b + 5);`.

**Q: Can the ternary operator be used as a statement in Java?**
A: No — the ternary operator (`?:`) is an expression that must produce and typically be used for its value (e.g. assigned to a variable or passed as an argument); it can't stand alone as a statement the way `if` can. Java requires you to assign or use the result.

**Q: What does `~x` do for an integer `x`?**
A: Bitwise NOT — it flips every bit. For two's-complement integers this is equivalent to `-x - 1` (e.g. `~5` is `-6`).
