# Executor Framework (`ExecutorService`, Thread Pools)

Manually creating a `new Thread()` per task doesn't scale — thread creation is expensive, and unbounded thread creation can exhaust system resources. The Executor Framework separates **task submission** from **thread management**, using a managed pool of reusable threads.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

for (int i = 0; i < 10; i++) {
    int taskId = i;
    executor.submit(() -> {
        System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
    });
}

executor.shutdown();   // stop accepting new tasks; let submitted ones finish
```

- Threads in the pool are **reused** across tasks — no per-task thread creation/teardown cost.
- `submit(Runnable)` / `submit(Callable<T>)` — queues the task; it runs on whichever pool thread becomes free.

## Common factory methods (`Executors`)

| Factory | Behavior |
|---|---|
| `newFixedThreadPool(n)` | Exactly `n` threads; extra tasks queue up until a thread frees |
| `newCachedThreadPool()` | Grows/shrinks as needed; reuses idle threads, creates new ones if all busy — good for many short-lived tasks |
| `newSingleThreadExecutor()` | One thread; tasks run strictly sequentially, in submission order |
| `newScheduledThreadPool(n)` | Supports delayed/periodic task execution (`schedule`, `scheduleAtFixedRate`) |

## Shutdown

- `shutdown()` — graceful: stops accepting new tasks, lets already-submitted tasks finish.
- `shutdownNow()` — attempts to stop all actively executing tasks (via interruption) and returns tasks that were queued but never started.
- `awaitTermination(timeout, unit)` — blocks until all tasks finish or the timeout elapses; typically called right after `shutdown()` to wait for completion.
- Forgetting to `shutdown()` an `ExecutorService` leaks its threads — a JVM with only non-daemon pool threads still running will **never exit**.

## Practice Questions / Exercises

- Create a fixed thread pool of 3 and submit 10 `Runnable` tasks that print their task number and thread name — observe how tasks queue and reuse threads.
- Compare `newFixedThreadPool(2)` against `newCachedThreadPool()` for the same 10 short tasks — note the difference in how many distinct thread names appear.
- Call `shutdown()`, then `awaitTermination()` with a timeout, and print whether all tasks completed in time.
- Try submitting a task *after* calling `shutdown()` and observe the `RejectedExecutionException`.

## Interview Questions

**Q: Why use the Executor Framework instead of creating `new Thread()` for every task?**
A: Thread creation/destruction is relatively expensive (OS-level resource allocation), and unbounded thread creation for every task can exhaust memory/CPU under load. The Executor Framework reuses a managed pool of threads across many tasks, decouples "submitting work" from "how it's executed," and provides lifecycle management (`shutdown`, `awaitTermination`) that raw `Thread` usage doesn't.

**Q: What's the difference between `newFixedThreadPool` and `newCachedThreadPool`?**
A: `newFixedThreadPool(n)` maintains exactly `n` threads always; if more tasks arrive than threads available, extras queue up and wait. `newCachedThreadPool()` has no fixed size — it creates new threads on demand when none are idle and reuses/reaps idle ones after a timeout, which suits many short-lived tasks but risks unbounded thread growth under sustained heavy load.

**Q: What's the difference between `shutdown()` and `shutdownNow()`?**
A: `shutdown()` is graceful — it stops accepting new tasks but lets already-submitted (including queued) tasks run to completion. `shutdownNow()` attempts to stop everything immediately — it interrupts actively running tasks (interruption is cooperative, so a task must check/handle it to actually stop) and returns the list of tasks that were queued but never started.

**Q: What happens if you submit a task to an `ExecutorService` after calling `shutdown()`?**
A: It throws `RejectedExecutionException` — once shutdown has been initiated, the executor refuses any new task submissions, even though already-queued/running tasks continue until completion.

**Q: Why is it a problem to forget calling `shutdown()` on an `ExecutorService`?**
A: The pool's threads are typically non-daemon by default, so as long as they're alive (even idle, waiting for more tasks), the JVM will not exit — a forgotten `shutdown()` can leave an application hanging indefinitely after its actual work is done, since the executor's threads keep the process alive.

**Q: How would you wait for all submitted tasks to finish before continuing, after calling `shutdown()`?**
A: Call `executor.awaitTermination(timeout, timeUnit)` right after `shutdown()` — it blocks the calling thread until either all tasks complete or the given timeout elapses (returning a boolean indicating which happened), letting you know whether it's safe to assume everything finished.
