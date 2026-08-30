import java.util.Arrays;

public class MethodsDemo {
    public static void main(String[] args) {
        // Overloading: which version gets picked
        System.out.println("add(2, 3) -> " + add(2, 3));
        System.out.println("add(2.5, 3.5) -> " + add(2.5, 3.5));
        System.out.println("add(1, 2, 3) -> " + add(1, 2, 3));

        // Pass-by-value: primitive vs array parameter
        int number = 10;
        tryToModify(number);
        System.out.println("number after tryToModify: " + number); // unchanged

        int[] arr = {1, 2, 3};
        modifyFirstElement(arr);
        System.out.println("arr after modifyFirstElement: " + Arrays.toString(arr)); // changed

        // Varargs
        System.out.println("sum() -> " + sum());
        System.out.println("sum(5) -> " + sum(5));
        System.out.println("sum(1, 2, 3, 4) -> " + sum(1, 2, 3, 4));

        // Method returning an array
        int[] fib = firstFibonacci(8);
        System.out.println("First 8 Fibonacci numbers: " + Arrays.toString(fib));
    }

    // Overloaded add() methods
    static int add(int a, int b) {
        return a + b;
    }

    static double add(double a, double b) {
        return a + b;
    }

    static int add(int a, int b, int c) {
        return a + b + c;
    }

    // Pass-by-value demonstration
    static void tryToModify(int value) {
        value = 999; // only changes the local copy
    }

    static void modifyFirstElement(int[] values) {
        values[0] = 999; // mutates the object the caller's reference also points to
    }

    // Varargs
    static int sum(int... nums) {
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    // Returning an array
    static int[] firstFibonacci(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = (i <= 1) ? i : result[i - 1] + result[i - 2];
        }
        return result;
    }
}
