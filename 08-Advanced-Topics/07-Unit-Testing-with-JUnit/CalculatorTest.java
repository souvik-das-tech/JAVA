// Q: Write a Calculator class with add, subtract, multiply, divide methods
// (divide throws ArithmeticException on division by zero). Write a
// CalculatorTest with @BeforeEach creating a fresh Calculator, one @Test
// method per operation, and a test using assertThrows to verify divide(x,0)
// throws ArithmeticException.
//
// NOTE: requires the JUnit 5 (Jupiter) library on the classpath to compile
// and run — not available in this sandbox. Set it up via Maven/Gradle (see
// ../06-Maven-Gradle-Basics) to actually run this.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    int subtract(int a, int b) {
        return a - b;
    }

    int multiply(int a, int b) {
        return a * b;
    }

    int divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    }
}

class CalculatorTest {
    Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    @Test
    void addsTwoNumbers() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void subtractsTwoNumbers() {
        assertEquals(1, calc.subtract(3, 2));
    }

    @Test
    void multipliesTwoNumbers() {
        assertEquals(6, calc.multiply(2, 3));
    }

    @Test
    void dividesTwoNumbers() {
        assertEquals(2, calc.divide(6, 3));
    }

    @Test
    void divideByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }
}
