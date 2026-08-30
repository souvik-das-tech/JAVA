# Constructors

A constructor initializes a newly created object. It has the same name as the class, no return type (not even `void`), and runs automatically when `new` is used.

## Default constructor

If a class defines **no** constructor at all, the compiler auto-generates a no-argument constructor that does nothing but call `super()`. The moment you write *any* constructor yourself, the compiler stops generating this one.

## Parameterized constructor

```java
class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;   // `this.x` = field, `x` = parameter — disambiguates the shadowed name
        this.y = y;
    }
}
```

## `this`

- `this.field = param` — refers to the current object's field, distinguishing it from a same-named parameter.
- `this(...)` — calls another constructor of the *same* class (constructor chaining); must be the **first statement** in the constructor.

## Constructor chaining

```java
class Point {
    int x, y;
    Point() {
        this(0, 0);         // chains to the parameterized constructor
    }
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

- Chaining avoids duplicating initialization logic across overloaded constructors.
- `super(...)` (call to the parent class's constructor) and `this(...)` are mutually exclusive — you can only use one, and it must be the first line.
- If neither `super(...)` nor `this(...)` is written explicitly, the compiler inserts an implicit `super()` call as the first statement.

## Practice Questions / Exercises

- Write a `Point` class with a no-arg constructor that chains to a parameterized constructor via `this(0, 0)`.
- Write a class with two overloaded constructors and show, using `this(...)`, how one reuses the other's initialization logic.
- Demonstrate what happens (compiler behavior) if you write a constructor with parameters and then try to call `new MyClass()` — is the default no-arg constructor still available?
- Write a class where a constructor parameter shadows an instance field of the same name, and show why `this.field = field;` is necessary instead of just `field = field;`.

## Interview Questions

**Q: What happens if you don't define any constructor in a class?**
A: The compiler automatically generates a public, no-argument default constructor that does nothing except implicitly call `super()` (the no-arg constructor of the parent class). It disappears the moment you define any constructor yourself.

**Q: Can a class have multiple constructors? How does Java pick which one to call?**
A: Yes — this is constructor overloading. Java picks the constructor whose parameter list matches the arguments passed to `new`, using the same overload-resolution rules as method overloading (based on argument types/count, resolved at compile time).

**Q: What is constructor chaining, and how do you do it within the same class vs. to a superclass?**
A: Constructor chaining is one constructor invoking another to reuse initialization logic. Within the same class, use `this(...)`; to invoke the superclass's constructor, use `super(...)`. Both must be the first statement in the constructor, so you can never call both from the same constructor.

**Q: Why must `this(...)` or `super(...)` be the first statement in a constructor?**
A: Java requires the superclass (or an alternate same-class constructor) to be fully initialized before the current constructor's own body runs, so that any field initializers or logic in this constructor can safely rely on the inherited/chained state already being set up.

**Q: Can a constructor be `private`? What's that used for?**
A: Yes. A private constructor prevents the class from being instantiated from outside itself — commonly used in the Singleton pattern (only a static factory method inside the class can call it) or in utility classes with only static members (to prevent instantiation entirely).

**Q: Does a constructor get inherited by a subclass?**
A: No — constructors are not inherited. A subclass must define its own constructors (or rely on the compiler-generated default one), though its constructors typically call a superclass constructor via `super(...)` to initialize the inherited state.
