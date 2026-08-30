// Q: Write a method Optional<String> findUser(int id) that returns
// Optional.empty() for unknown IDs (e.g. anything but 1 and 2). Call it for
// a known and unknown id, using orElse, orElseGet, and orElseThrow
// respectively at three call sites. Chain .map() and .filter() on an
// Optional<String>. Use ifPresentOrElse to print either the value or a
// fallback. Call .get() on an empty Optional and catch
// NoSuchElementException.

import java.util.NoSuchElementException;
import java.util.Optional;

public class OptionalDemo {
    static Optional<String> findUser(int id) {
        if (id == 1) return Optional.of("Alice");
        if (id == 2) return Optional.of("Bob");
        return Optional.empty();
    }

    public static void main(String[] args) {
        System.out.println("findUser(1).orElse: " + findUser(1).orElse("Unknown"));
        System.out.println("findUser(99).orElse: " + findUser(99).orElse("Unknown"));
        System.out.println("findUser(99).orElseGet: " + findUser(99).orElseGet(() -> "Computed Default"));

        try {
            findUser(99).orElseThrow(() -> new IllegalStateException("no such user"));
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        Optional<String> transformed = findUser(1)
                .map(String::toUpperCase)
                .filter(name -> name.length() > 3);
        System.out.println("Transformed: " + transformed);

        findUser(2).ifPresentOrElse(
                name -> System.out.println("Found: " + name),
                () -> System.out.println("Not found")
        );
        findUser(99).ifPresentOrElse(
                name -> System.out.println("Found: " + name),
                () -> System.out.println("Not found")
        );

        try {
            Optional.empty().get();
        } catch (NoSuchElementException e) {
            System.out.println("Caught NoSuchElementException from empty Optional.get()");
        }
    }
}
