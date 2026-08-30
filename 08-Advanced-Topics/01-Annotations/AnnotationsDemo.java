// Q: Write a custom @Test(description = "...") annotation with RUNTIME
// retention targeting methods, with a default description. Apply it to two
// methods in a Calculator class. Use reflection to find all @Test-annotated
// methods and print their descriptions.

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {
    String description() default "no description";
}

class Calculator {
    @Test(description = "checks addition")
    void testAdd() {
    }

    @Test
    void testSubtract() {
    }

    void notATest() {
    }
}

public class AnnotationsDemo {
    public static void main(String[] args) {
        for (Method m : Calculator.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                Test t = m.getAnnotation(Test.class);
                System.out.println("Found test: " + m.getName() + " -> " + t.description());
            }
        }
    }
}
