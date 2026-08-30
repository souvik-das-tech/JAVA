// Q: Add the same list of strings (including one duplicate) to a HashSet, a
// LinkedHashSet, and a TreeSet. Print each set and compare the iteration
// order between the three. Then create a TreeSet<Integer> with a few values
// and print the results of first(), last(), higher(x), and lower(x) for
// some x in the set.

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetDemo {
    public static void main(String[] args) {
        List<String> words = List.of("banana", "apple", "cherry", "banana");

        Set<String> hashSet = new HashSet<>(words);
        Set<String> linkedHashSet = new LinkedHashSet<>(words);
        Set<String> treeSet = new TreeSet<>(words);

        System.out.println("HashSet: " + hashSet);
        System.out.println("LinkedHashSet: " + linkedHashSet);
        System.out.println("TreeSet: " + treeSet);

        TreeSet<Integer> numbers = new TreeSet<>();
        numbers.add(1);
        numbers.add(3);
        numbers.add(5);
        numbers.add(7);

        System.out.println("first(): " + numbers.first());
        System.out.println("last(): " + numbers.last());
        System.out.println("higher(3): " + numbers.higher(3));
        System.out.println("lower(3): " + numbers.lower(3));
    }
}
