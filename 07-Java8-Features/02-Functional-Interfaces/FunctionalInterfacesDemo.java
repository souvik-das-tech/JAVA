// Q: Use Function<Integer,Integer> addOne and square, chained with andThen
// and separately with compose, printing both results for input 3. Combine
// two Predicate<Integer> (isEven, isPositive) with and(), or(), and
// negate(), testing a few values. Write a Supplier<String> returning a fixed
// greeting, and a Consumer<String> that prints an uppercased version,
// calling accept() with the supplier's value.

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionalInterfacesDemo {
    public static void main(String[] args) {
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> square = x -> x * x;

        System.out.println("addOne.andThen(square).apply(3) = " + addOne.andThen(square).apply(3));
        System.out.println("addOne.compose(square).apply(3) = " + addOne.compose(square).apply(3));

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;

        System.out.println("isEven.and(isPositive).test(4) = " + isEven.and(isPositive).test(4));
        System.out.println("isEven.or(isPositive).test(-3) = " + isEven.or(isPositive).test(-3));
        System.out.println("isEven.negate().test(4) = " + isEven.negate().test(4));

        Supplier<String> greeting = () -> "hello there";
        Consumer<String> shout = s -> System.out.println(s.toUpperCase());
        shout.accept(greeting.get());
    }
}
