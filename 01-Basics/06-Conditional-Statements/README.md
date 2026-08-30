# Conditional Statements (`if`, `else if`, `switch`)

## `if` / `else if` / `else`

```java
if (score >= 90) {
    grade = "A";
} else if (score >= 75) {
    grade = "B";
} else {
    grade = "C";
}
```

- Conditions must evaluate to `boolean` (unlike C, Java does not accept `int` as truthy/falsy).
- `else if` is just an `else` block containing another `if` — there's no separate keyword.

## `switch` — classic form

```java
switch (day) {
    case 1:
        name = "Monday";
        break;
    case 2:
        name = "Tuesday";
        break;
    default:
        name = "Unknown";
}
```

- Without `break`, execution **falls through** to the next case (runs every case below the matched one until it hits a `break` or the end).
- Works on `byte`, `short`, `char`, `int` (and their wrappers), `String` (since Java 7), and `enum`.
- `default` is optional and can appear anywhere, though convention puts it last.

## `switch` — arrow form (Java 14+)

```java
String name = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    default -> "Unknown";
};
```

- No fall-through — each arm only runs its own case.
- Can be used as an **expression** that produces a value directly (as above), removing the need for a separate variable assignment per case.
- Multiple labels can share one arm: `case 6, 7 -> "Weekend";`.

## Practice Questions / Exercises

- Write an `if`/`else if`/`else` chain that assigns a letter grade from a numeric score.
- Write a classic `switch` on an `int` month number that prints the season, deliberately omitting one `break` to observe fall-through, then fix it.
- Rewrite the same logic using the arrow-form `switch` expression.
- Write a `switch` on a `String` (e.g. a day name) that groups multiple case labels together (e.g. `"Saturday", "Sunday" -> "Weekend"`).
- Write nested `if` statements to classify a number as positive/negative and even/odd in one pass.

## Interview Questions

**Q: What is the key difference between classic `switch` and the arrow-form `switch` (Java 14+)?**
A: Classic `switch` falls through to subsequent cases unless each is terminated with `break`. The arrow-form (`->`) has no fall-through — only the matched case's arm runs — and it can be used directly as an expression that returns a value, avoiding a separately declared variable.

**Q: Can a `switch` statement work on a `String`?**
A: Yes, since Java 7. Internally the compiler compares using `.hashCode()` and `.equals()` on the string, so it behaves like a series of `.equals()` checks, not reference comparison.

**Q: What happens if you forget a `break` in a classic `switch`?**
A: Execution "falls through" into the next case's code and keeps running until it hits a `break` (or the `switch` block ends) — regardless of whether that next case's label matches. This is a common source of bugs.

**Q: Can `switch` work on `boolean` or `long`?**
A: No. `switch` supports `byte`, `short`, `char`, `int` (and their wrapper classes), `String`, and `enum` — not `boolean`, `long`, `float`, or `double`.

**Q: Why does Java require `if` conditions to be strictly `boolean`, unlike C?**
A: It's a deliberate type-safety choice — in C, `if (x)` treats any nonzero `int` as true, which is a common source of bugs (e.g. `if (x = 5)` accidental assignment instead of `==`). Java requires an actual `boolean` expression, catching such mistakes at compile time.

**Q: Is `default` required in a `switch` statement?**
A: No, it's optional. If no case matches and there's no `default`, the `switch` simply does nothing (for a statement) — though for a `switch` *expression*, the compiler requires all possible values to be covered, effectively forcing a `default` (or exhaustive enum cases) so the expression always yields a value.
