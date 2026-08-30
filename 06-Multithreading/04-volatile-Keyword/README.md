# The `volatile` Keyword

`volatile` guarantees **visibility** of a field's value across threads — it does **not** provide atomicity or mutual exclusion the way `synchronized` does.

## The visibility problem

```java
class Flag {
    boolean running = true;   // NOT volatile
}

// Thread A:
while (flag.running) { /* busy loop */ }

// Thread B:
flag.running = false;
```

Without `volatile`, thread A may **never see** thread B's update — each CPU core can cache `running` locally (for performance), and there's no guarantee the write is ever flushed to main memory (or that A re-reads from main memory instead of its cache) in a timely way. This can cause `A`'s loop to run forever, even though `running` was clearly set to `false`.

## The fix

```java
class Flag {
    volatile boolean running = true;
}
```

- `volatile` forces every read of the field to go to main memory (not a thread-local cache), and every write to be immediately flushed to main memory — establishes a **happens-before** relationship: a write to a volatile field is guaranteed visible to any thread that subsequently reads it.

## What `volatile` does **not** do

```java
volatile int count = 0;
count++;   // STILL not atomic — read, increment, write are still 3 separate steps!
```

- `volatile` only fixes visibility, not compound (read-modify-write) atomicity. `count++` on a `volatile int` can still lose updates under concurrent access — you'd still need `synchronized`, `AtomicInteger`, or a lock for that.
- `volatile` is appropriate for simple flags/state where one thread writes and others only read (e.g. a "shutdown requested" flag), not for counters or anything involving read-modify-write logic.

## `volatile` vs `synchronized`

| | Guarantees | Cost | Use for |
|---|---|---|---|
| `volatile` | Visibility only | Cheap (no locking) | Simple flags, single-writer/multi-reader state |
| `synchronized` | Visibility **and** atomicity/mutual exclusion | More expensive (locking, potential blocking) | Compound operations, multi-step invariants |

## Practice Questions / Exercises

- Write a `Flag` class with a non-`volatile` boolean, start a thread that busy-loops on it, and from `main`, set it to `false` after a short delay — note (conceptually, since this can be JIT/hardware-dependent and may not always reproduce) why this *can* fail to terminate the loop.
- Fix it by adding `volatile` and confirm the loop reliably terminates.
- Demonstrate that `volatile` alone doesn't make `count++` atomic by racing multiple threads incrementing a `volatile int`, showing lost updates (similar to the [[02-Synchronization]] exercise, but with `volatile` instead of `synchronized`).
- Compare a `volatile int` counter (broken for concurrent increments) against an `AtomicInteger` counter (correct) for the same workload.

## Interview Questions

**Q: What guarantee does `volatile` provide, and what does it explicitly NOT provide?**
A: It guarantees visibility — a write to a volatile field by one thread is immediately visible to any thread that subsequently reads it (no stale, thread-local cached values). It does **not** provide atomicity for compound operations — `volatileInt++` is still a non-atomic read-modify-write sequence, so `volatile` alone doesn't prevent race conditions on operations like incrementing.

**Q: When is `volatile` the right tool, versus needing `synchronized` or an `Atomic*` class?**
A: `volatile` is right for simple state where one thread writes and others only read the current value (e.g. a boolean "shutdown" flag, or a reference being swapped atomically as a whole). `synchronized` or `Atomic*` classes are needed whenever the operation is a compound read-modify-write (like incrementing a counter) or spans multiple related fields that must stay consistent together.

**Q: Why might a loop like `while (running) { }` never terminate if `running` isn't `volatile`, even after another thread sets it to `false`?**
A: Without `volatile`, there's no guarantee the writing thread's update to `running` is flushed to main memory in a timely way, or that the reading thread re-reads from main memory instead of a cached/register value — the JIT compiler is also free to hoist the read outside the loop entirely since, from its perspective, nothing inside the loop could change `running`. `volatile` disables these optimizations for that field and forces genuine cross-thread visibility.

**Q: Does `volatile` establish any ordering guarantees beyond the single field itself?**
A: Yes — it participates in the Java Memory Model's happens-before relationship: all writes made by a thread *before* it writes to a volatile field are guaranteed visible to another thread *after* that thread reads the same volatile field (not just the volatile field's own value, but everything the writing thread did beforehand). This is why the double-checked locking singleton pattern requires the instance reference to be `volatile`.

**Q: Is `AtomicInteger` just a `volatile int` with extra methods?**
A: No — `AtomicInteger` uses low-level CPU compare-and-swap (CAS) instructions internally to make compound operations like `incrementAndGet()` genuinely atomic (a true single indivisible read-modify-write), while also providing the same visibility guarantee as `volatile` for its underlying value. A `volatile int` alone gives you visibility but still lets `count++` race.

**Q: Can `volatile` be applied to any field type?**
A: Yes, including object references, though marking a reference `volatile` only makes the *reference itself* (which object it points to) visible/atomic when reassigned — it says nothing about the visibility of changes made to the mutable fields *inside* the object being pointed to. For full thread-safety of a mutable object's internals, you still need proper synchronization or immutability.
