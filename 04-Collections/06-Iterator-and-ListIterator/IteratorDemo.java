// Q: Given a List<Integer> of mixed even/odd numbers, use Iterator.remove()
// to remove all even numbers while iterating, printing the list before and
// after. Then use ListIterator on a List<String> to uppercase every element
// via set() while walking forward, and print each element while walking
// backward afterward.

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IteratorDemo {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        System.out.println("Before: " + numbers);

        Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            int n = it.next();
            if (n % 2 == 0) {
                it.remove();
            }
        }
        System.out.println("After removing evens: " + numbers);

        List<String> words = new ArrayList<>(List.of("apple", "banana", "cherry"));
        ListIterator<String> lit = words.listIterator();
        while (lit.hasNext()) {
            String s = lit.next();
            lit.set(s.toUpperCase());
        }
        System.out.println("Uppercased: " + words);

        System.out.print("Walking backward: ");
        while (lit.hasPrevious()) {
            System.out.print(lit.previous() + " ");
        }
        System.out.println();
    }
}
