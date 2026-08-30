# Java Roadmap (Basic → Advanced)

Progress tracker for Core Java. Mark a topic done by changing `- [ ]` to `- [x]` and commit/push — GitHub will render it as a checked box.

**Progress: 11 / 70 topics done**

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

- [ ] Classes & objects
- [ ] Constructors (default, parameterized, constructor chaining, `this`)
- [ ] Encapsulation (getters/setters, access modifiers)
- [ ] Inheritance (`extends`, `super`)
- [ ] Polymorphism — method overloading vs overriding
- [ ] Abstraction — abstract classes vs interfaces
- [ ] Interfaces (default & static methods, functional interfaces)
- [ ] `final` keyword (variables, methods, classes)
- [ ] Packages & access modifiers (`public`, `private`, `protected`, default)
- [ ] `equals()`, `hashCode()`, `toString()` overriding
- [ ] Nested, inner, static nested & anonymous classes
- [ ] Enums

## 3. Core APIs

- [ ] String, StringBuilder, StringBuffer (immutability, string pool)
- [ ] Wrapper classes & autoboxing/unboxing
- [ ] Exception handling (`try/catch/finally`, checked vs unchecked)
- [ ] Custom exceptions
- [ ] `try-with-resources`
- [ ] File I/O (`File`, `FileReader/Writer`, `BufferedReader/Writer`)
- [ ] Serialization & deserialization

## 4. Collections Framework

- [ ] `Collection` hierarchy overview
- [ ] `List` — `ArrayList`, `LinkedList`
- [ ] `Set` — `HashSet`, `LinkedHashSet`, `TreeSet`
- [ ] `Map` — `HashMap`, `LinkedHashMap`, `TreeMap`
- [ ] `Queue`/`Deque` — `PriorityQueue`, `ArrayDeque`
- [ ] `Iterator` & `ListIterator`
- [ ] `Comparable` vs `Comparator`
- [ ] `Collections` utility class (sort, reverse, synchronizedList, etc.)

## 5. Generics

- [ ] Generic classes & methods
- [ ] Bounded type parameters
- [ ] Wildcards (`?`, `? extends`, `? super`)

## 6. Multithreading & Concurrency

- [ ] Thread lifecycle, creating threads (`Thread` vs `Runnable`)
- [ ] Synchronization (`synchronized`, locks)
- [ ] `wait()`, `notify()`, `notifyAll()`
- [ ] `volatile` keyword
- [ ] Executor framework (`ExecutorService`, thread pools)
- [ ] `Callable` & `Future`
- [ ] Concurrent collections (`ConcurrentHashMap`, `CopyOnWriteArrayList`)

## 7. Java 8+ Features

- [ ] Lambda expressions
- [ ] Functional interfaces (`Function`, `Predicate`, `Supplier`, `Consumer`)
- [ ] Streams API (map, filter, reduce, collect)
- [ ] Method references
- [ ] `Optional`
- [ ] Date & Time API (`LocalDate`, `LocalDateTime`, `Duration`)

## 8. Advanced Topics

- [ ] Annotations (built-in & custom)
- [ ] Reflection API
- [ ] Java Memory Model basics & Garbage Collection
- [ ] Design patterns (Singleton, Factory, Builder, Observer)
- [ ] JDBC — connecting to a database, CRUD operations
- [ ] Basics of Maven/Gradle
- [ ] Unit testing with JUnit

## 9. Practice / Mini Projects

- [ ] Console-based CRUD app
- [ ] File-based data storage mini-project
- [ ] Multithreaded mini-project
- [ ] JDBC-based project (connect to MySQL/Postgres)
- [ ] REST-ish project practice (optional, pre-Spring warm-up)

---

### How I update this

1. Finish a topic → write/practice code for it in this repo.
2. Open this file, change `- [ ]` to `- [x]` for that topic.
3. Update the **Progress** count at the top.
4. Commit with a message like `docs: mark <topic> as done` and push.
