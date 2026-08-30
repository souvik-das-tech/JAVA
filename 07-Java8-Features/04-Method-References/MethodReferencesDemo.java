// Q: Rewrite s -> s.toUpperCase() as a method reference and use it in
// list.stream().map(...). Use a static method reference (Integer::parseInt)
// to convert a List<String> of numbers into a List<Integer>. Use a
// constructor reference (ArrayList::new) as a Supplier passed to
// Collectors.toCollection(...). Use System.out::println as a Consumer<String>.

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MethodReferencesDemo {
    public static void main(String[] args) {
        List<String> words = List.of("apple", "banana", "cherry");
        List<String> upper = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("Uppercased: " + upper);

        List<String> numberStrings = List.of("10", "20", "30");
        List<Integer> numbers = numberStrings.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println("Parsed: " + numbers);

        ArrayList<String> collected = words.stream()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Collected into ArrayList: " + collected);

        Consumer<String> printer = System.out::println;
        printer.accept("printed via method reference");
    }
}
