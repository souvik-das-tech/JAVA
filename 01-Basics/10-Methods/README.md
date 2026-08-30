# Methods (Declaration, Parameters, Return Types, Overloading)

## Declaration

```java
accessModifier returnType methodName(parameterType paramName, ...) {
    // body
    return value; // omitted if returnType is void
}
```

```java
public static int add(int a, int b) {
    return a + b;
}
```

- `returnType` is `void` if the method returns nothing; otherwise every code path must return a value of that type (or a compatible subtype).
- `static` methods belong to the class itself and are called via `ClassName.method()`; instance methods belong to an object and are called via `object.method()`.

## Parameters

- Java is **pass-by-value** for everything: primitives are copied by value; object references are also copied by value (the copy points to the same object, so mutations through it are visible, but reassigning the parameter inside the method doesn't affect the caller's variable).
- **Varargs**: `void print(String... items)` lets the caller pass zero, one, or many arguments (or an array) — treated as `String[]` inside the method. Must be the last parameter.

## Return types

- A method with a non-`void` return type must return a value on **every possible execution path** — the compiler enforces this ("missing return statement" otherwise).
- Can return primitives, objects, arrays, or nothing (`void`).

## Overloading

```java
int add(int a, int b) { return a + b; }
double add(double a, double b) { return a + b; }
int add(int a, int b, int c) { return a + b + c; }
```

- Same method name, **different parameter list** (type, number, or order of parameters) within the same class.
- Return type **alone** is not enough to overload — two methods with identical parameter lists but different return types will not compile.
- Resolved at **compile time** based on the static types of the arguments (this is why it's a form of *static/compile-time polymorphism*, unlike overriding).

## Practice Questions / Exercises

- Write an overloaded `add` method: one version for two `int`s, one for two `double`s, one for three `int`s. Call all three and observe which one is picked.
- Write a method that takes an `int` parameter and tries to change it, and a method that takes an array and modifies an element — show the difference in whether the caller sees the change.
- Write a varargs method `sum(int... nums)` that works whether called with zero, one, or many arguments.
- Write a method returning an `int[]` (e.g. the first `n` Fibonacci numbers) and call it, printing the returned array.
- Try to write two methods with the same name and parameter list but different return types, and observe the compiler error.

## Interview Questions

**Q: What is method overloading, and how does the compiler decide which overload to call?**
A: Overloading is defining multiple methods with the same name but different parameter lists (type, number, or order) in the same class. The compiler picks the best match at **compile time** based on the static (declared) types of the arguments passed — this is why it's called compile-time/static polymorphism.

**Q: Can two methods be overloaded if they differ only in return type?**
A: No. The parameter list must differ; return type alone is not part of a method's overload signature. Two methods with identical parameter lists but different return types produce a compile error ("method X is already defined").

**Q: Is Java pass-by-value or pass-by-reference for method parameters?**
A: Always pass-by-value. Primitives are copied by value. Object references are also copied by value — the method gets a copy of the reference (pointing to the same object), so it can mutate the object's state, but reassigning the parameter itself inside the method has no effect on the caller's original variable.

**Q: What's the difference between `static` and instance methods regarding how they're called and what they can access?**
A: `static` methods belong to the class, are called without an instance (`ClassName.method()`), and can't directly access instance (non-static) fields or methods since there's no `this`. Instance methods belong to an object, are called on an instance (`obj.method()`), and can access both instance and static members.

**Q: What are varargs, and what are the rules around using them?**
A: Varargs (`Type... name`) let a method accept a variable number of arguments of that type, treated as an array inside the method body. Rules: a method can have at most one varargs parameter, and it must be the **last** parameter in the list.

**Q: Does the compiler enforce that a non-void method always returns a value?**
A: Yes — every possible code path through a non-`void` method must end in a `return` statement with a compatible value, or the code won't compile ("missing return statement"). This is checked via static reachability analysis, not just "somewhere in the method."
