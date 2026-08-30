# The `final` Keyword

`final` means "cannot be changed further" — its exact meaning depends on what it's applied to.

## `final` variables

Once assigned, the value/reference cannot be reassigned.

```java
final int MAX = 100;
MAX = 200;   // compile error

final int[] arr = {1, 2, 3};
arr[0] = 99;      // OK — the array's *contents* are mutable
arr = new int[5]; // compile error — the reference itself can't be reassigned
```

- For a reference type, `final` freezes the *reference*, not the object it points to — the referenced object's own state can still change if it has mutable fields.
- A `final` instance field must be assigned exactly once — either at declaration or in every constructor — before the constructor finishes.
- A `final` local variable (including a lambda's captured variables) must be "effectively final" if not declared `final` explicitly — lambdas can only capture local variables that are never reassigned after initialization.

## `final` methods

Cannot be **overridden** by a subclass. Used to lock in behavior that must not change (e.g. for security or to preserve an invariant depended on elsewhere).

## `final` classes

Cannot be **extended** — no subclass can be created from it. `String` and all wrapper classes (`Integer`, `Boolean`, ...) are `final`, partly to preserve their immutability guarantees.

## Practice Questions / Exercises

- Declare a `final int` and try to reassign it — read the compiler error message.
- Declare a `final` array/`List` field and show that you can mutate its contents but not reassign the reference itself.
- Write a class with a `final` instance field assigned inside the constructor (not at declaration) — then try adding a second constructor that forgets to assign it, and observe the compiler error.
- Write a `final` method in a superclass and attempt to override it in a subclass to see the compiler error; do the same with a `final` class and attempt to extend it.

## Interview Questions

**Q: What does `final` mean when applied to a variable, a method, and a class respectively?**
A: On a variable: its value/reference can be assigned only once. On a method: it cannot be overridden by any subclass. On a class: it cannot be extended/subclassed at all.

**Q: If you declare `final List<String> list = new ArrayList<>();`, can you still add elements to it?**
A: Yes — `final` only prevents `list` from being reassigned to point at a different `List` object; it says nothing about the mutability of the object itself. You can still call `list.add(...)`, `list.remove(...)`, etc.

**Q: Why must a `final` instance field be assigned in every constructor?**
A: Because a `final` field must be definitely assigned exactly once before the object finishes construction, and there's no reassigning it afterward — if some constructor path left it unassigned, the object could exist with an undefined `final` field, so the compiler enforces that every constructor sets it (directly or via constructor chaining to one that does).

**Q: Why are `String` and the wrapper classes declared `final`?**
A: Partly to guarantee their immutability can't be broken by a subclass overriding behavior in a way that mutates state, and partly for safety/performance — since `String` is used pervasively (e.g. as `HashMap` keys, security-sensitive values like class names/file paths), letting anyone subclass and alter its behavior would be a security and correctness risk.

**Q: What does "effectively final" mean, and why does it matter for lambdas and anonymous classes?**
A: A local variable is "effectively final" if it's never reassigned after its initial assignment, even without the `final` keyword. Lambdas and anonymous inner classes can only capture local variables that are final or effectively final, because they may outlive the method's stack frame — the captured value is copied, and allowing reassignment afterward would create ambiguity about which value the lambda "sees."

**Q: Can a `final` method be overloaded in a subclass?**
A: Yes — `final` only blocks *overriding* (same signature). A subclass is free to declare a different method with the same name but a different parameter list (a legitimate overload), since that's a distinct method, not a redefinition of the final one.
