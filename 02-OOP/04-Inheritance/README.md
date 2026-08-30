# Inheritance

Inheritance lets one class (subclass/child) acquire the fields and methods of another (superclass/parent) via `extends`, enabling code reuse and establishing an "is-a" relationship.

```java
class Animal {
    String name;
    void eat() { System.out.println(name + " is eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println(name + " says Woof"); }
}

Dog d = new Dog();
d.name = "Rex";   // inherited field
d.eat();          // inherited method
d.bark();         // own method
```

- Java supports **single inheritance** for classes — a class can `extends` only one other class (multiple inheritance of *state* is disallowed to avoid the diamond problem; interfaces fill that gap instead).
- Every class implicitly extends `Object` if it doesn't extend anything else — so every object has `equals()`, `hashCode()`, `toString()`, etc. from `Object` by default.
- `private` members of the superclass are **not** directly accessible in the subclass (though they exist and are inherited in the sense of being part of the object's memory layout); only `public`/`protected`/package-private (same package) members are directly reachable.

## `super`

- `super.field` / `super.method()` — accesses the parent class's field/method, useful when the subclass overrides a method but still wants the parent's behavior as part of its own.
- `super(...)` — calls the parent's constructor; must be the first statement in the subclass constructor (or the compiler inserts an implicit `super()` call).

```java
class Dog extends Animal {
    void eat() {
        super.eat();   // reuse parent's eat(), then add extra behavior
        System.out.println("...enthusiastically");
    }
}
```

## Practice Questions / Exercises

- Write an `Animal` class with a `name` field and `eat()` method, and a `Dog` subclass that adds a `bark()` method — create a `Dog` object and call both inherited and own methods on it.
- Override `eat()` in `Dog` to call `super.eat()` and then print an extra line, showing how to extend rather than fully replace parent behavior.
- Give `Animal` a constructor that takes `name`, and give `Dog` a constructor that calls `super(name)` — show what compiler error you get if you remove the `super(name)` call while `Animal` has no no-arg constructor.
- Create a three-level hierarchy (`Animal` → `Dog` → `Puppy`) and show that a `Puppy` object has access to methods/fields from all three levels.

## Interview Questions

**Q: Why doesn't Java support multiple inheritance for classes?**
A: To avoid the "diamond problem" — ambiguity when two parent classes define a conflicting field or method implementation and the compiler can't unambiguously decide which one a subclass should inherit. Java sidesteps this for state/implementation by allowing only single class inheritance, while still allowing a class to implement multiple *interfaces* (which historically had no conflicting state, and since Java 8 resolves default-method conflicts by forcing the subclass to override explicitly).

**Q: What is the difference between `super.method()` and just calling `method()` inside an overriding method?**
A: Calling `method()` (or `this.method()`) invokes the subclass's own overriding version (dynamic dispatch). `super.method()` explicitly calls the parent class's version, bypassing the override — commonly used to extend rather than fully replace the parent's behavior.

**Q: If a subclass doesn't define any constructor, and the superclass has no no-arg constructor, what happens?**
A: Compile error. The compiler inserts an implicit `super()` call in the subclass's (also implicit, in this case) default constructor, but if the superclass has no no-arg constructor available, that implicit call fails to resolve, and you're forced to explicitly define a subclass constructor with the correct `super(args)` call.

**Q: Are private members of a superclass inherited by the subclass?**
A: They exist as part of the subclass object's memory layout (inherited in that sense), but they are not directly *accessible* by name in the subclass — the subclass can only reach them indirectly, through public/protected methods the superclass exposes (like getters).

**Q: What's the difference between inheritance ("is-a") and composition ("has-a")? When would you prefer one over the other?**
A: Inheritance models an "is-a" relationship (a `Dog` is an `Animal`) by extending a class. Composition models a "has-a" relationship by holding a reference to another object as a field (a `Car` has an `Engine`). Composition is generally preferred when you just want to reuse behavior without being tightly coupled to the other class's implementation and hierarchy — it's more flexible and doesn't break encapsulation the way deep inheritance chains can ("favor composition over inheritance").

**Q: Can a subclass reduce the visibility of an inherited method when overriding it?**
A: No — an overriding method can only keep the same visibility or widen it (e.g. `protected` → `public`), never narrow it (e.g. `public` → `private`), because that would violate the superclass's contract that the method is callable wherever the superclass type is used (Liskov Substitution Principle).
