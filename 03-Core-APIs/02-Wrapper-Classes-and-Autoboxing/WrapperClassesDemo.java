// Q: Demonstrate the Integer cache trap: compare Integer objects with == for
// values 100 vs 100, and 200 vs 200, printing both results. Then write code
// that deliberately triggers a NullPointerException by auto-unboxing a null
// Integer, and show the fixed version with a null check instead.

public class WrapperClassesDemo {
    public static void main(String[] args) {
        Integer a = 100, b = 100;
        System.out.println("100 == 100 (Integer): " + (a == b));

        Integer c = 200, d = 200;
        System.out.println("200 == 200 (Integer): " + (c == d));

        Integer count = null;
        try {
            int x = count; // auto-unboxing null -> NullPointerException
            System.out.println("unreachable: " + x);
        } catch (NullPointerException e) {
            System.out.println("Caught NPE from auto-unboxing null Integer");
        }

        int safeX = (count != null) ? count : 0;
        System.out.println("Safe unboxed value: " + safeX);
    }
}
