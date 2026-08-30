# Concurrent Collections (`ConcurrentHashMap`, `CopyOnWriteArrayList`)

Regular collections (`HashMap`, `ArrayList`) are **not thread-safe** — concurrent modification from multiple threads can corrupt internal structure or throw `ConcurrentModificationException`. `java.util.concurrent` provides collections designed for safe concurrent access, generally with much better throughput than wrapping a regular collection in `Collections.synchronizedMap`/`synchronizedList`.

## `ConcurrentHashMap`

A thread-safe `Map` that allows **high concurrency** — unlike `Collections.synchronizedMap(new HashMap<>())`, which locks the *entire* map for every operation, `ConcurrentHashMap` internally partitions locking so multiple threads can read/write **different** parts concurrently without blocking each other.

```java
Map<String, Integer> map = new ConcurrentHashMap<>();
map.put("a", 1);
map.computeIfAbsent("b", k -> 0);
map.merge("a", 1, Integer::sum);   // atomic increment — safe under concurrent access
```

- Does **not** allow `null` keys or values (unlike `HashMap`) — this is deliberate: in a concurrent context, `map.get(key) == null` would be ambiguous between "no such key" and "mapped to null," and that ambiguity is worse under concurrency.
- Iteration is **weakly consistent** — it won't throw `ConcurrentModificationException` even if the map is modified during iteration, but the iterator may or may not reflect very recent concurrent changes (no snapshot guarantee, no exception either).
- Compound operations like `merge`, `computeIfAbsent`, `putIfAbsent` are atomic — safe building blocks for concurrent read-modify-write logic without needing external locking.

## `CopyOnWriteArrayList`

A thread-safe `List` where **every mutation** (`add`, `remove`, `set`) creates a **new copy** of the entire underlying array.

```java
List<String> list = new CopyOnWriteArrayList<>();
list.add("a");
for (String s : list) {   // safe to iterate even if another thread mutates concurrently
    System.out.println(s);
}
```

- Iterators operate on a **snapshot** of the array at the time the iterator was created — never throws `ConcurrentModificationException`, and never reflects concurrent mutations made after the iterator was obtained.
- Reads are fast and lock-free; writes are expensive (O(n) copy per mutation) — ideal for **read-heavy, write-rare** scenarios (e.g. a list of event listeners), poor for write-heavy workloads.

## Comparison with `Collections.synchronizedXxx`

| | Locking granularity | Iteration safety |
|---|---|---|
| `Collections.synchronizedMap(map)` | Whole map locked per operation | Must manually `synchronized` block around iteration |
| `ConcurrentHashMap` | Fine-grained (partitioned) | Weakly consistent, no exception, no manual locking needed |
| `Collections.synchronizedList(list)` | Whole list locked per operation | Must manually `synchronized` block around iteration |
| `CopyOnWriteArrayList` | Per-mutation full copy | Snapshot iterator, no exception, no manual locking needed |

## Practice Questions / Exercises

- Race 10 threads each calling `map.merge(key, 1, Integer::sum)` 1000 times on a `ConcurrentHashMap<String, Integer>` and confirm the final count is exactly correct (contrast with a plain `HashMap` under the same race, which would corrupt or lose updates).
- Iterate a `CopyOnWriteArrayList` while a separate thread concurrently adds elements to it — confirm no exception is thrown and the iteration reflects only the snapshot at creation time.
- Compare modifying a plain `ArrayList` during a for-each loop (throws `ConcurrentModificationException`) against a `CopyOnWriteArrayList` in the same scenario (doesn't throw).
- Try `map.put(null, 1)` on a `ConcurrentHashMap` and observe the `NullPointerException`.

## Interview Questions

**Q: Why is `ConcurrentHashMap` generally preferred over `Collections.synchronizedMap(new HashMap<>())`?**
A: `synchronizedMap` wraps every single method call in a lock on the *entire* map, so only one thread can access it at all, at any time — including two threads reading different keys. `ConcurrentHashMap` uses finer-grained internal locking (historically per-segment, now largely per-bucket via CAS/synchronized-on-bin), allowing many threads to read and even write concurrently as long as they're not touching the same bucket — much higher throughput under contention.

**Q: Why does `ConcurrentHashMap` disallow `null` keys and values, when `HashMap` allows them?**
A: In a concurrent context, if `get(key)` returns `null`, there'd be no way to distinguish "key absent" from "key present but mapped to null" without a further `containsKey()` check — and that two-step check-then-act itself isn't atomic under concurrent modification, making `null` values a correctness hazard specific to the concurrent case. The API designers chose to disallow them outright to eliminate the ambiguity.

**Q: What does "weakly consistent" iteration mean for `ConcurrentHashMap`?**
A: The iterator never throws `ConcurrentModificationException` even if the map is mutated during iteration, but it also offers no strong guarantee about whether it reflects those concurrent changes — it may see some, all, or none of them, and is guaranteed only to reflect the state at *some* point in time, not necessarily a full consistent snapshot.

**Q: When would `CopyOnWriteArrayList` be a poor choice?**
A: For write-heavy workloads — every mutation copies the *entire* backing array, which is O(n) per write and creates significant garbage-collection pressure as the list grows and mutates frequently. It's designed specifically for read-heavy, write-rare use cases (e.g. a registered-listeners list that's iterated often but modified rarely).

**Q: How does `CopyOnWriteArrayList` avoid `ConcurrentModificationException` during iteration?**
A: Its iterator is created over a fixed snapshot of the underlying array reference at the moment `iterator()` is called — since mutations create an entirely *new* array (never modifying the one the iterator is walking), the iterator's view is immutable and consistent for its entire lifetime, regardless of what other threads do to the list afterward.

**Q: Is `map.computeIfAbsent()` on a `ConcurrentHashMap` guaranteed to be atomic?**
A: Yes — `ConcurrentHashMap`'s compound methods (`computeIfAbsent`, `compute`, `merge`, `putIfAbsent`) are specified to be atomic per-key, meaning the entire read-check-write sequence for that key happens as a single indivisible operation from the perspective of other threads, making them safe building blocks for concurrent logic without needing external synchronization (though the supplied function should avoid other blocking/long-running work, since it may hold an internal lock while executing).
