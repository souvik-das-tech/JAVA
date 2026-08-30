# `Collection` Hierarchy Overview

Study notes only — no single code file for this topic (it's a conceptual map; individual collection types get their own code files in later topics).

The Java Collections Framework is rooted at the `Collection` interface (for single-element collections) and `Map` (for key-value pairs, which is **not** a `Collection`).

```
Iterable
└── Collection
    ├── List        — ordered, allows duplicates, index-based access (ArrayList, LinkedList, Vector)
    ├── Set         — no duplicates (HashSet, LinkedHashSet, TreeSet)
    └── Queue       — FIFO/priority processing (LinkedList, PriorityQueue, ArrayDeque)
        └── Deque   — double-ended queue (ArrayDeque, LinkedList)

Map                  — key-value pairs, NOT a Collection (HashMap, LinkedHashMap, TreeMap)
```

- `Collection` extends `Iterable` — this is why every `Collection` (List, Set, Queue) can be used in a for-each loop; `Map` does **not** extend `Collection` or `Iterable` directly (you iterate its `keySet()`, `values()`, or `entrySet()` instead, each of which is a `Collection`/`Set`).
- Choosing the right collection is about the guarantees you need:
  - Need order + duplicates + index access? → `List`.
  - Need uniqueness? → `Set`.
  - Need key-based lookup? → `Map`.
  - Need FIFO/LIFO/priority processing? → `Queue`/`Deque`.
- All the concrete implementations (`ArrayList`, `HashSet`, `HashMap`, ...) trade off differently on: insertion order preservation, sorting, duplicate handling, null support, and performance characteristics (covered per-topic in this section).

## Practice Questions / Exercises

- Draw (on paper or in the README) the interface hierarchy above from memory, then check it against this note.
- For a given requirement ("I need to store unique usernames", "I need to process items in the order they arrive, FIFO", "I need fast key lookup"), name which top-level Collections Framework interface fits.
- List which of `List`, `Set`, `Queue`, `Map` allow duplicate elements/keys, and which preserve insertion order by default.

## Interview Questions

**Q: What is the difference between `Collection` and `Collections`?**
A: `Collection` (singular) is the root interface of the framework's single-element data structures (`List`, `Set`, `Queue`). `Collections` (plural) is an unrelated **utility class** full of `static` helper methods (`sort`, `reverse`, `unmodifiableList`, etc.) that operate on collections — easy to confuse by name, but they serve completely different purposes.

**Q: Why doesn't `Map` extend `Collection`?**
A: `Map` stores key-value *pairs*, not single elements, so its core operations (`get(key)`, `put(key, value)`) don't fit `Collection`'s single-element contract (`add(element)`, `contains(element)`). Instead, `Map` exposes `Collection`-compatible *views* of itself — `keySet()` (a `Set`), `values()` (a `Collection`), and `entrySet()` (a `Set` of key-value pairs) — for when you need to iterate or treat part of it as a `Collection`.

**Q: What's the main structural difference between `List`, `Set`, and `Queue`?**
A: `List` is an ordered sequence that allows duplicates and supports index-based access/insertion. `Set` disallows duplicate elements and generally doesn't support index-based access. `Queue` (and its subinterface `Deque`) is designed around ordered processing from one or both ends (FIFO, LIFO, or priority-based), not arbitrary indexed access.

**Q: Why does `Collection` extend `Iterable`, and what does that give you for free?**
A: `Iterable` requires a single method, `iterator()`, which is exactly what the for-each loop (`for (T x : collection)`) is syntactic sugar for under the hood. Because every `Collection` implementation provides an `iterator()`, all of them automatically work in for-each loops without any extra code.

**Q: If you need to pick between a `List`, `Set`, or `Map` for a new piece of code, what's the first question you'd ask?**
A: Whether elements need to be looked up by a key (→ `Map`), whether duplicates are meaningful and order/index access matters (→ `List`), or whether the requirement is really "does this collection contain X" with uniqueness enforced and no need for index access (→ `Set`) — the access pattern you need drives the choice more than performance, which is a secondary consideration once the interface is chosen.
