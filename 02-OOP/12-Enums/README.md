# Enums

An `enum` is a special class representing a fixed set of named constants. Each constant is actually a `public static final` instance of the enum type itself.

```java
enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

Day d = Day.MONDAY;
```

- Type-safe: a variable of type `Day` can only ever hold one of the declared constants — unlike using `int`/`String` constants, the compiler rejects invalid values.
- Implicitly extends `java.lang.Enum` — so enums **cannot** extend any other class (single inheritance already used), though they can implement interfaces.
- Comes with useful built-ins: `values()` (array of all constants, in declaration order), `ordinal()` (0-based position), `name()` (declared constant name as a `String`), `valueOf(String)` (parses a name back into the constant), and works directly in `switch` statements.

## Enums with fields, constructors, and methods

```java
enum Planet {
    MERCURY(3.3e23, 2.4e6),
    EARTH(5.9e24, 6.4e6);

    private final double mass, radius;

    Planet(double mass, double radius) {   // constructor — implicitly private, called once per constant
        this.mass = mass;
        this.radius = radius;
    }

    double surfaceGravity() {
        return 6.674e-11 * mass / (radius * radius);
    }
}
```

- Enum constructors are implicitly `private` — you can never call `new Planet(...)` yourself; constants are constructed exactly once, when the enum class is loaded.
- Each constant can even override a method with constant-specific behavior, using a body: `SATURDAY { boolean isWeekend() { return true; } }`.

## Practice Questions / Exercises

- Write a `Day` enum with all seven days, and use a `switch` on a `Day` value to print "Weekend" or "Weekday".
- Print `Day.values()` in a loop along with each constant's `ordinal()`.
- Extend `Planet` (as above) with a `surfaceGravity()` method, and print the gravity for each planet using `Planet.values()`.
- Use `Day.valueOf("FRIDAY")` and show what happens (which exception) if you pass a string that doesn't match any constant name.

## Interview Questions

**Q: What is an enum, under the hood?**
A: It's a special kind of class — the compiler generates a `final` class extending `java.lang.Enum`, where each declared constant is a `public static final` instance of that class, created exactly once when the enum is loaded. This is why enums support fields, constructors, and methods just like a regular class.

**Q: Why can't an enum extend another class?**
A: Because every enum implicitly extends `java.lang.Enum` already, and Java only supports single class inheritance — that inheritance slot is used up. An enum can still implement any number of interfaces, though.

**Q: Why are enum constructors implicitly `private`, and can you call `new MyEnum(...)` yourself?**
A: Enum constants are meant to be a fixed, closed set defined entirely within the enum declaration — allowing external code to construct new instances would break that guarantee. The constructor is implicitly `private` (declaring it any other way is a compile error) and can only be invoked by the compiler when initializing the declared constants.

**Q: What's the difference between `ordinal()` and using an enum constant's declared order for logic, and why is relying on `ordinal()` often discouraged?**
A: `ordinal()` returns the 0-based position of the constant as declared in the source. Relying on it for business logic (e.g. persisting it to a database, or comparing priority) is fragile — reordering, inserting, or removing a constant silently shifts every ordinal after it, quietly breaking anything depending on the old values. Prefer explicit fields or `name()` for anything that needs to be stable.

**Q: How does `enum` compare to `switch`, and can you switch directly on an enum value?**
A: Yes — `switch` has first-class support for enums; the `case` labels use the bare constant name (not `Day.MONDAY`, just `MONDAY`) since the switch's type is already known to be that enum. This is one of the main ergonomic reasons to prefer enums over raw `int`/`String` constants for a fixed set of options.

**Q: Can two different enum constants of the same enum type ever be `equal` via `==`?**
A: No — each constant is a distinct singleton instance created once at class-loading time, so reference equality (`==`) is safe and is in fact the recommended way to compare enum values (equivalent to and interchangeable with `.equals()` for enums, but conventionally `==` is used since it also does a compile-time type check).
