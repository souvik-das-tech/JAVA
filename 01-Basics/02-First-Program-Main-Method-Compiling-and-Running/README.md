# First Program, `main` Method, Compiling & Running

## Anatomy of a Java program

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

- A `.java` file contains one or more classes; if any class is `public`, the **file name must exactly match that public class's name** (e.g. `HelloWorld.java` for `public class HelloWorld`).
- Execution always starts from the `main` method — the JVM looks for this exact signature as the entry point.

## Breaking down `public static void main(String[] args)`

- **`public`** — must be accessible from outside the class, since the JVM (external to the class) needs to call it.
- **`static`** — the JVM calls `main` without creating an object of the class first, so it must be callable on the class itself, not an instance.
- **`void`** — `main` doesn't return anything to the JVM; the program communicates results via output/exit codes, not a return value.
- **`main`** — the exact method name the JVM looks for as the entry point.
- **`String[] args`** — command-line arguments passed to the program, as an array of strings. Can also be written `String... args` (varargs).

## Compiling & running

1. `javac HelloWorld.java` — compiles source into bytecode, producing `HelloWorld.class` in the same directory.
2. `java HelloWorld` — launches the JVM, which loads `HelloWorld.class` and invokes its `main` method. (Note: no `.class` extension in this command.)
3. Command-line arguments: `java HelloWorld arg1 arg2` → `args = {"arg1", "arg2"}` inside `main`.

## Practice Questions / Exercises

- Write a program that prints `Hello, World!` to the console.
- Modify it to print your own name instead.
- Write a program that reads a command-line argument and prints it back (run it as `java ClassName SomeText`).
- Deliberately misspell `main` (e.g. `mian`) or change its signature, compile and run it — observe what error/behavior you get.
- Create a `.java` file where the public class name does **not** match the file name and see what the compiler says.

## Interview Questions

**Q: Why is the `main` method declared `public static void`?**
A: `public` so the JVM (outside the class) can call it; `static` so the JVM can invoke it without first creating an instance of the class; `void` because it doesn't need to return a value to the JVM — the program's outcome is observed via output or exit codes.

**Q: Can the `main` method be overloaded?**
A: Yes — Java allows multiple `main` methods with different parameter lists in the same class. However, the JVM only ever calls the specific `public static void main(String[] args)` signature as the entry point; overloaded versions must be invoked manually from within the code.

**Q: What happens if the public class name doesn't match the file name?**
A: A compile-time error: "class X is public, should be declared in a file named X.java."

**Q: Can `main` be declared `final`, `synchronized`, or `private`?**
A: `final` and `synchronized` are allowed since they don't violate the required signature — the JVM will still find and invoke it correctly. `private` is not allowed for the entry-point `main`, since the JVM (calling from outside the class) needs access — making it private causes a runtime error ("main method not found").

**Q: What's the significance of `String[] args`?**
A: It receives command-line arguments passed when launching the program (`java ClassName arg1 arg2`), as an array of strings. If no arguments are passed, it's an empty array (not `null`).

**Q: Can a `.java` file have multiple classes?**
A: Yes, but at most one of them can be `public`, and the file name must match that public class's name. Any number of non-public (package-private) classes can also live in the same file.

**Q: What is the difference between compile-time and runtime in this process?**
A: Compile-time is when `javac` translates `.java` source into `.class` bytecode, catching syntax/type errors. Runtime is when `java` starts the JVM and actually executes that bytecode, where logic errors or exceptions can surface.
