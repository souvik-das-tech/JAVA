# Multithreaded Mini-Project

A capstone tying together [[../06-Multithreading/05-Executor-Framework]], [[../06-Multithreading/06-Callable-and-Future]], and [[../06-Multithreading/07-Concurrent-Collections]] into one small but realistic concurrent program.

## What to build

A **parallel word-frequency counter**: given a batch of "documents" (strings — could be lines of a real file), split the work across a thread pool so multiple documents are processed concurrently, and safely aggregate word counts from all threads into one shared result.

## Suggested structure

- An `ExecutorService` (fixed thread pool) that each document's word-counting work is `submit()`ted to as a `Callable<Void>` (or `Runnable`) — see [[../06-Multithreading/06-Callable-and-Future]].
- A shared `ConcurrentHashMap<String, Integer>` that every task safely merges its word counts into via `map.merge(word, 1, Integer::sum)` — see [[../06-Multithreading/07-Concurrent-Collections]] for why a plain `HashMap` would be unsafe here.
- Waiting for all submitted tasks to finish (`invokeAll`, or collecting `Future`s and calling `.get()` on each) before reading the final aggregated result — reading it too early would race with still-running tasks.

## What this project should exercise

- Splitting independent units of work (one per document) across a thread pool instead of one thread per document (unbounded thread creation) or a single thread (no concurrency benefit).
- Using a concurrent collection instead of manually synchronizing a `HashMap`, and understanding *why* that's necessary here (many threads writing to the same map concurrently).
- Correctly waiting for all background work to complete before using its result — a classic concurrency bug is reading a result too early.
- (Optional extension) comparing wall-clock time for a sequential loop vs. the thread-pool version, on a large enough workload to actually see a difference.

## Practice Questions / Exercises

- Implement the parallel word counter as described, and verify its output word-frequency map exactly matches a simple sequential (single-threaded) version run on the same input.
- Swap the shared map from `ConcurrentHashMap` to a plain `HashMap` and (if you can reproduce it) observe incorrect/inconsistent results or an exception under concurrent access.
- Extend it to also track, per word, how many *distinct documents* it appeared in (not just total occurrences) — this requires a slightly more careful concurrent data structure/approach.
- Time a large workload sequentially vs. via the thread pool and note the speedup (or lack of one, if the workload is too small to overcome thread-pool overhead).

## Interview Questions

**Q: Why use a thread pool (`ExecutorService`) here instead of creating one `Thread` per document?**
A: Creating a new OS thread per document doesn't scale if there are many documents — thread creation is relatively expensive and unbounded thread creation risks exhausting system resources. A fixed thread pool reuses a bounded set of threads across all the work, submitting tasks that queue up when all threads are busy — see [[../06-Multithreading/05-Executor-Framework]].

**Q: Why must the shared word-count map be a `ConcurrentHashMap` rather than a plain `HashMap` here?**
A: Multiple threads are calling `merge()` on the same map concurrently, from different documents. A plain `HashMap` isn't thread-safe under concurrent modification — it can corrupt its internal bucket structure or produce lost updates. `ConcurrentHashMap` provides safe, atomic per-key compound operations (like `merge`) specifically designed for exactly this kind of concurrent aggregation.

**Q: How do you know it's safe to read the final aggregated word-count map, and not accidentally read it while some tasks are still running?**
A: By explicitly waiting for all submitted tasks to complete before reading the result — either via `executor.invokeAll(tasks)` (which blocks until all given tasks finish) or by collecting each task's `Future` and calling `.get()` on all of them before proceeding. Reading the map before this wait would risk seeing a partial, still-changing result.

**Q: What would happen if you used `map.put(word, count)` instead of `map.merge(word, 1, Integer::sum)` from multiple threads?**
A: `put` would simply overwrite whatever value is currently there rather than atomically combining with it — two threads both incrementing the same word's count could both read the same old value and then both write back the same incremented value, silently losing one thread's increment (a classic lost-update race), even on a `ConcurrentHashMap`, since `put` alone isn't a compound read-modify-write operation the way `merge` is.

**Q: When would parallelizing this workload actually *not* pay off?**
A: If the documents are small/few, or the per-document processing is trivially fast, the overhead of submitting tasks to a thread pool and coordinating results can outweigh any parallel speedup — a sequential loop might actually be faster. Parallelism generally pays off once there's enough CPU-bound work per unit to amortize the coordination overhead, and enough independent units to actually keep multiple threads busy.
