# `wait()`, `notify()`, `notifyAll()`

Low-level coordination primitives on `Object` (every object has them) for threads to communicate about shared state changes — the classic building block behind producer-consumer patterns.

## The rules

- Must be called from inside a `synchronized` block/method on the **same object** you're calling them on, or it throws `IllegalMonitorStateException` — you must hold the lock to call `wait`/`notify` on it.
- `wait()` — releases the object's lock and pauses the current thread until another thread calls `notify()`/`notifyAll()` on the same object (or the wait times out, if a timeout was given). Crucially, `wait()` is the *only* thing that releases the lock while paused — `Thread.sleep()` does not.
- `notify()` — wakes up **one** arbitrary waiting thread (no guarantee which). `notifyAll()` — wakes up **all** waiting threads; they then compete to reacquire the lock and re-check their condition.

## Classic pattern: producer-consumer

```java
class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    synchronized void produce(int value) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();               // full — release lock, wait to be notified
        }
        queue.add(value);
        notifyAll();              // wake up any waiting consumer
    }

    synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();               // empty — release lock, wait to be notified
        }
        int value = queue.poll();
        notifyAll();              // wake up any waiting producer
        return value;
    }
}
```

- **Always call `wait()` in a `while` loop, never a plain `if`.** Between being notified and actually reacquiring the lock, another thread could have changed the condition again (e.g. another consumer already drained the queue) — a `while` re-checks the condition after waking up; an `if` would proceed blindly on a possibly-stale assumption. This is called a "spurious wakeup" guard, though the risk isn't only spurious wakeups — it's genuine races with other threads too.

## Why `notifyAll()` over `notify()` (usually)

`notify()` wakes exactly one thread, but you can't control *which* — if it happens to wake a thread whose condition still isn't satisfiable, that thread just goes back to waiting, and other threads that *could* have proceeded stay asleep. `notifyAll()` is safer by default (all waiting threads get to re-check their condition); `notify()` is only safe as an optimization when you know all waiting threads are interchangeable and only one can proceed anyway.

## Practice Questions / Exercises

- Implement a bounded `Buffer` (as above) with `produce`/`consume`, and run one producer thread and one consumer thread concurrently, printing what's produced/consumed.
- Change `wait()`'s surrounding `if` to a `while` (or vice versa) and reason about (or try to construct) a scenario where the `if` version misbehaves with multiple consumers.
- Call `wait()` outside of a `synchronized` block and observe the `IllegalMonitorStateException`.
- Run two producers and two consumers against one `Buffer`, using `notifyAll()`, and confirm no items are lost or duplicated.

## Interview Questions

**Q: Why must `wait()`/`notify()` be called from within a `synchronized` block on the same object?**
A: Because they operate on that object's monitor (lock) and wait-set — the JVM needs the calling thread to already hold the lock in order to safely and atomically release it (for `wait()`) or signal waiting threads (for `notify()`/`notifyAll()`). Calling them without holding the lock throws `IllegalMonitorStateException`.

**Q: What's the key difference between `Thread.sleep()` and `Object.wait()`?**
A: `sleep()` pauses the current thread for a fixed duration *without releasing any locks* it holds. `wait()` releases the lock on the object it's called on while paused, allowing other threads to acquire that lock and make progress (typically to change the condition the waiting thread is waiting for), and only reacquires the lock once notified (or timed out) and it's the thread's turn again.

**Q: Why should `wait()` always be called inside a `while` loop checking the condition, rather than a single `if`?**
A: Because after a thread is notified and wakes up, it must reacquire the lock before proceeding — during that gap, another thread might have already changed the shared state again (e.g. another consumer drained the queue first), or the JVM might issue a "spurious wakeup" (waking a thread with no corresponding notify at all, which the JLS explicitly permits). A `while` loop re-verifies the actual condition after waking, whereas an `if` would proceed based on a potentially stale assumption.

**Q: What's the difference between `notify()` and `notifyAll()`, and which is generally safer to use?**
A: `notify()` wakes exactly one arbitrary waiting thread; `notifyAll()` wakes all of them, letting each re-check its own condition (in a `while` loop) and only proceed if it's actually satisfied. `notifyAll()` is generally safer by default, since `notify()` can wake the "wrong" thread (one whose condition still isn't met) while leaving other threads that could have proceeded still asleep — `notify()` is only safe when you're certain any waiting thread is interchangeable.

**Q: What is a "spurious wakeup"?**
A: A thread waking up from `wait()` without actually having been `notify()`-ed or timed out — the Java Language Spec explicitly permits the JVM to do this (a consequence of how wait/notify maps to underlying OS threading primitives). It's another reason `wait()` must always be guarded by a `while` loop re-checking the actual condition, rather than assuming a wakeup means the condition is now true.

**Q: How do `wait`/`notify` relate to higher-level concurrency utilities like `BlockingQueue`?**
A: `BlockingQueue` implementations (e.g. `ArrayBlockingQueue`, `LinkedBlockingQueue`) solve the exact producer-consumer problem `wait`/`notify` are classically used for, but internally (often using `Lock`/`Condition`, a more flexible cousin of `wait`/`notify`) — in real code, using `BlockingQueue` directly is almost always preferable to hand-rolling `wait`/`notify` logic, which is easy to get subtly wrong (missed notifications, incorrect condition checks).
