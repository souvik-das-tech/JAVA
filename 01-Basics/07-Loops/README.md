# Loops (`for`, `while`, `do-while`, enhanced for)

## `for`

```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);
}
```

- Three parts: initialization (runs once), condition (checked before each iteration), update (runs after each iteration). Any part can be omitted (`for (;;)` is an infinite loop).

## `while`

```java
int i = 0;
while (i < 5) {
    System.out.println(i);
    i++;
}
```

- Condition checked **before** each iteration — body may run zero times.

## `do-while`

```java
int i = 0;
do {
    System.out.println(i);
    i++;
} while (i < 5);
```

- Condition checked **after** each iteration — body always runs **at least once**, even if the condition is false from the start.

## Enhanced `for` (for-each)

```java
int[] nums = {1, 2, 3};
for (int n : nums) {
    System.out.println(n);
}
```

- Iterates over every element of an array or any `Iterable` (collections). No index variable, no manual bounds-checking.
- Can't modify the underlying array/collection structure while iterating this way, and you don't get the index for free — use a classic `for` loop when you need the index or need to mutate.

## `break` and `continue`

- `break` exits the loop entirely.
- `continue` skips the rest of the current iteration and moves to the next one.
- Labeled versions (`break outer;`, `continue outer;`) let you control an outer loop from inside a nested one.

## Practice Questions / Exercises

- Print numbers 1 to 10 using a `for` loop, then the same using a `while` loop.
- Write a `do-while` loop that runs its body once even though the starting condition is already false, to prove it always executes at least once.
- Use an enhanced `for` loop to sum all elements of an `int[]` array.
- Write nested loops (multiplication table) and use a labeled `break` to exit both loops early once a condition is met (e.g. product exceeds 50).
- Use `continue` inside a loop to skip printing multiples of 3 from 1 to 20.

## Interview Questions

**Q: What's the key difference between `while` and `do-while`?**
A: `while` checks its condition *before* each iteration, so the body may execute zero times. `do-while` checks the condition *after* the body runs, so the body always executes at least once, regardless of the condition.

**Q: When would you prefer an enhanced `for` loop over a classic `for` loop, and when not?**
A: Prefer enhanced `for` when you just need to read every element in order and don't need the index (cleaner, less error-prone — no off-by-one risk). Use a classic `for` loop when you need the index, need to iterate multiple collections in lockstep, need to iterate backwards, or need to modify the collection/array structure during iteration.

**Q: What does a labeled `break`/`continue` do, and why is it needed?**
A: A label (e.g. `outer:`) placed before a loop lets `break outer;` or `continue outer;` from inside a nested loop target that specific outer loop, rather than only the innermost one. It's needed because unlabeled `break`/`continue` only ever affects the nearest enclosing loop.

**Q: Can you modify an array while iterating it with an enhanced `for` loop?**
A: You can modify individual elements' *values* (for arrays) but not the structure (can't resize an array anyway). For collections, structurally modifying the collection (e.g. removing an element) during an enhanced `for` loop throws a `ConcurrentModificationException`, because the for-each uses an `Iterator` internally and detects the modification.

**Q: What happens with `for(;;)`?**
A: All three clauses of a classic `for` loop are optional. `for(;;)` omits initialization, condition, and update entirely, producing an infinite loop (equivalent to `while(true)`) — you'd need a `break` or `return` inside to exit it.

**Q: Is there a performance difference between the loop types?**
A: Not meaningfully for the same logic — they compile down to essentially the same bytecode/looping constructs. The choice is about readability and correctness for the situation, not performance.
