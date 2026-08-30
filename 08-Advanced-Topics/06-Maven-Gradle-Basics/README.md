# Basics of Maven/Gradle

Study notes only — no `.java` code file for this topic (it's about build-tool configuration files, not language features).

Both Maven and Gradle solve the same core problems: **dependency management** (download and wire up third-party libraries), a **standardized build lifecycle** (compile → test → package → ...), and **project structure conventions** — replacing manually managing classpaths and running `javac`/`jar` by hand.

## Maven (`pom.xml`, XML-based)

```xml
<project>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

- **Convention over configuration**: expects `src/main/java` for source, `src/test/java` for tests, `src/main/resources` for non-code files — following this layout means barely any configuration is needed.
- **Lifecycle phases** (run in order — running a later phase runs all earlier ones first): `validate` → `compile` → `test` → `package` → `verify` → `install` → `deploy`.
- Common commands: `mvn compile`, `mvn test`, `mvn package` (produces a `.jar`/`.war` in `target/`), `mvn install` (also puts it in your local `~/.m2` repository for other local projects to depend on).
- Dependencies are pulled from a **repository** (Maven Central by default) — declared by `groupId:artifactId:version` (a "GAV" coordinate), with **transitive dependencies** (your dependency's own dependencies) resolved automatically.

## Gradle (`build.gradle` / `build.gradle.kts`, Groovy/Kotlin DSL-based)

```groovy
plugins {
    id 'java'
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}
```

- Uses a real programming language (Groovy or Kotlin) for build scripts instead of Maven's declarative XML — more flexible and powerful for custom build logic, at the cost of being less immediately declarative/predictable to read.
- Generally **faster** than Maven for incremental builds, due to build caching and a task-dependency graph that skips work already done and up-to-date.
- Common commands: `gradle build`, `gradle test`, `gradle run` (with the `application` plugin).

## Maven vs Gradle at a glance

| | Maven | Gradle |
|---|---|---|
| Config format | XML (`pom.xml`) | Groovy/Kotlin DSL (`build.gradle[.kts]`) |
| Philosophy | Convention, declarative | Flexible, scriptable |
| Build speed | Slower (less caching by default) | Generally faster (incremental builds, caching) |
| Learning curve | Simpler for standard cases | More powerful, steeper for custom logic |

## Practice Questions / Exercises

- Create a minimal `pom.xml` for a Java project and add JUnit as a `test`-scoped dependency; run `mvn test` (once JUnit tests exist, see [[07-Unit-Testing-with-JUnit]]).
- Create the Gradle equivalent (`build.gradle`) for the same project, and compare the two files side by side.
- Look up (or run) `mvn dependency:tree` to see how transitive dependencies get resolved for a real dependency like Spring Boot.
- Identify which Maven lifecycle phase `mvn install` implies runs before it (hint: earlier phases always run first).

## Interview Questions

**Q: What core problems do build tools like Maven and Gradle solve?**
A: Dependency management (automatically downloading and wiring up third-party libraries, including their own transitive dependencies, instead of manually managing `.jar` files and classpaths), a standardized, repeatable build lifecycle (compile, test, package, deploy), and enforced project structure conventions that make any project built with the tool immediately navigable to someone else familiar with it.

**Q: What does "convention over configuration" mean in the context of Maven?**
A: Maven expects a standard project layout (`src/main/java`, `src/test/java`, `src/main/resources`, etc.) and standard lifecycle phase names — if you follow these conventions, a minimal `pom.xml` (just project coordinates and dependencies) is enough to build, test, and package the project, without explicitly configuring source directories, compiler invocations, etc.

**Q: What is a Maven lifecycle phase, and why does running `mvn package` also run `mvn test` first?**
A: Lifecycle phases represent an ordered sequence of build steps (`validate`, `compile`, `test`, `package`, ...). Running any given phase automatically runs every phase *before* it in that sequence first — `package` implies `compile` and `test` must have already succeeded, since packaging a broken or untested build wouldn't make sense.

**Q: What's the main philosophical difference between Maven and Gradle?**
A: Maven is declarative and XML-based — you describe *what* the project needs (dependencies, plugins) and Maven's fixed lifecycle handles *how*. Gradle uses a real scripting language (Groovy/Kotlin) for its build files, giving you programmatic flexibility to define custom build logic and tasks, generally at some cost to how immediately declarative/predictable a build file is to read compared to Maven's structured XML.

**Q: What is a "transitive dependency," and why does automatic resolution matter?**
A: If your project depends on library A, and A itself depends on library B, then B is a transitive dependency of your project — Maven/Gradle automatically resolve and include B (and everything it in turn depends on) without you needing to declare it explicitly. This matters because manually tracking an entire dependency tree by hand for any non-trivial library (e.g. Spring Boot, which pulls in dozens of transitive dependencies) would be impractical.

**Q: Why is Gradle often described as faster than Maven for iterative/local development?**
A: Gradle builds a task dependency graph and supports incremental builds and build caching — it can skip re-running tasks whose inputs haven't changed since the last build (including, in some setups, reusing cached outputs from other machines/CI). Maven's default lifecycle model re-runs each phase's work more unconditionally, without the same fine-grained incremental/caching model built in.
