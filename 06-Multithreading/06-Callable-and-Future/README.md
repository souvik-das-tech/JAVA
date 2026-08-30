# `Callable` & `Future`

`Runnable`'s `run()` returns nothing and can't throw a checked exception. `Callable<V>` fixes both: its single method `call()` **returns a value** and **can throw a checked exception**.

```java
Callable<Integer> task = () -> {
    Thread.sleep(100);
    return 42;
};
```

## Submitting a `Callable` — `Future<V>`

`ExecutorService.submit(Callable<V>)` returns a `Future<V>` — a handle representing the result of an **asynchronous** computation that may not have finished yet.

```java
ExecutorService executor = Executors.newFixedThreadPool(2);
Future<Integer> future = executor.submit(task);

// do other work while the task runs in the background...

Integer result = future.get();   // BLOCKS until the task completes, then returns its result
executor.shutdown();
```

- `future.get()` — blocks the calling thread until the result is available; re-throws the task's exception wrapped in `ExecutionException` if `call()` threw one.
- `future.get(timeout, unit)` — blocks with a timeout, throwing `TimeoutException` if it's not done in time.
- `future.isDone()` — non-blocking check for completion.
- `future.cancel(mayInterruptIfRunning)` — attempts to cancel the task; returns whether cancellation succeeded. `future.isCancelled()` checks afterward.

## Submitting a batch — `invokeAll`

```java
List<Callable<Integer>> tasks = List.of(task1, task2, task3);
List<Future<Integer>> results = executor.invokeAll(tasks);   // blocks until ALL complete
for (Future<Integer> f : results) {
    System.out.println(f.get());
}
```

## Practice Questions / Exercises

- Write a `Callable<Integer>` that sleeps briefly then returns a computed value, submit it to an `ExecutorService`, and call `future.get()` to retrieve the result.
- Write a `Callable` that throws a checked exception, submit it, and catch the `ExecutionException` from `future.get()`, printing the wrapped original cause via `getCause()`.
- Submit a batch of `Callable`s via `invokeAll()` and print all results after they complete.
- Submit a long-running task, call `future.cancel(true)` shortly after, and check `future.isCancelled()`.

## Interview Questions

**Q: What's the difference between `Runnable` and `Callable`?**
A: `Runnable.run()` returns `void` and cannot throw a checked exception. `Callable<V>.call()` returns a value of type `V` and is permitted to throw a checked exception — `Callable` is what you use when a background task needs to produce a result or might fail with a checked exception.

**Q: What is a `Future`, and what does `future.get()` do?**
A: `Future<V>` is a handle to the eventual result of an asynchronous computation submitted to an executor. `future.get()` blocks the calling thread until that computation finishes, then returns its result (or re-throws its exception, wrapped in `ExecutionException`, if the task failed).

**Q: If the `Callable` submitted for a `Future` throws an exception, what happens when you call `future.get()`?**
A: The original exception is caught internally and wrapped in an `ExecutionException`, which `future.get()` then throws — you retrieve the original failure via `executionException.getCause()`. This wrapping is why `get()` declares `throws ExecutionException, InterruptedException`.

**Q: How do you cancel a running task submitted as a `Future`, and what does the boolean argument to `cancel()` control?**
A: Call `future.cancel(mayInterruptIfRunning)`. If `true`, and the task is currently running, the executor attempts to interrupt the thread executing it (interruption is cooperative — the task's code must check/handle it to actually stop). If `false`, a currently-running task is left to finish; cancellation only prevents a task that hasn't started yet from starting.

**Q: What's the difference between `executor.submit()` and `executor.invokeAll()`?**
A: `submit()` submits a single task and immediately returns its `Future`, without blocking. `invokeAll(Collection<Callable<T>>)` submits a whole batch of tasks at once and **blocks** until all of them complete, returning a `List<Future<T>>` for their results in the same order the tasks were given.

**Q: Is `Future.get()` the only way to know when a task completes, or can you avoid blocking?**
A: `Future` on its own is blocking-only for retrieving the result (aside from polling `isDone()` in a loop, which is inefficient). `CompletableFuture` (Java 8+) extends this with non-blocking callback-style composition (`thenApply`, `thenAccept`, `thenCombine`, etc.), letting you react to completion without ever calling a blocking `get()`.
