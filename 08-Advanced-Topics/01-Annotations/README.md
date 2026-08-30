# Annotations (Built-in & Custom)

Annotations attach metadata to code — classes, methods, fields, parameters — that tools, the compiler, or reflection at runtime can act on. They don't change program logic by themselves; something has to actually *read* them (compiler checks, a framework scanning via reflection, an annotation processor at build time).

## Built-in annotations

```java
@Override                        // compiler verifies this actually overrides a superclass/interface method
void method() { }

@Deprecated                      // marks as discouraged; compiler warns callers
void oldMethod() { }

@SuppressWarnings("unchecked")   // tells the compiler to suppress a specific warning category here
List list = new ArrayList();

@FunctionalInterface              // compiler enforces exactly one abstract method
interface MyFunctional { void run(); }
```

- `@Override` catches a common bug class: typo a method signature meant to override a parent method, and without `@Override` you silently get a harmless-looking *new* overload instead of an override — the annotation makes the compiler verify a real override exists.

## Custom annotations

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)   // keep it available for reflection at runtime
@Target(ElementType.METHOD)            // only valid on methods
public @interface Test {
    String description() default "";   // an annotation "element" — like a member with a default
}

class Calculator {
    @Test(description = "checks addition")
    void testAdd() { ... }
}
```

## `@Retention` policies

| Policy | Kept until |
|---|---|
| `SOURCE` | Discarded by the compiler — only visible in source code (e.g. `@Override`) |
| `CLASS` | Kept in the `.class` file, but not loaded by the JVM at runtime (default if unspecified) |
| `RUNTIME` | Kept and available via reflection at runtime — required if you intend to read the annotation with `getAnnotation(...)` |

## Reading annotations via reflection

```java
for (Method m : Calculator.class.getDeclaredMethods()) {
    if (m.isAnnotationPresent(Test.class)) {
        Test t = m.getAnnotation(Test.class);
        System.out.println("Found test: " + t.description());
    }
}
```

This is exactly how frameworks like JUnit find `@Test`-annotated methods, or Spring finds `@Autowired` fields — annotations + reflection.

## Practice Questions / Exercises

- Write a custom `@Test(description = "...")` annotation with `RUNTIME` retention targeting methods, and apply it to a couple of methods in a class.
- Use reflection (`Class.getDeclaredMethods()`, `Method.isAnnotationPresent()`, `Method.getAnnotation()`) to find and print all `@Test`-annotated methods and their descriptions.
- Demonstrate `@Deprecated` on a method and call it from another method, noting the compiler warning (visible in IDE/build output, not a hard error).
- Write an annotation with a `default` value for one element and omit that element at a use site, confirming the default applies.

## Interview Questions

**Q: What is the difference between `@Override`, `@Deprecated`, and `@SuppressWarnings`?**
A: `@Override` tells the compiler to verify the annotated method genuinely overrides a superclass/interface method, catching signature-mismatch typos at compile time. `@Deprecated` marks a member as discouraged for future use, triggering compiler warnings at call sites. `@SuppressWarnings("category")` tells the compiler to suppress a specific category of warning (e.g. `"unchecked"`) that would otherwise be reported for the annotated element.

**Q: What do `@Retention` and `@Target` control on a custom annotation?**
A: `@Retention` controls how long the annotation is kept — discarded after compilation (`SOURCE`), kept in the `.class` file but not loaded at runtime (`CLASS`, the default), or available via reflection at runtime (`RUNTIME`). `@Target` restricts which kinds of program elements (methods, fields, types, parameters, ...) the annotation is legally allowed to be applied to.

**Q: Why must an annotation have `RUNTIME` retention to be read via reflection?**
A: Reflection inspects the loaded `.class` file's metadata at runtime — if the annotation's retention policy is `SOURCE` or `CLASS`, that metadata either never made it into the `.class` file, or was stripped/not retained by the JVM's class loading, so there's nothing left for `getAnnotation()` to find. Only `RUNTIME` retention guarantees the JVM keeps the annotation data accessible after loading.

**Q: How do frameworks like JUnit or Spring actually "do something" based on an annotation, given annotations by themselves don't add behavior?**
A: They use reflection at startup/runtime to scan classes for specific annotations (e.g. `@Test`, `@Autowired`), and based on what they find, take action programmatically — invoking annotated test methods, injecting dependencies into annotated fields, registering annotated classes as beans, etc. The annotation itself is purely inert metadata; all the actual behavior lives in the framework code that interprets it.

**Q: What is an annotation "element," and can it have a default value?**
A: An element is like a member declared inside the annotation interface (`String description();`), specified at each usage site (`@Test(description = "...")`) unless a `default` value is given, in which case it can be omitted. Elements are restricted to primitive types, `String`, `Class`, enums, other annotations, or arrays of these — not arbitrary object types.

**Q: What's the difference between a marker annotation (like `@Deprecated`, no elements) and one with elements (like a custom `@Test(description = "...")`)?**
A: A marker annotation carries no data at all — its mere *presence* is the entire signal (e.g. `isAnnotationPresent()` is all you'd check). An annotation with elements carries structured metadata alongside its presence, retrievable via `getAnnotation(...).elementName()`, letting the reading code branch on more than just "is this annotated or not."
