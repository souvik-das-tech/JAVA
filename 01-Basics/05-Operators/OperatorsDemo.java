public class OperatorsDemo {
    public static void main(String[] args) {
        // Arithmetic
        int a = 7, b = 2;
        System.out.println("7 / 2 (int) = " + (a / b));         // 3, truncated
        System.out.println("7 / 2.0 (double) = " + (a / 2.0));  // 3.5
        System.out.println("7 % 2 = " + (a % b));                // 1

        // Pre vs post increment
        int x = 5;
        System.out.println("x++ returns: " + (x++) + ", x is now: " + x); // 5, then 6
        int y = 5;
        System.out.println("++y returns: " + (++y) + ", y is now: " + y); // 6, then 6

        // Relational
        System.out.println("7 > 2: " + (a > b));
        System.out.println("7 == 2: " + (a == b));

        // Short-circuit logical operators
        System.out.println("false && sideEffect(): " + (false && sideEffect("&&")));
        System.out.println("true || sideEffect(): " + (true || sideEffect("||")));
        System.out.println("true && sideEffect(): " + (true && sideEffect("&& (runs)")));

        // Bitwise
        int m = 12, n = 10; // 1100, 1010
        System.out.println("12 & 10 = " + (m & n) + " (" + Integer.toBinaryString(m & n) + ")");
        System.out.println("12 | 10 = " + (m | n) + " (" + Integer.toBinaryString(m | n) + ")");
        System.out.println("12 ^ 10 = " + (m ^ n) + " (" + Integer.toBinaryString(m ^ n) + ")");
        System.out.println("12 << 2 = " + (m << 2));
        System.out.println("-12 >> 2 = " + (-m >> 2));   // sign-preserving
        System.out.println("-12 >>> 2 = " + (-m >>> 2)); // fills with 0

        // Assignment (compound) with implicit narrowing cast
        byte bb = 10;
        bb += 5; // equivalent to bb = (byte)(bb + 5)
        System.out.println("byte bb after += : " + bb);

        // Ternary
        int max = (a > b) ? a : b;
        System.out.println("max(7, 2) = " + max);
        int num = 9;
        String parity = (num % 2 == 0) ? "even" : "odd";
        System.out.println(num + " is " + parity);
    }

    private static boolean sideEffect(String label) {
        System.out.println("  -> sideEffect() evaluated for " + label);
        return true;
    }
}
