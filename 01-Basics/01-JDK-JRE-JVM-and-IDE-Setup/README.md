# JDK / JRE / JVM & IDE Setup

Study notes only — no code for this topic (it's setup/conceptual).

## JVM (Java Virtual Machine)

- An abstract machine that runs compiled Java bytecode (`.class` files).
- Platform-dependent implementation, but the bytecode it runs is platform-independent — this is what gives Java "write once, run anywhere."
- Responsibilities: class loading, bytecode verification, execution (interpreter + JIT compiler), memory management (garbage collection).
- JVM does **not** compile `.java` → bytecode; that's the compiler's job (`javac`). JVM only executes bytecode.

## JRE (Java Runtime Environment)

- JVM + core libraries (`java.lang`, `java.util`, etc.) + supporting files needed to **run** Java applications.
- Does **not** include development tools like the compiler.
- If you only need to run a Java program (not write/compile one), JRE is enough.

## JDK (Java Development Kit)

- JRE + development tools: `javac` (compiler), `java` (launcher), `jar`, `javadoc`, debugger, etc.
- Needed to **write and compile** Java code.
- What you install for development.

### Relationship

```
JDK = JRE + Development Tools (javac, jar, javadoc, debugger...)
JRE = JVM + Core Libraries
JVM = Bytecode execution engine
```

## Installing Java (JDK)

1. Download a JDK distribution — options: Oracle JDK, Eclipse Temurin (Adoptium), Amazon Corretto, OpenJDK. Pick an LTS version (e.g. 17 or 21) unless a project requires a specific version.
2. Install it (installer on Windows, package manager on Linux/Mac).
3. Set `JAVA_HOME` environment variable to the JDK install path.
4. Add `%JAVA_HOME%\bin` (Windows) or `$JAVA_HOME/bin` (Linux/Mac) to `PATH`.
5. Verify installation:
   ```
   java -version
   javac -version
   ```
   Both should print the installed version.

## Setting up an IDE

Pick one:

- **IntelliJ IDEA** (Community edition is free) — most popular for Java, strong refactoring/debugging tools.
- **VS Code** + "Extension Pack for Java" — lightweight, good if already using VS Code for other languages.
- **Eclipse** — older, still widely used in enterprise/legacy projects.

Steps (general):

1. Install the IDE.
2. Point it to the installed JDK (usually auto-detected via `JAVA_HOME`).
3. Create a new Java project, write a `HelloWorld` class, run it to confirm the toolchain works end-to-end (edit → compile → run).

## Interview Questions

**Q: What is the difference between JDK, JRE, and JVM?**
A: JVM is the engine that executes Java bytecode. JRE is the JVM plus the core libraries needed to *run* Java applications. JDK is the JRE plus development tools (`javac`, `jar`, `javadoc`, debugger) needed to *write and compile* Java code. So JDK ⊃ JRE ⊃ JVM.

**Q: Is JVM platform-independent? What about the bytecode it runs?**
A: No — the JVM itself is platform-*dependent* (a separate build exists per OS/architecture). What's platform-*independent* is the compiled bytecode (`.class` file): the same bytecode runs on any JVM regardless of OS, which is what gives Java "write once, run anywhere."

**Q: Can you run a Java program without installing the JDK? What's the minimum needed?**
A: Yes, if you already have a compiled `.class` file — the JRE alone is enough to run it, since running only needs the JVM + core libraries, not the compiler. You need the JDK only to compile `.java` source into bytecode.

**Q: What is JIT (Just-In-Time) compilation and how does it relate to the JVM?**
A: The JVM normally interprets bytecode instruction-by-instruction. The JIT compiler, part of the execution engine, compiles frequently-executed ("hot") bytecode into native machine code at runtime so subsequent calls run at native speed instead of being re-interpreted each time.

**Q: Why is Java called "write once, run anywhere"? What makes this possible?**
A: Java source compiles to platform-independent bytecode rather than native machine code. Any machine with a JVM built for its platform can run that same bytecode unmodified, so the same compiled artifact runs on Windows, Linux, macOS, etc. without recompilation.

**Q: What are the main components/architecture of the JVM (class loader, runtime data areas, execution engine)?**
A: Three broad parts: (1) **Class Loader Subsystem** — loads, links, and initializes `.class` files into memory; (2) **Runtime Data Areas** — memory regions like heap, stack, method area, PC registers, native method stack; (3) **Execution Engine** — interpreter + JIT compiler + garbage collector, which actually executes the bytecode and manages memory.

**Q: If you only have the JRE installed, can you compile a `.java` file? Why or why not?**
A: No — the JRE deliberately excludes development tools like `javac`. Without the compiler, there's no way to turn `.java` source into bytecode; you'd need the full JDK for that.

**Q: What is the role of `javac` vs `java` commands?**
A: `javac` is the compiler — it takes `.java` source files and produces `.class` bytecode files. `java` is the launcher — it starts the JVM and runs the bytecode in a given `.class` file (or JAR).
