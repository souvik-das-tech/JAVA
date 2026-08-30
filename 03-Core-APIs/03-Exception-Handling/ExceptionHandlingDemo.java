// Q: Write code with a try block that triggers ArithmeticException (divide
// by zero), catch it separately from ArrayIndexOutOfBoundsException (trigger
// that too, in a second try), each with its own catch block, and a finally
// that always prints "done". Then demonstrate finally running even when the
// try block has a return statement, by putting the logic in a small helper
// method and printing its returned value.

public class ExceptionHandlingDemo {
    static int helperWithReturn() {
        try {
            return 42;
        } finally {
            System.out.println("finally runs even though try returned");
        }
    }

    public static void main(String[] args) {
        try {
            int result = 10 / 0;
            System.out.println("unreachable: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("done");
        }

        try {
            int[] arr = { 1, 2, 3 };
            System.out.println(arr[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("done");
        }

        int value = helperWithReturn();
        System.out.println("Returned value: " + value);
    }
}
