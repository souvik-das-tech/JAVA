# Thread Lifecycle, Creating Threads (`Thread` vs `Runnable`)

## Two ways to create a thread

```java
// 1. Extend Thread, override run()
class MyThread extends Thread {
    public void run() { System.out.println("Running in " + Thread.currentThread().getName()); }
}
new MyThread().start();

// 2. Implement Runnable, pass it to a Thread — PREFERRED
class MyTask implements Runnable {
    public void run() { System.out.println("Running in " + Thread.currentThread().getName()); }
}
new Thread(new MyTask()).start();

// or with a lambda, since Runnable is a functional interface:
new Thread(() -> System.out.println("Running")).start();
```

- **Prefer `Runnable`**: Java has single inheritance, so extending `Thread` uses up your one `extends` slot; `Runnable` keeps the task separate from "being a thread," so the same task logic could also run on an `ExecutorService` thread pool without any `Thread` subclassing at all.
- Calling `.run()` directly (instead of `.start()`) just executes the method like a normal method call, **on the current thread** — no new thread is created. `.start()` is what actually spawns a new OS-level thread and schedules `run()` to execute on it.
- A `Thread` can only be `.start()`ed **once** — calling `start()` again on an already-started thread throws `IllegalThreadStateException`.

## Thread lifecycle (states — `Thread.State`)

```
NEW → RUNNABLE → (BLOCKED / WAITING / TIMED_WAITING) → TERMINATED
```

- **NEW** — created (`new Thread(...)`) but `.start()` not yet called.
- **RUNNABLE** — eligible to run (actually running, or waiting for CPU time from the OS scheduler).
- **BLOCKED** — waiting to acquire a lock (e.g. entering a `synchronized` block held by another thread).
- **WAITING** / **TIMED_WAITING** — waiting indefinitely (`Object.wait()`, `Thread.join()`) or for a bounded time (`Thread.sleep(ms)`, `wait(timeout)`).
- **TERMINATED** — `run()` has completed (normally or via an uncaught exception).

## Practice Questions / Exercises

- Create a thread by extending `Thread`, and another by implementing `Runnable`, and start both — print `Thread.currentThread().getName()` from each to confirm they run on separate threads.
- Call `.run()` directly instead of `.start()` on a `Thread`/`Runnable`, and confirm (via `getName()`) it executed on the main thread, not a new one.
- Print a thread's state (`Thread.getState()`) at different points: right after creation, right after `start()`, and after it finishes (may need a short `Thread.sleep` in `main` to observe intermediate states reliably).
- Try calling `.start()` twice on the same `Thread` object and catch the `IllegalThreadStateException`.

## Interview Questions

**Q: What's the difference between extending `Thread` and implementing `Runnable`, and which is preferred?**
A: Extending `Thread` means your task class *is* a thread and uses up Java's single class-inheritance slot. Implementing `Runnable` separates "what to run" from "the thread that runs it" — the same `Runnable` can be handed to a plain `Thread`, an `ExecutorService`, or anything else that accepts one. `Runnable` is generally preferred for this flexibility, especially since modern code typically uses thread pools rather than raw `Thread` objects anyway.

**Q: What actually happens if you call `run()` directly instead of `start()`?**
A: `run()` is just a normal method — calling it directly executes its body synchronously on the *current* thread, exactly like any other method call, and does not create or schedule a new thread at all. Only `start()` asks the JVM/OS to spin up a new thread and have *that* thread execute `run()`.

**Q: Can you call `start()` twice on the same `Thread` instance?**
A: No — a `Thread` object can only transition out of the `NEW` state once. Calling `start()` a second time on an already-started (or terminated) thread throws `IllegalThreadStateException`.

**Q: What is the difference between the `BLOCKED` and `WAITING` thread states?**
A: `BLOCKED` specifically means the thread is waiting to acquire a monitor lock to enter a `synchronized` block/method that another thread currently holds. `WAITING`/`TIMED_WAITING` means the thread has voluntarily paused itself (e.g. via `Object.wait()`, `Thread.join()`, or `Thread.sleep()`) and is waiting for another thread's action or a timeout, not specifically for a lock.

**Q: Does calling `Thread.sleep()` release any locks the thread currently holds?**
A: No — `sleep()` merely pauses the current thread's execution for the given time; it does not release any `synchronized` locks it holds. This is a key difference from `Object.wait()`, which does release the lock on the object it's called on while waiting, allowing other threads to acquire it.

**Q: What happens to a thread if an uncaught exception is thrown inside its `run()` method?**
A: The thread terminates (moves to `TERMINATED`) and the exception is passed to the thread's `UncaughtExceptionHandler` (by default, printing a stack trace to `System.err`) — it does **not** propagate to the thread that called `start()`, since threads have independent call stacks; the rest of the program (other threads) continues running unaffected.
