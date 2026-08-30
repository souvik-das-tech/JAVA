# `Iterator` & `ListIterator`

## `Iterator`

The standard way to traverse any `Collection` without exposing its internal structure — what a for-each loop compiles down to under the hood.

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("remove-me")) {
        it.remove();   // safe removal DURING iteration
    }
}
```

- `hasNext()` / `next()` / `remove()` — that's the whole interface (three methods).
- `it.remove()` is the **only** safe way to remove elements while iterating; calling `list.remove(...)` directly during a for-each/iterator loop throws `ConcurrentModificationException`.
- Fail-fast behavior: most `Collection` iterators detect structural modification (add/remove) made outside the iterator itself during iteration and throw `ConcurrentModificationException` on the next `next()`/`hasNext()` call, rather than silently producing wrong results.

## `ListIterator`

A `List`-specific extension of `Iterator` with **bidirectional** traversal and in-place mutation support.

```java
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String s = lit.next();
    lit.set(s.toUpperCase());   // replace the last-returned element
    if (s.equals("insert-after-me")) {
        lit.add("new-item");    // insert without shifting via a separate list.add(index, ...) call
    }
}
// can also walk backwards:
while (lit.hasPrevious()) {
    lit.previous();
}
```

- Adds `hasPrevious()`/`previous()` (backward traversal), `set(e)` (replace the last element returned by `next()`/`previous()`), `add(e)` (insert at the current cursor position), and `nextIndex()`/`previousIndex()`.
- Only available on `List` implementations (`list.listIterator()`), not on `Set`/`Queue` — since it relies on positional/index semantics that only an ordered, indexable structure has.

## Practice Questions / Exercises

- Use `Iterator.remove()` to remove all even numbers from a `List<Integer>` while iterating — then show that calling `list.remove()` directly in the same loop throws `ConcurrentModificationException`.
- Use `ListIterator` to walk a `List<String>` forward, uppercase every element via `set()`, then walk it backward and print each element.
- Use `ListIterator.add()` to insert a new element right after a specific target element, without using indexed `list.add(i, ...)`.
- Write a for-each loop over a `List`, and inside it, write (commented out, or in a try/catch) an attempted direct `list.add(...)` — confirm you get `ConcurrentModificationException`.

## Interview Questions

**Q: Why does modifying a `List` directly during a for-each loop throw `ConcurrentModificationException`, but using `Iterator.remove()` doesn't?**
A: A for-each loop uses an internal `Iterator` that tracks a `modCount` snapshot from when it was created; any structural change made outside that iterator (e.g. `list.remove()`) increments the list's `modCount`, and the iterator detects the mismatch on the next call and fails fast. `Iterator.remove()` updates that tracked `modCount` itself as part of the removal, so the iterator's internal state stays consistent and no exception is thrown.

**Q: What's the difference between `Iterator` and `ListIterator`?**
A: `Iterator` supports only forward traversal and element removal (`hasNext`, `next`, `remove`). `ListIterator` (available only on `List`s) adds backward traversal (`hasPrevious`, `previous`), in-place element replacement (`set`), and insertion at the cursor (`add`) — a richer, bidirectional, mutation-capable cursor.

**Q: Is `ConcurrentModificationException` guaranteed to be thrown whenever a collection is modified during iteration?**
A: No — it's a best-effort ("fail-fast") mechanism, not a guarantee; the Javadoc explicitly states it should not be relied upon for correctness, only used to detect bugs. In some edge cases (e.g. certain modification patterns), the exception may not be thrown even though the iteration produces undefined behavior.

**Q: How would you implement your own class so it can be used in a for-each loop?**
A: Implement the `Iterable<T>` interface, which requires providing an `iterator()` method returning an `Iterator<T>` (itself implementing `hasNext()` and `next()`, typically as a custom or anonymous inner class holding the traversal state) — the compiler translates `for (T x : myIterable)` directly into calls to that `iterator()`/`hasNext()`/`next()`.

**Q: Can `ListIterator.add()` be used to insert while iterating without the shifting cost of `list.add(index, element)`?**
A: The underlying cost still depends on the list's implementation (an `ArrayList` still has to shift internally), but `ListIterator.add()` avoids the *separate* index-lookup/traversal cost of calling `list.add(index, e)` after already being positioned at that spot mid-iteration — you insert exactly where the cursor already is, without recomputing a position.

**Q: What does `it.remove()` actually remove — the element you're about to visit, or the one you just visited?**
A: The element most recently returned by the preceding `next()` (or `previous()`, for `ListIterator`) call — you must call `next()` at least once before calling `remove()`, and you can't call `remove()` twice in a row without an intervening `next()`/`previous()`, or it throws `IllegalStateException`.
