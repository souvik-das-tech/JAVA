// Q: Rewrite an anonymous Runnable and an anonymous Comparator<Integer> as
// lambdas, and run/use both. Write a lambda capturing a local variable
// `factor` inside a Function<Integer, Integer> that multiplies its input by
// factor, and call it with a couple of values. Print `this` behavior isn't
// directly testable statically, so instead just demonstrate a block-body
// lambda alongside an expression-body lambda for the same logic.

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class LambdaDemo {
    public static void main(String[] args) {
        Runnable r = () -> System.out.println("Running via lambda");
        r.run();

        List<Integer> numbers = new ArrayList<>(List.of(5, 3, 1, 4, 2));
        Comparator<Integer> descending = (a, b) -> b - a;
        numbers.sort(descending);
        System.out.println("Sorted descending: " + numbers);

        int factor = 10;
        Function<Integer, Integer> multiply = x -> x * factor;
        System.out.println("multiply(3) = " + multiply.apply(3));
        System.out.println("multiply(7) = " + multiply.apply(7));

        Function<Integer, Integer> square = x -> {
            int result = x * x;
            return result;
        };
        System.out.println("square(6) = " + square.apply(6));
    }
}
