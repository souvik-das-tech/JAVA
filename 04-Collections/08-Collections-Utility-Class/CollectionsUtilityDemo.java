// Q: Given a List<Integer> {5, 3, 1, 4, 2}, sort it ascending with
// Collections.sort, then descending with Comparator.reverseOrder(). Use
// Collections.max/min, Collections.frequency (after adding a duplicate),
// and Collections.binarySearch on the sorted version. Then wrap the list
// with Collections.unmodifiableList and demonstrate that calling add() on
// the wrapper throws UnsupportedOperationException.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionsUtilityDemo {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(5, 3, 1, 4, 2));

        Collections.sort(numbers);
        System.out.println("Ascending: " + numbers);

        Collections.sort(numbers, Comparator.reverseOrder());
        System.out.println("Descending: " + numbers);

        System.out.println("Max: " + Collections.max(numbers));
        System.out.println("Min: " + Collections.min(numbers));

        numbers.add(3);
        System.out.println("After adding duplicate 3: " + numbers);
        System.out.println("Frequency of 3: " + Collections.frequency(numbers, 3));

        Collections.sort(numbers);
        System.out.println("Sorted for binary search: " + numbers);
        System.out.println("binarySearch(4): " + Collections.binarySearch(numbers, 4));

        List<Integer> readOnly = Collections.unmodifiableList(numbers);
        try {
            readOnly.add(99);
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException on unmodifiable list");
        }
    }
}
