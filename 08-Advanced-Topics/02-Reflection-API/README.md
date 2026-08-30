# Reflection API

Reflection lets code inspect and manipulate classes, fields, methods, and constructors **at runtime**, even ones it didn't know about at compile time — the mechanism behind frameworks (Spring, JUnit, Jackson) that need to work generically with arbitrary user classes.

## Getting a `Class` object

```java
Class<?> c1 = String.class;                    // compile-time known type
Class<?> c2 = someObject.getClass();            // from an instance
Class<?> c3 = Class.forName("java.lang.String"); // by fully-qualified name (throws ClassNotFoundException)
```

## Inspecting a class

```java
Class<?> clazz = Calculator.class;

clazz.getName();                 // fully-qualified name
clazz.getSuperclass();
clazz.getInterfaces();

Method[] methods = clazz.getDeclaredMethods();   // ALL methods (any access level), declared directly on this class
Field[] fields = clazz.getDeclaredFields();
Constructor<?>[] ctors = clazz.getDeclaredConstructors();
```

- `getMethods()`/`getFields()` — only **public** members, including inherited ones.
- `getDeclaredMethods()`/`getDeclaredFields()` — **all** members (any access level) declared directly on this class, but **not** inherited ones.

## Creating instances and invoking members dynamically

```java
Constructor<?> ctor = clazz.getDeclaredConstructor();
Object instance = ctor.newInstance();

Method method = clazz.getDeclaredMethod("add", int.class, int.class);
method.setAccessible(true);            // bypass access checks — needed for private members
Object result = method.invoke(instance, 3, 4);   // like calling instance.add(3, 4), but dynamically

Field field = clazz.getDeclaredField("total");
field.setAccessible(true);
field.set(instance, 100);              // like instance.total = 100, but dynamically
Object value = field.get(instance);
```

- `setAccessible(true)` bypasses Java's normal access control (`private`, `protected`) — powerful but breaks encapsulation; frameworks use it (e.g. dependency injection into `private` fields), but it should never be reached for in application-level business code.

## Performance & trade-offs

- Reflective calls are noticeably slower than direct calls (no compile-time optimization, extra access checks, boxing for primitive arguments) — fine for framework startup/configuration code, generally avoided in hot paths.
- Loses compile-time type safety — typos in method/field names surface only at runtime (`NoSuchMethodException`, `NoSuchFieldException`), not at compile time.

## Practice Questions / Exercises

- Get a `Class<?>` object for a custom class three different ways (`.class`, `getClass()`, `Class.forName(...)`), and print its name from each.
- Use `getDeclaredFields()`/`getDeclaredMethods()` to print all field and method names of a class, including private ones.
- Use reflection to create an instance of a class via its no-arg constructor, then invoke a method on it dynamically with `Method.invoke()`.
- Use `setAccessible(true)` to read and modify a `private` field of an object from outside its class, printing the value before and after.

## Interview Questions

**Q: What is reflection, and what's a real-world use case for it?**
A: Reflection is the ability to inspect and manipulate classes, methods, fields, and constructors at runtime, even for types not known at compile time. A classic use case: a dependency injection framework (like Spring) scans classes for `@Autowired`-annotated fields and uses reflection to set their values, without the framework's own code ever having compile-time knowledge of your specific classes.

**Q: What's the difference between `getMethods()` and `getDeclaredMethods()`?**
A: `getMethods()` returns only `public` methods, but includes ones inherited from superclasses/interfaces. `getDeclaredMethods()` returns methods of *any* access level (`private`, `protected`, package-private, `public`) but only those declared directly on that class — not inherited ones. The same distinction applies to `getFields()`/`getDeclaredFields()` and constructors.

**Q: What does `setAccessible(true)` do, and why is it risky?**
A: It tells the JVM to bypass the normal Java access-control checks (`private`/`protected`) for that specific reflected member, letting you read/write a private field or invoke a private method from outside its class. It's risky because it deliberately breaks encapsulation — code relying on it can be broken by internal refactors of a class that were never meant to be a public contract, and it can also be blocked by the Java Platform Module System's strong encapsulation in modularized code.

**Q: Why is reflection generally slower than direct method calls, and when does that matter?**
A: Reflective invocation involves extra work the JIT can't optimize away as easily as a direct call — access checks (unless bypassed), boxing/unboxing of primitive arguments (since `invoke` takes `Object...`), and indirection through the reflection API's internal dispatch. It matters in hot, performance-sensitive loops; it's generally a non-issue for one-time startup/configuration code (e.g. scanning annotations once when an application boots).

**Q: How would a framework create an instance of a class it only knows about as a `Class<?>` object (e.g. loaded from a config file by class name)?**
A: `Class.forName("com.example.MyClass")` loads the class by name, then `clazz.getDeclaredConstructor().newInstance()` invokes its constructor reflectively to produce an instance — this is exactly how many plugin systems, JDBC driver loading, and dependency injection containers instantiate classes they only know about as configuration strings, not compile-time types.

**Q: Does reflection let you bypass Java's type system entirely — e.g. call `List<String>`'s `add()` with an `Integer`?**
A: At the reflection-API level, yes — since generics are erased at compile time (see [[../05-Generics/01-Generic-Classes-and-Methods]]), a reflective `Method.invoke(list, someInteger)` on `add` sees only `Object` and doesn't perform the generic type check a normal call would have (the compiler inserted that check, not the runtime). This is a real, if rarely hit, way to corrupt a supposedly type-safe collection.
