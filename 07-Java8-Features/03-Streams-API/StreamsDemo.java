// Q: Given a List<String> of names, filter for those longer than 4 letters,
// uppercase them, sort alphabetically, and collect to a List, printing the
// result. Use reduce to compute the sum and the max of a List<Integer>. Use
// Collectors.groupingBy to group a List<String> of words by length. Use
// Collectors.partitioningBy to split a List<Integer> into evens and odds.

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamsDemo {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Dave", "Eve");

        List<String> result = names.stream()
                .filter(name -> name.length() > 4)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Filtered/mapped/sorted: " + result);

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int sum = numbers.stream().reduce(0, Integer::sum);
        int max = numbers.stream().reduce(Integer::max).orElseThrow();
        System.out.println("Sum: " + sum + ", Max: " + max);

        List<String> words = List.of("cat", "dog", "bird", "ant", "lion");
        Map<Integer, List<String>> grouped = words.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + grouped);

        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Partitioned by even/odd: " + partitioned);
    }
}
