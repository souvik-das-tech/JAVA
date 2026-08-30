# `Set` — `HashSet`, `LinkedHashSet`, `TreeSet`

`Set` is a collection that disallows duplicate elements (by `equals()`/`hashCode()` contract).

## `HashSet`

Backed by a `HashMap` internally (elements stored as keys). **No ordering guarantee** — iteration order can appear arbitrary and may even change between runs.

```java
Set<String> set = new HashSet<>();
set.add("banana"); set.add("apple"); set.add("banana");  // duplicate ignored
// iteration order is unspecified
```

- O(1) average time for `add`/`remove`/`contains`, via hashing.
- Relies on correct `equals()`/`hashCode()` overrides on stored objects (see [[10-equals-hashCode-toString]]) — without them, custom objects are compared by identity, breaking de-duplication.

## `LinkedHashSet`

A `HashSet` variant that additionally maintains a doubly-linked list through all entries, preserving **insertion order** during iteration — same O(1) average performance, slightly more memory overhead.

```java
Set<String> set = new LinkedHashSet<>();
set.add("banana"); set.add("apple");
// iterates in insertion order: banana, apple
```

## `TreeSet`

Backed by a **red-black tree** (`TreeMap` internally) — keeps elements in **sorted order** (natural ordering via `Comparable`, or a supplied `Comparator`).

```java
Set<Integer> set = new TreeSet<>();
set.add(5); set.add(1); set.add(3);
// iterates as: 1, 3, 5
```

- O(log n) for `add`/`remove`/`contains` (tree traversal), slower than `HashSet`'s O(1) average.
- Implements `NavigableSet`, adding methods like `first()`, `last()`, `higher(x)`, `lower(x)`, `ceiling(x)`, `floor(x)`.
- Elements must be mutually comparable — either implement `Comparable`, or a `Comparator` must be passed to the constructor, or `add()` throws `ClassCastException`.

## Comparison

| | Order | Add/remove/contains | Nulls |
|---|---|---|---|
| `HashSet` | Unspecified | O(1) average | One `null` allowed |
| `LinkedHashSet` | Insertion order | O(1) average | One `null` allowed |
| `TreeSet` | Sorted order | O(log n) | No `null` (throws `NullPointerException` when comparing) |

## Practice Questions / Exercises

- Add the same set of strings, with a duplicate, to a `HashSet`, `LinkedHashSet`, and `TreeSet` — print each and compare the iteration order.
- Store custom objects (e.g. a `Point` class without `equals()`/`hashCode()` overridden) in a `HashSet` and show duplicates aren't removed; then add the overrides and show they now are.
- Use a `TreeSet<Integer>` and try `higher(5)`, `lower(5)`, `first()`, `last()`.
- Try adding `null` to a `TreeSet` and observe the `NullPointerException`.

## Interview Questions

**Q: What's the ordering guarantee (or lack of it) for `HashSet`, `LinkedHashSet`, and `TreeSet`?**
A: `HashSet` has no ordering guarantee at all — iteration order depends on hash bucket placement and can appear to change. `LinkedHashSet` preserves insertion order. `TreeSet` maintains sorted order (natural or via a supplied `Comparator`).

**Q: Why does storing custom objects in a `HashSet` require correctly overriding both `equals()` and `hashCode()`?**
A: `HashSet` uses `hashCode()` to find the bucket a new element would belong to, then `equals()` to check if an equal element already exists there. Without correct overrides, `Object`'s default identity-based versions are used, so two objects with the same logical data are treated as distinct — duplicates aren't detected and the "no duplicates" guarantee silently fails for your intended notion of equality.

**Q: How does `TreeSet` know how to order elements, and what happens if the elements aren't comparable?**
A: It either uses the elements' natural ordering (they must implement `Comparable`) or a `Comparator` supplied to the `TreeSet` constructor. If elements implement neither and no comparator is given, `add()` throws `ClassCastException` at runtime the first time it needs to compare two elements.

**Q: Why is `TreeSet`'s `add`/`contains` O(log n) while `HashSet`'s is O(1) average — and when would you accept that trade-off?**
A: `TreeSet` maintains a balanced binary search tree (red-black tree) to keep elements sorted, so every operation involves a tree traversal proportional to its height (log n). `HashSet` uses hashing, which is average O(1) but gives up ordering entirely. You'd accept `TreeSet`'s cost when you genuinely need sorted iteration or range queries (`headSet`, `tailSet`, `ceiling`, etc.) that `HashSet` simply can't provide.

**Q: Can a `TreeSet` contain `null`? Can a `HashSet`?**
A: `HashSet` allows a single `null` element (it's just another hashable "value," with a fixed bucket). `TreeSet` cannot — attempting to add `null` throws `NullPointerException`, because ordering requires comparing the new element against existing ones, and `null` can't be meaningfully compared.

**Q: What underlying data structure backs each of `HashSet`, `LinkedHashSet`, and `TreeSet`?**
A: `HashSet` is backed by a `HashMap` (elements stored as keys with a dummy value). `LinkedHashSet` is backed by a `LinkedHashMap`. `TreeSet` is backed by a `TreeMap` (a red-black tree) — in all three cases, the `Set` is essentially a thin wrapper reusing the corresponding `Map` implementation's key-storage machinery.
