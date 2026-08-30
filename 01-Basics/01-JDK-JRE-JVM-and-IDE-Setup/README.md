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

## Key takeaways / self-check

- [ ] I can explain the difference between JDK, JRE, and JVM in my own words.
- [ ] Java is installed and `java -version` / `javac -version` both work from the terminal.
- [ ] `JAVA_HOME` is set correctly.
- [ ] IDE is installed and configured with the JDK.
- [ ] I ran a Hello World program successfully through the IDE (and/or terminal with `javac`/`java`).
