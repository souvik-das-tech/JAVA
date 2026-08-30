# Java Memory Model Basics & Garbage Collection

## Runtime memory areas

| Area | Holds | Notes |
|---|---|---|
| **Heap** | All objects (`new`-allocated) | Shared across all threads; garbage-collected |
| **Stack** | Method call frames — local variables, primitives, references | One stack per thread; automatically freed when a method returns |
| **Method area / Metaspace** | Class metadata, static fields, method bytecode | Shared across threads |

- A local primitive (`int x = 5;`) lives directly on the stack. A local **reference** variable also lives on the stack, but the **object** it points to lives on the heap — this is why passing an object reference into a method lets that method mutate the shared object, while passing a primitive only ever copies the value.

## Garbage collection basics

Java automatically reclaims heap memory for objects that are no longer **reachable** from any live reference (a "GC root" — local variables on any thread's stack, static fields, etc.) — no manual `free()`/`delete` like C/C++.

- **Reachability**, not reference counting, determines eligibility — even two objects referencing only *each other* (a cycle) are still collected if nothing reachable from a GC root points to either of them.
- The generational hypothesis ("most objects die young") underlies the standard design: the heap is split into a small **Young Generation** (further split into Eden + Survivor spaces, collected frequently and cheaply — "Minor GC") and a larger **Old Generation** (collected less often, more expensively — "Major/Full GC") for long-lived objects that survive several young-gen collections and get **promoted**.
- Modern collectors (G1, ZGC, Shenandoah) aim to minimize GC pause times, especially important for latency-sensitive applications.

## `finalize()` — deprecated, avoid

`Object.finalize()` was a hook to run cleanup before an object was collected — **deprecated since Java 9** because its timing is unpredictable (may never run promptly, or at all), it can resurrect objects, and it hurts GC performance. Use `try-with-resources`/`AutoCloseable` (see [[../03-Core-APIs/05-try-with-resources]]) instead for deterministic cleanup.

## Common causes of memory leaks in Java (despite GC)

Even with automatic GC, memory leaks happen when objects are **unintentionally kept reachable** — e.g. a growing `static` collection nobody ever removes entries from, listeners registered but never unregistered, or an inner class holding an implicit outer-instance reference (see [[../02-OOP/11-Nested-Inner-and-Anonymous-Classes]]) that outlives its intended scope.

## Practice Questions / Exercises

- Write code that creates many short-lived objects in a loop (e.g. millions of small objects) and observe (via `-Xlog:gc` JVM flag, if you want to go deeper) that most are collected quickly from the young generation.
- Explain (in your own words, in this README or out loud) why setting a reference to `null` can sometimes help the GC, and why it's not something you need to do everywhere defensively.
- Construct a small example of an accidental memory leak: a `static List` that keeps growing across method calls with nothing ever removing old entries.
- Look up (conceptually) the difference between a Minor GC and a Full GC pause, and why Full GCs are more disruptive to a running application.

## Interview Questions

**Q: What's the difference between the heap and the stack in the JVM's memory model?**
A: The heap holds all objects (created via `new`) and is shared across all threads, managed by the garbage collector. The stack holds method call frames — local variables, primitives, and object references (not the objects themselves) — with one stack per thread, automatically popped when each method call returns; no GC involvement needed there.

**Q: How does the JVM decide an object is eligible for garbage collection?**
A: Based on reachability from a set of "GC roots" (active thread stacks' local variables, static fields, JNI references, etc.) — an object is eligible once no chain of references from any GC root reaches it anymore. This correctly handles reference cycles (two objects referencing only each other are still collected if nothing else points to either), unlike simple reference counting.

**Q: What is the generational hypothesis, and how does it shape the heap's structure?**
A: The observation that most objects die young (are short-lived) while a small fraction live much longer. This motivates splitting the heap into a Young Generation (frequently, cheaply collected) and an Old Generation (rarely, more expensively collected) — objects start in Young and get promoted to Old only after surviving several collections, so the common case (short-lived garbage) is handled efficiently without repeatedly scanning long-lived objects.

**Q: Why was `Object.finalize()` deprecated, and what's the recommended replacement?**
A: Its execution timing is entirely unpredictable (the GC decides if/when to call it, possibly never before JVM shutdown), it can be used to "resurrect" an object by re-establishing a reference to it mid-finalization (a correctness hazard), and finalizable objects are more expensive for the GC to manage. `try-with-resources` with `AutoCloseable` gives deterministic, immediate cleanup instead, and is the recommended replacement for resource cleanup.

**Q: Can a Java program still have memory leaks despite automatic garbage collection?**
A: Yes — a "leak" in Java means objects staying unintentionally *reachable* longer than intended (not literally unreachable-but-unfreed memory like in C). Common causes: an ever-growing static collection nothing removes from, registered listeners/callbacks never unregistered, or inner class instances (holding an implicit outer-instance reference) outliving their intended scope and keeping the outer object alive too.

**Q: What's the practical difference between a Minor GC and a Full GC, from an application's perspective?**
A: A Minor GC collects only the Young Generation, is fast, and typically causes a very brief pause. A Full GC collects the entire heap (Young + Old, sometimes Metaspace too), takes significantly longer, and can cause a noticeably longer application pause ("stop-the-world") — frequent Full GCs are usually a sign of a memory-pressure or leak problem worth investigating, since they indicate the Old Generation is filling up faster than expected.
