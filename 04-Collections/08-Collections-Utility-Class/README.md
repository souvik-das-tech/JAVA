# `Collections` Utility Class

`java.util.Collections` (plural — not to be confused with the `Collection` interface, see [[01-Collection-Hierarchy-Overview]]) is a final class of `static` helper methods that operate on `Collection`/`List` instances.

```java
List<Integer> list = new ArrayList<>(List.of(5, 3, 1, 4));

Collections.sort(list);                    // ascending, in place: [1, 3, 4, 5]
Collections.sort(list, Comparator.reverseOrder());  // descending: [5, 4, 3, 1]
Collections.reverse(list);                 // reverses current order in place
Collections.shuffle(list);                 // random reorder
Collections.max(list); Collections.min(list);
Collections.binarySearch(list, 4);         // list MUST already be sorted for this to work correctly
Collections.frequency(list, 3);            // count of occurrences
```

## Unmodifiable / synchronized wrappers

```java
List<String> readOnly = Collections.unmodifiableList(list);   // any add()/remove() on it throws UnsupportedOperationException
readOnly.add("x");     // throws — the wrapper is read-only, though it still reflects changes to the underlying `list`

List<String> syncList = Collections.synchronizedList(new ArrayList<>());  // thread-safe wrapper (manual synchronization still needed for compound actions/iteration)
```

- `unmodifiableXxx()` wraps a collection so its own mutation methods throw — but it's a **view**, not a deep copy: changes made directly to the original underlying collection still show through the wrapper.
- Since Java 9, prefer `List.of(...)`, `Set.of(...)`, `Map.of(...)` for genuinely immutable collections built from scratch (no underlying mutable collection at all) — `Collections.unmodifiableList` is more for wrapping an *existing* mutable collection you don't own.
- `synchronizedXxx()` wraps individual method calls in `synchronized` blocks, but compound operations (like iterating, or "check-then-act") still need external synchronization (`synchronized(syncList) { ... }`) to be truly thread-safe — see `ConcurrentHashMap`/`CopyOnWriteArrayList` in [[06-Multithreading]] for a more robust alternative.

## Empty / singleton helpers

```java
Collections.emptyList(); Collections.emptyMap(); Collections.emptySet();      // immutable, shared empty instances
Collections.singletonList("only-one");                                        // immutable one-element list
```

## Practice Questions / Exercises

- Sort a `List<Integer>` ascending, then descending using `Collections.sort` with both no comparator and `Comparator.reverseOrder()`.
- Use `Collections.binarySearch` on a sorted list, then try it on an unsorted list and observe the unreliable/wrong result.
- Wrap a `List` with `Collections.unmodifiableList` and confirm `add()` throws, but a mutation on the *original* list is still visible through the wrapper.
- Use `Collections.max`/`min` with a custom `Comparator` (e.g. to find the employee with the highest salary from [[07-Comparable-vs-Comparator]]'s example).

## Interview Questions

**Q: What's the difference between `Collection` and `Collections`, and why is the naming confusing?**
A: `Collection` (singular) is the root interface for `List`/`Set`/`Queue`. `Collections` (plural) is an unrelated static utility class of helper methods (`sort`, `reverse`, `unmodifiableList`, etc.) that operate on those collections — the near-identical name is a frequent source of confusion for newcomers, but they have nothing to do with each other structurally.

**Q: What happens if you call `Collections.binarySearch()` on a list that isn't sorted?**
A: The behavior is undefined/unspecified — it may return an incorrect index or a wrong "not found" result, without necessarily throwing any exception, because binary search's algorithm assumes sorted input to correctly narrow its search range; violating that assumption silently produces garbage results rather than a clear error.

**Q: Is `Collections.unmodifiableList(list)` a deep copy of `list`? What's the practical implication?**
A: No — it's a thin read-only *view* over the same underlying list. You can't mutate the collection through the wrapper, but if you (or other code) still hold a reference to the original mutable `list` and mutate it directly, those changes are immediately visible through the "unmodifiable" wrapper too — it only prevents mutation via the wrapper's own reference, not true immutability of the data.

**Q: Does `Collections.synchronizedList()` make iteration over the list thread-safe by itself?**
A: No — it synchronizes each individual method call (e.g. a single `add` or `get`), but a *sequence* of operations like iterating (which is really a sequence of `hasNext`/`next` calls) isn't atomic as a whole. You must manually wrap iteration in `synchronized (syncList) { for (...) {...} }` to prevent another thread from concurrently modifying it mid-iteration.

**Q: Since Java 9's `List.of(...)` exists, when would you still reach for `Collections.unmodifiableList()`?**
A: `List.of(...)` creates a brand-new, genuinely immutable list from given elements — there's no underlying mutable collection at all. `Collections.unmodifiableList()` is for the specific case where you already have an existing mutable `List` (perhaps built up via a loop) and want to hand out a read-only view of *that specific collection* to callers, without copying it.

**Q: What does `Collections.emptyList()` return, and why use it instead of `new ArrayList<>()`?**
A: A shared, immutable, type-safe empty `List` singleton instance. It's preferred over `new ArrayList<>()` when you specifically want to signal/enforce "this is empty and must stay empty" (e.g. as a default return value), avoiding an unnecessary allocation and preventing accidental mutation by callers who might otherwise assume they can add to a freshly-created empty list.
