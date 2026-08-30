# `Comparable` vs `Comparator`

Both define ordering, but they answer different questions: "what is this class's *natural* order?" vs "here's *a* way to order these, possibly not the natural one."

## `Comparable<T>`

Implemented **by the class itself** — defines its single natural ordering. One method: `compareTo(T other)`.

```java
class Employee implements Comparable<Employee> {
    int age;
    public int compareTo(Employee other) {
        return Integer.compare(this.age, other.age);   // ascending by age
    }
}
Collections.sort(employees);   // uses compareTo() — natural ordering
```

- Return negative if `this < other`, zero if equal, positive if `this > other`.
- A class can only have **one** `compareTo()` implementation — its one "natural" order.
- `TreeSet`/`TreeMap` use `compareTo()` by default (if no `Comparator` is supplied), and collections of `Comparable` elements can be sorted with the no-arg `Collections.sort(list)`.

## `Comparator<T>`

A **separate** object defining *a* way to order `T` — you can have as many as you want, for different sort criteria, without touching the class itself.

```java
Comparator<Employee> byName = (e1, e2) -> e1.name.compareTo(e2.name);
Comparator<Employee> bySalaryDesc = Comparator.comparingDouble((Employee e) -> e.salary).reversed();

Collections.sort(employees, byName);
employees.sort(bySalaryDesc);
```

- Useful when the class isn't yours to modify (can't add `Comparable`), or you need multiple different orderings.
- Java 8+ fluent builders: `Comparator.comparing(keyExtractor)`, `.thenComparing(...)` (tie-breaking), `.reversed()`.

```java
Comparator<Employee> byDeptThenName = Comparator
    .comparing((Employee e) -> e.department)
    .thenComparing(e -> e.name);
```

## Comparison

| | `Comparable` | `Comparator` |
|---|---|---|
| Where defined | Inside the class (`implements Comparable<T>`) | Separate, external object |
| How many orderings | One (the "natural" order) | As many as needed |
| Method | `compareTo(T other)` | `compare(T a, T b)` |
| Used by default in | `Collections.sort(list)`, `TreeSet`/`TreeMap` with no comparator | `Collections.sort(list, comparator)`, `TreeSet`/`TreeMap` constructed with one |

## Practice Questions / Exercises

- Make an `Employee` class implement `Comparable<Employee>` by age, and sort a `List<Employee>` with `Collections.sort(list)`.
- Write two separate `Comparator<Employee>` instances (by name, by salary descending) and sort the same list both ways without changing `Employee`.
- Chain `Comparator.comparing(...).thenComparing(...)` to sort by department, then by name within each department.
- Put `Employee` objects (implementing `Comparable`) into a `TreeSet`, then create a second `TreeSet` using a custom `Comparator` instead, and compare their iteration order.

## Interview Questions

**Q: What is the core difference between `Comparable` and `Comparator`?**
A: `Comparable` is implemented by the class being compared, defining its single natural ordering (`compareTo`). `Comparator` is a separate, external object implementing a *particular* way to compare two instances of a type (`compare`) — you can define as many different `Comparator`s for a class as you need, without modifying the class itself.

**Q: When would you use `Comparator` instead of making a class `Comparable`?**
A: When the class either can't be modified (e.g. it's from a third-party library), or when you need more than one ordering (e.g. sort employees by name in one place, by salary in another) — `Comparable` only gives you one fixed "natural" order per class, while `Comparator`s are unlimited and swappable per call site.

**Q: What must `compareTo()`/`compare()` return, and what do negative/zero/positive mean?**
A: An `int`: negative if the first argument should sort before the second, zero if they're considered equal for ordering purposes, positive if the first should sort after the second. The exact magnitude doesn't matter, only the sign (though using something like `Integer.compare(a, b)` internally is safer than naive subtraction, which can overflow).

**Q: What is the contract between `equals()` and `compareTo()` — must they be consistent?**
A: They *should* be consistent ("consistent with equals": `x.compareTo(y) == 0` should imply `x.equals(y)` returns true), though it isn't strictly enforced by the compiler. Being inconsistent is legal but can cause surprising behavior in sorted collections like `TreeSet`/`TreeMap`, which use `compareTo()`/`compare()` alone (not `equals()`) to determine both ordering and uniqueness — meaning two "unequal" objects that compare as `0` are treated as duplicates and one is silently dropped.

**Q: How do `Comparator.comparing()` and `.thenComparing()` work together for multi-field sorting?**
A: `Comparator.comparing(keyExtractor)` builds a comparator from a single field/key. `.thenComparing(secondKeyExtractor)` chains a tie-breaker: if the first comparator considers two elements equal (returns 0), the second comparator's result is used instead — letting you express "sort by department, then by name within each department" fluently without writing a manual multi-field `compare()` method.

**Q: If a class implements `Comparable` but you pass a `Comparator` to `Collections.sort(list, comparator)`, which one is actually used?**
A: The explicitly supplied `Comparator` always takes precedence over the class's own `compareTo()` — `Collections.sort(list, comparator)` and `TreeSet`/`TreeMap` constructed with a comparator ignore `Comparable` entirely and use the given `Comparator` for all ordering decisions.
