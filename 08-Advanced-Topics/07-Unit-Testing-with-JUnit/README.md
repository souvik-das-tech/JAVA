# Unit Testing with JUnit

> Note: unlike [[05-JDBC]], this one *was* verified — the demo file needs the JUnit library on the classpath (it's not part of the core JDK), so it was compiled and run against the `junit-platform-console-standalone` jar (all 5 tests pass). On your own machine, get it onto the classpath via Maven/Gradle instead (see [[06-Maven-Gradle-Basics]]) rather than a manually downloaded standalone jar.

JUnit (current version: **JUnit 5** / "Jupiter") is the standard framework for writing and running automated unit tests in Java — verifying a piece of code behaves as expected, repeatably, without manual checking.

## A basic test

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void addsTwoNumbers() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        assertEquals(5, result);   // fails the test if result != 5
    }
}
```

- `@Test` marks a method as a test case — JUnit discovers and runs all `@Test`-annotated methods (this is exactly the annotation + reflection pattern from [[01-Annotations]]).
- Assertion methods (`assertEquals`, `assertTrue`, `assertFalse`, `assertThrows`, `assertNull`, ...) check an expected condition and make the test **fail** (throwing an `AssertionError`) if it doesn't hold — a test with no failed assertions **passes**.

## Common assertions

```java
assertEquals(expected, actual);
assertTrue(condition);
assertFalse(condition);
assertNull(value);
assertNotNull(value);
assertThrows(IllegalArgumentException.class, () -> calc.divide(1, 0));
```

## Setup/teardown lifecycle

```java
class CalculatorTest {
    Calculator calc;

    @BeforeEach   // runs before EVERY test method — fresh state per test
    void setUp() {
        calc = new Calculator();
    }

    @AfterEach    // runs after every test method — cleanup
    void tearDown() { }

    @BeforeAll    // runs ONCE before all tests in this class (must be static)
    static void setUpAll() { }

    @AfterAll     // runs ONCE after all tests in this class (must be static)
    static void tearDownAll() { }
}
```

- `@BeforeEach` running fresh for every test is important for **test isolation** — one test's leftover state should never affect another test's outcome, regardless of run order.

## Test organization

```java
@Test
@DisplayName("adding two positive numbers returns their sum")
void addsTwoPositiveNumbers() { ... }

@Test
void divideByZeroThrows() {
    assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
}
```

- `@DisplayName` gives a readable name in test reports, independent of the method name.
- Good test names/structure describe **behavior** ("what should happen"), making a failing test's report self-explanatory without reading its implementation.

## Practice Questions / Exercises

- Write a `Calculator` class with `add`, `subtract`, `multiply`, `divide` methods (`divide` throwing `ArithmeticException` on division by zero).
- Write a `CalculatorTest` with one `@Test` method per operation, using `@BeforeEach` to create a fresh `Calculator` instance for each test.
- Write a test using `assertThrows` to verify `divide(x, 0)` throws `ArithmeticException`.
- Deliberately write a failing assertion (wrong expected value) and read the failure output JUnit produces, to get used to interpreting test failures.

## Interview Questions

**Q: What does `@Test` do, and how does JUnit actually find and run test methods?**
A: `@Test` marks a method as a test case for JUnit to discover. Under the hood, JUnit uses reflection (see [[02-Reflection-API]]) to scan test classes for methods annotated `@Test`, then invokes each one via reflection, catching any thrown exception/`AssertionError` to determine pass/fail — the exact annotation + reflection pattern discussed in the Annotations topic.

**Q: How does a test actually "fail"?**
A: An assertion method (`assertEquals`, `assertTrue`, etc.) throws an `AssertionError` (or a JUnit-specific subclass carrying expected/actual details) when the checked condition doesn't hold. JUnit's test runner catches this per test method — a caught `AssertionError` (or any other uncaught exception during the test) marks that specific test as failed, without stopping the rest of the test suite from running.

**Q: What's the difference between `@BeforeEach` and `@BeforeAll`?**
A: `@BeforeEach` runs before *every single* test method in the class, giving each test fresh, isolated setup (e.g. a new object instance) so tests can't accidentally affect each other via shared state. `@BeforeAll` runs exactly *once*, before any test in the class runs — used for expensive setup genuinely safe to share across all tests (and must be `static`, since it runs before any test instance exists).

**Q: Why is test isolation (each test getting fresh state) important?**
A: Without it, a test's outcome could depend on which other tests ran before it and in what order — a test that passes alone might fail (or worse, pass for the wrong reason) when run after another test that left behind some shared mutated state. Isolated tests are deterministic and can be run in any order (including in parallel) with identical results.

**Q: How would you test that a method correctly throws an exception under some condition?**
A: Use `assertThrows(ExceptionType.class, () -> methodCallThatShouldThrow())` — it runs the given lambda, asserts that it throws an exception of (at least) the specified type, fails the test if it doesn't throw or throws a different type, and returns the caught exception so you can further assert on its message if needed.

**Q: What's the difference between a unit test and other kinds of tests (integration, end-to-end)?**
A: A unit test verifies a single, small unit of code (typically one class/method) in isolation — dependencies are often replaced with test doubles (mocks/stubs) so the test doesn't depend on external systems like a real database or network call. Integration tests verify multiple components (or a component and a real dependency, like an actual database) work correctly together; end-to-end tests exercise the whole system as a user would. Unit tests are fastest and most numerous; the other kinds trade speed for broader, more realistic coverage.
