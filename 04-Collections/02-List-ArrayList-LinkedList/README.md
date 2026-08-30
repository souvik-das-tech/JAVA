# `List` — `ArrayList`, `LinkedList`

`List` is an ordered collection that allows duplicates and supports index-based access.

## `ArrayList`

Backed by a resizable **array**.

```java
List<String> list = new ArrayList<>();
list.add("a"); list.add("b");
list.get(0);              // O(1) — direct index into the backing array
list.add(1, "x");         // O(n) — shifts every element after index 1
```

- **Fast random access** (`get(i)`) — O(1), since it's direct array indexing.
- **Slow insert/remove in the middle** — O(n), since every subsequent element must shift.
- Grows by allocating a new, larger backing array (typically 1.5x) and copying elements over when capacity is exceeded — amortized O(1) for `add` at the end, but an occasional O(n) resize.

## `LinkedList`

Backed by a **doubly-linked list** of nodes (each holding data + references to the previous/next node). Also implements `Deque`, so it works as a stack/queue too.

```java
List<String> list = new LinkedList<>();
list.add("a");
((LinkedList<String>) list).addFirst("x");   // O(1) — no shifting needed
list.get(2);                                   // O(n) — must walk the chain from an end
```

- **Fast insert/remove at the ends** (or given a node reference) — O(1).
- **Slow random access** — O(n), since reaching index `i` means walking `i` links from the nearest end.
- Higher memory overhead per element (each node stores two extra references) compared to `ArrayList`'s tightly packed array.

## When to use which

| Need | Prefer |
|---|---|
| Frequent random access by index | `ArrayList` |
| Frequent insert/remove at the start/middle | `LinkedList` (if truly frequent — often `ArrayDeque` is even better for pure queue/stack use) |
| Iteration only, mostly appends at the end | `ArrayList` (better cache locality, less memory overhead) |

In practice, `ArrayList` is the default choice unless you specifically need frequent middle/front insertions.

## Practice Questions / Exercises

- Create an `ArrayList<Integer>` and a `LinkedList<Integer>`, add 100,000 elements to each, then time inserting an element at index 0 repeatedly on both to observe the performance difference.
- Use `ListIterator` to insert an element in the middle of a `List` while iterating.
- Use `LinkedList` as a stack (`push`/`pop`) and as a queue (`offer`/`poll`) via its `Deque` methods.
- Convert between a `List` and an array using `toArray()`, and between an array and a `List` using `Arrays.asList()`.

## Interview Questions

**Q: What's the core structural difference between `ArrayList` and `LinkedList`, and how does it drive their performance trade-offs?**
A: `ArrayList` is backed by a contiguous resizable array, giving O(1) index-based `get`/`set` but O(n) insert/remove in the middle (elements must shift). `LinkedList` is backed by a doubly-linked list of nodes, giving O(1) insert/remove at a known position (just relinking pointers) but O(n) index-based access (must traverse from an end).

**Q: Why is `ArrayList.add()` (appending at the end) usually considered O(1) despite occasionally needing to resize the backing array?**
A: It's *amortized* O(1) — most appends just write into existing spare capacity, and the occasional resize (allocate a bigger array, copy all elements, typically ~O(n)) happens rarely enough (capacity growing geometrically, e.g. 1.5x) that the average cost per `add` across many operations works out to constant time.

**Q: When would `LinkedList` actually outperform `ArrayList` in practice?**
A: When you frequently insert/remove elements at the head, tail, or a position you already hold an iterator/node reference to, and rarely need random access by index — e.g. implementing a queue/deque where you constantly push/pop from the ends. If you also need random indexed access mixed in, `ArrayList` (or `ArrayDeque` for pure queue/stack use) usually wins overall due to better cache locality.

**Q: Is `Arrays.asList(array)` the same as `new ArrayList<>(Arrays.asList(array))`? What's the trap?**
A: No — `Arrays.asList()` returns a fixed-size list backed directly by the original array; you can `set()` elements (which writes through to the array) but calling `add()`/`remove()` throws `UnsupportedOperationException`. Wrapping it in `new ArrayList<>(...)` copies the elements into a genuinely resizable, independent `ArrayList`.

**Q: Why does `LinkedList` implement `Deque` in addition to `List`?**
A: Because its doubly-linked structure naturally supports O(1) insertion/removal at both ends, which is exactly what `Deque` (double-ended queue) requires — so `LinkedList` can serve as a stack, queue, or deque implementation in addition to being a general-purpose `List`.

**Q: What happens if you modify a `List` (e.g. `add`/`remove`) while iterating over it with a for-each loop?**
A: It throws `ConcurrentModificationException` at the next `iterator.next()` call — both `ArrayList` and `LinkedList`'s iterators track a modification count and fail fast if the list is structurally modified outside the iterator itself. Use `Iterator.remove()` (or `ListIterator`'s add/remove) to safely modify during iteration instead.
