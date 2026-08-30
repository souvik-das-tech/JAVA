public class Loops {
    public static void main(String[] args) {
        // for loop: 1 to 10
        System.out.println("-- for loop --");
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // while loop: 1 to 10
        System.out.println("-- while loop --");
        int i = 1;
        while (i <= 10) {
            System.out.print(i + " ");
            i++;
        }
        System.out.println();

        // do-while: runs at least once even though condition starts false
        System.out.println("-- do-while (condition starts false) --");
        int j = 100;
        do {
            System.out.println("Runs once even though j (" + j + ") < 5 is false");
        } while (j < 5);

        // enhanced for: sum array elements
        int[] nums = {4, 8, 15, 16, 23, 42};
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        System.out.println("Sum of array: " + sum);

        // nested loops with labeled break: multiplication table, stop once product > 50
        System.out.println("-- labeled break when product > 50 --");
        outer:
        for (int a = 1; a <= 10; a++) {
            for (int b = 1; b <= 10; b++) {
                int product = a * b;
                if (product > 50) {
                    System.out.println("Stopping at " + a + " x " + b + " = " + product);
                    break outer;
                }
                System.out.print(product + " ");
            }
        }
        System.out.println();

        // continue: skip multiples of 3 from 1 to 20
        System.out.println("-- skipping multiples of 3 --");
        for (int n = 1; n <= 20; n++) {
            if (n % 3 == 0) {
                continue;
            }
            System.out.print(n + " ");
        }
        System.out.println();
    }
}
