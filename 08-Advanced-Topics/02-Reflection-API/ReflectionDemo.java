// Q: Get a Class<?> object for a Calculator class three ways (.class,
// getClass(), Class.forName). Use getDeclaredFields()/getDeclaredMethods()
// to print all field and method names, including private ones. Use
// reflection to create an instance via its no-arg constructor, then invoke
// its add(int,int) method dynamically. Use setAccessible(true) to read and
// modify a private field, printing before and after.

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Calculator {
    private int total = 0;

    public Calculator() {
    }

    private int add(int a, int b) {
        return a + b;
    }
}

public class ReflectionDemo {
    public static void main(String[] args) throws Exception {
        Class<?> c1 = Calculator.class;
        Class<?> c2 = new Calculator().getClass();
        Class<?> c3 = Class.forName("Calculator");
        System.out.println("c1: " + c1.getName() + ", c2: " + c2.getName() + ", c3: " + c3.getName());

        System.out.println("Fields:");
        for (Field f : c1.getDeclaredFields()) {
            System.out.println("  " + f.getName());
        }
        System.out.println("Methods:");
        for (Method m : c1.getDeclaredMethods()) {
            System.out.println("  " + m.getName());
        }

        Constructor<?> ctor = c1.getDeclaredConstructor();
        Object instance = ctor.newInstance();

        Method addMethod = c1.getDeclaredMethod("add", int.class, int.class);
        addMethod.setAccessible(true);
        Object result = addMethod.invoke(instance, 3, 4);
        System.out.println("add(3, 4) via reflection: " + result);

        Field totalField = c1.getDeclaredField("total");
        totalField.setAccessible(true);
        System.out.println("total before: " + totalField.get(instance));
        totalField.set(instance, 100);
        System.out.println("total after: " + totalField.get(instance));
    }
}
