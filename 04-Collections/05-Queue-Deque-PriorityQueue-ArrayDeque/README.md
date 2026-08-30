# `Queue`/`Deque` — `PriorityQueue`, `ArrayDeque`

## `Queue`

FIFO (first-in-first-out) processing by convention. Core methods come in two flavors — throwing vs. `null`/`boolean`-returning on failure:

| Operation | Throws on failure | Returns special value on failure |
|---|---|---|
| Insert | `add(e)` | `offer(e)` (returns `false`) |
| Remove | `remove()` | `poll()` (returns `null` if empty) |
| Peek | `element()` | `peek()` (returns `null` if empty) |

- Prefer `offer`/`poll`/`peek` in most code — they signal "empty/full" via a return value instead of an exception, which is usually easier to handle in a loop.

## `Deque` (double-ended queue)

Supports insertion/removal at **both** ends — `addFirst`/`addLast`, `removeFirst`/`removeLast`, `peekFirst`/`peekLast` (plus `offer`/`poll` variants). Can be used as either a **queue** (FIFO: `offer`/`poll`) or a **stack** (LIFO: `push`/`pop`, which map to `addFirst`/`removeFirst`).

## `ArrayDeque`

Resizable-array-backed `Deque` implementation. **No capacity limit** (like `ArrayList`, grows as needed), and generally the best-performing choice for stack or queue usage — faster than `LinkedList` for this purpose (no per-node object overhead, better cache locality), and faster than `Stack` (which is legacy/synchronized).

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1); stack.push(2);
stack.pop();       // 2 — LIFO

Deque<Integer> queue = new ArrayDeque<>();
queue.offer(1); queue.offer(2);
queue.poll();      // 1 — FIFO
```

- Does **not** allow `null` elements (unlike `LinkedList`) — `null` is used internally as a sentinel for "empty."

## `PriorityQueue`

Backed by a **binary heap** — elements are ordered by natural ordering (`Comparable`) or a supplied `Comparator`; `poll()`/`peek()` always returns the **smallest** (or highest-priority, per the comparator) element, not insertion order.

```java
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(5); pq.offer(1); pq.offer(3);
pq.poll();   // 1 — smallest first
pq.poll();   // 3
```

- O(log n) for `offer`/`poll`, O(1) for `peek`.
- Iterating a `PriorityQueue` directly (e.g. for-each) does **not** yield sorted order — only repeated `poll()` calls do, since the heap only guarantees the root is the min/max, not full ordering.
- For max-heap behavior, pass `Comparator.reverseOrder()`, or negate the natural comparator.

## Practice Questions / Exercises

- Implement a stack and a queue both using `ArrayDeque`, and show the different output order (`push`/`pop` vs `offer`/`poll`) for the same sequence of insertions.
- Use `PriorityQueue<Integer>` to always process the smallest remaining task ID first; then redo it as a max-heap using `Comparator.reverseOrder()`.
- Use a `PriorityQueue<int[]>` with a custom `Comparator` to always pop the pair with the smallest second element.
- Show that iterating a `PriorityQueue` with a for-each loop does *not* give sorted order, but repeatedly `poll()`-ing does.

## Interview Questions

**Q: What's the difference between `add`/`remove`/`element` and `offer`/`poll`/`peek` on a `Queue`?**
A: They're functionally paired operations (insert/remove/peek), but they differ in failure behavior: `add`/`remove`/`element` throw an exception on failure (e.g. `remove()` on an empty queue throws `NoSuchElementException`), while `offer`/`poll`/`peek` return a special value instead (`false` for a failed `offer`, `null` for `poll`/`peek` on empty) — generally easier to use in loop conditions without try/catch.

**Q: Why is `ArrayDeque` generally preferred over both `Stack` and `LinkedList` for stack/queue usage?**
A: `Stack` extends the legacy `Vector` and its methods are `synchronized`, adding unnecessary overhead in single-threaded code. `LinkedList` has per-node object overhead and worse cache locality than a contiguous array. `ArrayDeque` is array-backed (like `ArrayList`, resizable, no per-element node objects), making it faster for pure stack/queue access patterns while still supporting both ends.

**Q: How does `PriorityQueue` order its elements internally, and does iterating it directly give you sorted output?**
A: It's backed by a binary heap, which only guarantees the smallest (or highest-priority) element is at the root/head — internal array order otherwise reflects the heap's structural invariant, not a full sort. Iterating with a for-each loop walks the internal array in heap order, *not* sorted order; only repeatedly calling `poll()` (which removes and re-heapifies) yields elements in fully sorted sequence.

**Q: What's the time complexity of `PriorityQueue.offer()` and `poll()`, and why?**
A: Both are O(log n) — inserting (`offer`) places the new element at the end and "sifts it up" to restore the heap property, while removing the root (`poll`) moves the last element to the root and "sifts it down"; both operations touch at most the height of the heap, which is O(log n) for n elements. `peek()` is O(1) since the min/max is always at the root.

**Q: Why does `ArrayDeque` disallow `null` elements, unlike `LinkedList`?**
A: `ArrayDeque` uses `null` internally as a sentinel value to represent "no element here" in its circular array implementation (and `poll`/`peek` use `null` to mean "empty"), so allowing actual `null` elements would make it impossible to distinguish a stored `null` from an empty slot/empty deque.

**Q: How would you implement a max-heap using `PriorityQueue`, which is a min-heap by default?**
A: Supply a `Comparator` that reverses natural ordering when constructing it — `new PriorityQueue<>(Comparator.reverseOrder())` — so the heap's "smallest per the comparator" is actually the largest per natural ordering, making `poll()` return the maximum element first.
