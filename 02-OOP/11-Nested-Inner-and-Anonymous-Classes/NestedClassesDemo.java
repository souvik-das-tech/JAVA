// Q: Write an Outer class with a private int value, a static nested class
// Nested with its own greet() method, and a non-static Inner class whose
// show() method prints Outer's private value. In main, create a Nested
// instance without any Outer instance, and an Inner instance via
// outer.new Inner(). Also create an anonymous Runnable inline and run it.

class Outer {
    private int value = 42;

    static class Nested {
        void greet() {
            System.out.println("Hi from nested");
        }
    }

    class Inner {
        void show() {
            System.out.println("Outer value = " + value);
        }
    }
}

public class NestedClassesDemo {
    public static void main(String[] args) {
        Outer.Nested nested = new Outer.Nested();
        nested.greet();

        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.show();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running anonymously");
            }
        };
        r.run();
    }
}
