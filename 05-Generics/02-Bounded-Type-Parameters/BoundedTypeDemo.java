// Q: Write a NumberBox<T extends Number> class with a value field and a
// doubled() method that returns value.doubleValue() * 2. In main, create a
// NumberBox<Integer> and a NumberBox<Double>, and call doubled() on each.
// Then write a generic static method <T extends Comparable<T>> T max(List<T>
// list) and test it with a List<Integer> and a List<String>.

import java.util.List;

class NumberBox<T extends Number> {
    T value;

    NumberBox(T value) {
        this.value = value;
    }

    double doubled() {
        return value.doubleValue() * 2;
    }
}

public class BoundedTypeDemo {
    static <T extends Comparable<T>> T max(List<T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        NumberBox<Integer> intBox = new NumberBox<>(21);
        System.out.println("intBox.doubled(): " + intBox.doubled());

        NumberBox<Double> doubleBox = new NumberBox<>(3.5);
        System.out.println("doubleBox.doubled(): " + doubleBox.doubled());

        System.out.println("max(ints): " + max(List.of(3, 7, 2, 9, 4)));
        System.out.println("max(strings): " + max(List.of("banana", "apple", "cherry")));
    }
}
