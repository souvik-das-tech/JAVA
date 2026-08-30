// Q: Demonstrate widening (byte -> ... -> double) and narrowing
// (double -> int, with truncation) conversions, deliberately overflow a
// byte by casting a large int into it, parse a String to an int (and
// observe the exception on an invalid string), and show what implicit
// conversion happens when adding an int and a double (see README.md for
// the full exercise list).

public class TypeCasting {
    public static void main(String[] args) {
        // 1. Widening: byte -> short -> int -> long -> float -> double
        byte b = 42;
        short s = b;
        int i = s;
        long l = i;
        float f = l;
        double d = f;
        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);

        // 2. Narrowing: double -> int (truncates, doesn't round)
        double pi = 9.999;
        int truncated = (int) pi;
        System.out.println("(int) 9.999 = " + truncated); // 9, not 10

        // 3. Deliberate overflow: casting a large int into a byte
        int big = 300;
        byte overflowed = (byte) big;
        System.out.println("(byte) 300 = " + overflowed); // wraps around, not clamped

        // 4. String -> int, valid and invalid
        int parsed = Integer.parseInt("42");
        System.out.println("Integer.parseInt(\"42\") = " + parsed);

        try {
            Integer.parseInt("12a");
        } catch (NumberFormatException e) {
            System.out.println("Parsing \"12a\" threw: " + e);
        }

        // 5. int + double -> implicit widening of the int operand to double
        int intVal = 5;
        double doubleVal = 2.5;
        double sum = intVal + doubleVal;
        System.out.println("5 + 2.5 = " + sum); // int widened to double before addition
    }
}
