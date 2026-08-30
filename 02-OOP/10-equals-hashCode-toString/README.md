# `equals()`, `hashCode()`, `toString()` Overriding

All three come from `Object` and have default implementations that are usually wrong for value-like classes.

## `toString()`

Default returns `ClassName@hexHashCode` (e.g. `Point@7a81197d`) — not useful for debugging/logging. Override it to return a human-readable representation.

```java
@Override
public String toString() {
    return "Point(" + x + ", " + y + ")";
}
```

## `equals()`

Default (`Object.equals`) is `==` — reference/identity comparison. For value semantics (two different objects considered "equal" if their data matches), override it.

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Point)) return false;
    Point p = (Point) o;
    return this.x == p.x && this.y == p.y;
}
```

## `hashCode()`

Must be overridden **whenever `equals()` is overridden**, and the two must stay consistent:

> **Contract:** if `a.equals(b)` is `true`, then `a.hashCode() == b.hashCode()` must also be `true`.
> (The reverse is not required — different objects can share a hash code; that's just a "hash collision".)

```java
@Override
public int hashCode() {
    return Objects.hash(x, y);   // combines multiple fields' hashes
}
```

- **Why this matters:** hash-based collections (`HashMap`, `HashSet`) use `hashCode()` to find the right bucket first, then `equals()` to confirm a match within that bucket. If two equal objects have different hash codes, a `HashSet` may store "duplicates" or a `HashMap.get()` may fail to find a key that's logically present.
- `equals()` must also satisfy: reflexive (`a.equals(a)`), symmetric (`a.equals(b) == b.equals(a)`), transitive, consistent, and `a.equals(null) == false`.

## Practice Questions / Exercises

- Write a `Point(int x, int y)` class overriding `toString()`, `equals()`, and `hashCode()`, then print an object directly (`System.out.println(point)`) to confirm `toString()` is called automatically.
- Create two `Point` objects with identical `x`/`y` but constructed separately — show `==` returns `false` while your overridden `equals()` returns `true`.
- Put several `Point` objects (including duplicates by value) into a `HashSet<Point>` — show duplicates collapse only if both `equals()` and `hashCode()` are correctly overridden; remove the `hashCode()` override and show the set no longer de-duplicates correctly.
- Use `Point` objects as keys in a `HashMap<Point, String>`, and show that `map.get(new Point(1,2))` finds the value stored under a *different* but equal `Point` instance.

## Interview Questions

**Q: Why must `hashCode()` be overridden whenever `equals()` is overridden?**
A: The general contract requires that equal objects (per `equals()`) produce the same hash code. Hash-based collections like `HashMap`/`HashSet` rely on this: they use `hashCode()` to locate a bucket and `equals()` to confirm the match within it — if equal objects hash differently, they could land in different buckets and the collection would treat them as distinct, breaking lookups and de-duplication.

**Q: If `a.hashCode() == b.hashCode()`, does that mean `a.equals(b)` is true?**
A: No — equal hash codes do not imply equal objects; that's a hash collision, which is legal and expected (a finite `int` hash space can't be unique per possible value). It's only the other direction — equal objects must have equal hash codes — that's a strict requirement.

**Q: What does the default `Object.equals()` do, and why is it often wrong for custom classes?**
A: It's equivalent to `==` — reference identity comparison, true only if both variables point to the exact same object. It's wrong for value-like classes where you want two *different* objects with the same data to be considered equal (e.g. two separately-created `Point(1,2)` instances).

**Q: What is the `equals()` contract's "symmetric" and "transitive" property?**
A: Symmetric: `a.equals(b)` must return the same result as `b.equals(a)`. Transitive: if `a.equals(b)` and `b.equals(c)` are both true, then `a.equals(c)` must also be true. Violating these (a classic trap: adding new fields in a subclass and not handling `equals()` carefully) leads to subtle collection bugs.

**Q: Why is `toString()`'s default output (`ClassName@hexHashCode`) rarely useful, and what does overriding it give you?**
A: The default just prints the class name and the object's default hash code in hex — it tells you nothing about the object's actual data. Overriding it to return meaningful field values makes debugging, logging, and `println`-based inspection immediately useful, since `toString()` is called implicitly by string concatenation, `println`, and most logging frameworks.

**Q: Is it a compile error to override `equals()` but not `hashCode()`?**
A: No — it compiles fine, but it silently breaks the `equals`/`hashCode` contract and causes subtle bugs in hash-based collections (most IDEs and static analyzers flag this as a warning/lint issue, even though the compiler itself allows it).
