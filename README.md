# Java Roadmap (Basic → Advanced)

Progress tracker for Core Java. Mark a topic done by changing `- [ ]` to `- [x]` and commit/push — GitHub will render it as a checked box.

**Progress: 66 / 66 topics done**

---

## 1. Basics

- [x] JDK/JRE/JVM, installing Java, setting up IDE
- [x] First program, `main` method, compiling & running
- [x] Variables & data types (primitive vs reference)
- [x] Type casting & type conversion
- [x] Operators (arithmetic, relational, logical, bitwise, assignment, ternary)
- [x] Conditional statements (`if`, `else if`, `switch`)
- [x] Loops (`for`, `while`, `do-while`, enhanced for)
- [x] Arrays (1D, 2D, multi-dimensional)
- [x] Input handling (`Scanner`, `BufferedReader`)
- [x] Methods (declaration, parameters, return types, overloading)
- [x] Variable scope & the `static` keyword

## 2. Object-Oriented Programming

- [x] Classes & objects
- [x] Constructors (default, parameterized, constructor chaining, `this`)
- [x] Encapsulation (getters/setters, access modifiers)
- [x] Inheritance (`extends`, `super`)
- [x] Polymorphism — method overloading vs overriding
- [x] Abstraction — abstract classes vs interfaces
- [x] Interfaces (default & static methods, functional interfaces)
- [x] `final` keyword (variables, methods, classes)
- [x] Packages & access modifiers (`public`, `private`, `protected`, default)
- [x] `equals()`, `hashCode()`, `toString()` overriding
- [x] Nested, inner, static nested & anonymous classes
- [x] Enums

## 3. Core APIs

- [x] String, StringBuilder, StringBuffer (immutability, string pool)
- [x] Wrapper classes & autoboxing/unboxing
- [x] Exception handling (`try/catch/finally`, checked vs unchecked)
- [x] Custom exceptions
- [x] `try-with-resources`
- [x] File I/O (`File`, `FileReader/Writer`, `BufferedReader/Writer`)
- [x] Serialization & deserialization

## 4. Collections Framework

- [x] `Collection` hierarchy overview
- [x] `List` — `ArrayList`, `LinkedList`
- [x] `Set` — `HashSet`, `LinkedHashSet`, `TreeSet`
- [x] `Map` — `HashMap`, `LinkedHashMap`, `TreeMap`
- [x] `Queue`/`Deque` — `PriorityQueue`, `ArrayDeque`
- [x] `Iterator` & `ListIterator`
- [x] `Comparable` vs `Comparator`
- [x] `Collections` utility class (sort, reverse, synchronizedList, etc.)

## 5. Generics

- [x] Generic classes & methods
- [x] Bounded type parameters
- [x] Wildcards (`?`, `? extends`, `? super`)

## 6. Multithreading & Concurrency

- [x] Thread lifecycle, creating threads (`Thread` vs `Runnable`)
- [x] Synchronization (`synchronized`, locks)
- [x] `wait()`, `notify()`, `notifyAll()`
- [x] `volatile` keyword
- [x] Executor framework (`ExecutorService`, thread pools)
- [x] `Callable` & `Future`
- [x] Concurrent collections (`ConcurrentHashMap`, `CopyOnWriteArrayList`)

## 7. Java 8+ Features

- [x] Lambda expressions
- [x] Functional interfaces (`Function`, `Predicate`, `Supplier`, `Consumer`)
- [x] Streams API (map, filter, reduce, collect)
- [x] Method references
- [x] `Optional`
- [x] Date & Time API (`LocalDate`, `LocalDateTime`, `Duration`)

## 8. Advanced Topics

- [x] Annotations (built-in & custom)
- [x] Reflection API
- [x] Java Memory Model basics & Garbage Collection
- [x] Design patterns (Singleton, Factory, Builder, Observer)
- [x] JDBC — connecting to a database, CRUD operations
- [x] Basics of Maven/Gradle
- [x] Unit testing with JUnit

## 9. Practice / Mini Projects

- [x] Console-based CRUD app
- [x] File-based data storage mini-project
- [x] Multithreaded mini-project
- [x] JDBC-based project (connect to MySQL/Postgres)
- [x] REST-ish project practice (optional, pre-Spring warm-up)

---

### How I update this

1. Finish a topic → write/practice code for it in this repo.
2. Open this file, change `- [ ]` to `- [x]` for that topic.
3. Update the **Progress** count at the top.
4. Commit with a message like `docs: mark <topic> as done` and push.
