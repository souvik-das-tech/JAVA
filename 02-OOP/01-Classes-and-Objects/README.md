# Classes & Objects

A **class** is a blueprint — it defines fields (state) and methods (behavior) but doesn't itself occupy memory for that state. An **object** is a concrete instance of a class, created with `new`, with its own copy of the instance fields.

```java
class Car {
    String model;   // instance field — state
    int speed;

    void accelerate() {   // instance method — behavior
        speed += 10;
    }
}

Car c1 = new Car();   // object #1
Car c2 = new Car();   // object #2 — independent state from c1
c1.model = "Civic";
c2.model = "Model 3";
```

- `new Car()` allocates memory on the heap for a new `Car` object and returns a reference to it; `c1` is a reference variable, not the object itself.
- Each object gets its own copy of instance fields; two objects of the same class never share instance state (unlike `static` fields, which are shared).
- A class can exist with zero objects created from it — declaring a class doesn't allocate any object memory.

## Practice Questions / Exercises

- Write a `Car` class with fields `model`, `speed`, and a method `accelerate()`; create two `Car` objects, set different `model` values, and show their state is independent.
- Add a method `printDetails()` that prints all fields of the object it's called on — call it via both object references and confirm each prints its own data.
- Create an array of `Car` objects (`Car[] cars = new Car[3]`) and initialize each element with `new Car()` — show what happens if you try to use an element before assigning it.
- Demonstrate that assigning one object reference to another (`Car c3 = c1;`) makes both variables point to the *same* object — mutate through `c3` and show the change is visible through `c1`.

## Interview Questions

**Q: What is the difference between a class and an object?**
A: A class is a template/blueprint that defines what fields and methods instances of it will have — it doesn't occupy object memory itself. An object is a runtime instance of a class, created with `new`, that has its own actual memory allocation and its own copy of the instance fields.

**Q: What does the `new` keyword actually do?**
A: It allocates memory on the heap for a new object of the given class, initializes its instance fields to their default values, invokes the constructor to complete initialization, and returns a reference to the newly created object.

**Q: If you assign one object reference to another variable, are they two separate objects?**
A: No — object variables hold references, not the object itself. `Car c2 = c1;` makes `c2` point to the *same* object as `c1`; mutating state through either reference is visible through both. Only `new` creates a genuinely separate object.

**Q: Can a class have zero objects created from it during a program's execution? What happens to its static members in that case?**
A: Yes, a class is just metadata until instantiated. Its static members still exist and are usable once the class is *loaded* by the JVM (which can happen without ever calling `new`, e.g. by referencing a static field), independent of whether any object is ever created.

**Q: What's stored in a reference variable — the object, or something else?**
A: A reference variable stores a reference (essentially a pointer/handle) to the object's location, not the object's data directly. The actual object lives on the heap; the reference variable itself (on the stack, for a local variable) just points to it.
