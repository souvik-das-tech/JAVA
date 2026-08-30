# `Map` — `HashMap`, `LinkedHashMap`, `TreeMap`

`Map` stores key-value pairs with unique keys (adding a duplicate key overwrites the old value). Not a `Collection` — see [[01-Collection-Hierarchy-Overview]].

## `HashMap`

Backed by an array of buckets, each holding a linked list (or, since Java 8, a balanced tree once a bucket gets large enough — 8+ entries — for better worst-case lookup).

```java
Map<String, Integer> map = new HashMap<>();
map.put("apple", 1);
map.put("apple", 2);      // overwrites — value is now 2
map.get("apple");         // 2
map.get("missing");       // null — no such key
```

- O(1) average for `put`/`get`/`remove`, via hashing the key.
- **No ordering guarantee** for iteration.
- Allows **one** `null` key and multiple `null` values.
- How it works: `key.hashCode()` determines the bucket; within a bucket, `key.equals()` distinguishes entries — same contract dependency as `HashSet`.

## `LinkedHashMap`

`HashMap` + a linked list threading through entries in **insertion order** (or, optionally, access order — useful for building an LRU cache via `removeEldestEntry`).

## `TreeMap`

Backed by a red-black tree — keeps keys in **sorted order**. O(log n) operations. Implements `NavigableMap` (`firstKey()`, `lastKey()`, `higherKey()`, `ceilingKey()`, ...). No `null` keys (throws `NullPointerException`), since keys must be comparable.

## Common operations

```java
map.getOrDefault("missing", 0);                 // avoid manual null-check
map.putIfAbsent("apple", 99);                   // only sets if key isn't already present
map.computeIfAbsent("list", k -> new ArrayList<>()).add("x");  // classic "map of lists" pattern
map.merge("apple", 1, Integer::sum);            // increment-or-initialize counter pattern
for (Map.Entry<String, Integer> e : map.entrySet()) { ... }    // preferred iteration style
```

## Practice Questions / Exercises

- Build a word-frequency counter from a sentence using `HashMap<String, Integer>` and `merge()`.
- Compare iteration order of the same entries inserted into `HashMap`, `LinkedHashMap`, and `TreeMap`.
- Build a "group items by category" structure using `Map<String, List<String>>` and `computeIfAbsent`.
- Use `TreeMap`'s `firstKey()`, `lastKey()`, and `ceilingKey()` on a map of sorted numeric keys.

## Interview Questions

**Q: How does `HashMap` decide which bucket a key goes into, and what happens when two keys land in the same bucket (a collision)?**
A: It computes `key.hashCode()`, applies an internal hash-spreading function, and maps the result to one of the backing array's bucket indices. Colliding keys in the same bucket are stored in a linked list (or, since Java 8, a red-black tree once a bucket holds 8+ entries, to bound worst-case lookup at O(log n) instead of O(n)); `equals()` is used to tell colliding keys apart when looking one up.

**Q: What's the difference between `map.get(key)` returning `null` because the key is absent, vs. the key being present with a `null` value?**
A: Both cases return `null` from `get()`, so you can't distinguish them with `get()` alone — use `containsKey(key)` to check presence explicitly, or `getOrDefault(key, fallback)` if you just want a sensible default regardless of which case it is.

**Q: How would you implement a simple LRU cache using the Collections Framework?**
A: `LinkedHashMap` has a constructor overload for access-order iteration (`new LinkedHashMap<>(cap, loadFactor, true)`) plus an overridable `removeEldestEntry(Map.Entry)` hook — overriding it to return `true` once the map exceeds a size threshold automatically evicts the least-recently-used entry on each `put`/`get`, giving a working LRU cache in just a few lines.

**Q: What's the difference between `putIfAbsent()` and `computeIfAbsent()`?**
A: `putIfAbsent(key, value)` only inserts if the key is absent (or mapped to `null`), but you must have the *value* ready upfront even if it won't be used. `computeIfAbsent(key, function)` only *invokes* the supplied function (and inserts its result) if the key is absent — useful when the value is expensive to construct or when you want to lazily initialize a nested structure (e.g. `map.computeIfAbsent(key, k -> new ArrayList<>())`).

**Q: Why must a `HashMap` key's class have consistent `equals()`/`hashCode()`, and what breaks if you mutate a key after inserting it?**
A: `HashMap` relies on `hashCode()` to find the bucket and `equals()` to locate the entry within it, both at insertion and lookup time. If a key's hash code changes after insertion (because you mutated a field it depends on), a later `get()`/`remove()` with an equal key computes a *different* bucket than where the entry actually lives — the entry becomes silently unreachable ("lost" in the map), which is why mutable objects make poor map keys.

**Q: What's the time complexity of `HashMap.get()` in the worst case, and how did Java 8 improve it?**
A: Before Java 8, worst case was O(n) per bucket if all keys hash-collided into one bucket (a linked list). Since Java 8, once a single bucket accumulates 8+ colliding entries (and the table is large enough), that bucket is converted from a linked list into a balanced red-black tree, bounding the worst case to O(log n) instead of O(n) — mainly a defense against adversarially crafted hash collisions.
