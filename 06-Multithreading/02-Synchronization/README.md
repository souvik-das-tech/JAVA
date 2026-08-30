# Synchronization (`synchronized`, Locks)

When multiple threads read/write shared mutable state without coordination, you get a **race condition** — the outcome depends unpredictably on thread scheduling.

## The problem

```java
class Counter {
    int count = 0;
    void increment() { count++; }   // NOT atomic! read-modify-write in 3 steps
}
```

`count++` is actually three separate operations (read, add 1, write back). Two threads can both read the same value before either writes back, and one increment gets lost.

## `synchronized`

Ensures only **one** thread at a time can execute a block/method guarded by the same lock (monitor). Every object has an intrinsic lock.

```java
class Counter {
    int count = 0;
    synchronized void increment() {   // locks `this` for the method's duration
        count++;
    }
}

// or a synchronized block, locking on any chosen object — allows finer-grained locking than a whole method:
void increment() {
    synchronized (this) {
        count++;
    }
}
```

- A `synchronized` instance method locks on `this`; a `synchronized static` method locks on the **Class object** (`Counter.class`), not any instance — so a static synchronized method and an instance synchronized method do **not** block each other.
- Only one thread can hold a given object's lock at a time; other threads calling any `synchronized` method/block on that same object **block** (wait) until the lock is released.

## `ReentrantLock` (`java.util.concurrent.locks`)

A more flexible, explicit alternative to `synchronized`.

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    count++;
} finally {
    lock.unlock();   // MUST be in finally, or an exception leaves the lock held forever
}
```

- Supports `tryLock()` (non-blocking attempt, optionally with a timeout), fairness policies, and `Condition` objects (a more flexible `wait`/`notify`) — capabilities `synchronized` doesn't offer.
- Unlike `synchronized`, the lock is **not** automatically released if an exception is thrown — you must always `unlock()` in a `finally` block.

## Practice Questions / Exercises

- Write a `Counter` with a non-synchronized `increment()`, run 1000 increments across 10 threads (100 each), and show the final count is unpredictably less than 1000 due to the race condition.
- Fix it by making `increment()` `synchronized`, rerun, and confirm the final count is always exactly 1000.
- Rewrite the fix using `ReentrantLock` instead of `synchronized`, with `lock()`/`unlock()` in a `try/finally`.
- Demonstrate that a `static synchronized` method and an instance `synchronized` method on the same class don't block each other (they lock on different objects).

## Interview Questions

**Q: Why is `count++` not thread-safe even though it looks like a single operation?**
A: It compiles to three separate steps — read the current value, add 1, write the new value back — none of which are atomic together. Two threads can interleave these steps (e.g. both read the same old value before either writes), causing one thread's increment to be silently lost — a classic race condition.

**Q: What does `synchronized` actually lock — the method, or something else?**
A: It locks on an object's intrinsic monitor lock — for an instance `synchronized` method, that's `this`; for a `static synchronized` method, it's the `Class` object itself; for a `synchronized(obj) { }` block, it's whatever object you explicitly specify. Only one thread can hold a given lock object at a time, so any code synchronized on the *same* lock object is mutually exclusive.

**Q: What's the difference between `synchronized` and `ReentrantLock`?**
A: `synchronized` is a simpler, built-in language construct that automatically releases the lock when the block/method exits (even via exception). `ReentrantLock` is an explicit API object requiring manual `lock()`/`unlock()` calls (unlock must be in `finally`, or a leaked exception leaves the lock held forever), but it adds capabilities `synchronized` lacks: `tryLock()` (non-blocking or timed lock attempts), configurable fairness, and multiple `Condition` objects per lock (for more granular wait/notify).

**Q: What does "reentrant" mean in the context of locks, and why does it matter?**
A: A reentrant lock allows the *same thread* that already holds the lock to acquire it again (e.g. calling another synchronized method on the same object from within a synchronized method) without deadlocking itself — the JVM tracks a hold count and only fully releases the lock once the thread has unlocked it the same number of times it locked it. Both intrinsic `synchronized` locks and `ReentrantLock` are reentrant.

**Q: If a thread throws an exception inside a `synchronized` block, is the lock released?**
A: Yes — `synchronized` guarantees the lock is released when the block exits, whether normally or via an exception (similar to how `finally` always runs). This is one advantage over `ReentrantLock`, where you must explicitly ensure `unlock()` runs in a `finally` block yourself, since it isn't automatic.

**Q: Does synchronizing a method make the whole class thread-safe?**
A: No — it only guarantees mutual exclusion for code paths that synchronize on the *same* lock. If some methods that touch the shared state are synchronized and others aren't (or they synchronize on different lock objects), the state is still vulnerable to race conditions through the unsynchronized/differently-locked paths.
