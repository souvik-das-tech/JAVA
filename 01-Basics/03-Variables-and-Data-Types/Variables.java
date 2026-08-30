// Q: Declare one variable of each of the 8 primitive types, print each one,
// then declare a String and an int[] array, and demonstrate that assigning
// one array reference to another lets both variables affect the same
// underlying array (see README.md for the full exercise list).

public class Variables {
    public static void main(String[] args) {
        // 1. One variable of each of the 8 primitive types
        byte b = 10;
        short s = 1000;
        int i = 100000;
        long l = 100000L;
        float f = 3.14f;
        double d = 3.14;
        char c = 'A';
        boolean flag = true;

        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("boolean: " + flag);

        // 2. A String (reference type)
        String name = "Alice";
        System.out.println("String: " + name);

        // 3. Two array references pointing to the SAME underlying array
        int[] a = {1, 2, 3};
        int[] copy = a; // copies the reference, not the array itself

        copy[0] = 99;
        System.out.println("a[0] after modifying copy[0]: " + a[0]); // 99 - same object

        // 4. == vs .equals() on reference types
        String x = new String("hello");
        String y = new String("hello");
        System.out.println("x == y: " + (x == y));         // false - different objects
        System.out.println("x.equals(y): " + x.equals(y)); // true - same content

        // 5. int vs double division
        int intResult = 5 / 2;
        double doubleResult = 5 / 2.0;
        System.out.println("5 / 2 = " + intResult);       // 2 - integer division truncates
        System.out.println("5 / 2.0 = " + doubleResult);  // 2.5 - one operand is double
    }
}
