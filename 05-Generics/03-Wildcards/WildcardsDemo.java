// Q: Write a method double sumAll(List<? extends Number> list) that sums a
// list of any Number subtype, and call it with List<Integer> and
// List<Double>. Write a method void addNumbers(List<? super Integer> list)
// that adds a few Integers, and call it with List<Integer>, List<Number>,
// and List<Object>. Write void printAll(List<?> list) that just prints the
// list's size, and call it with lists of different element types.

import java.util.ArrayList;
import java.util.List;

public class WildcardsDemo {
    static double sumAll(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }

    static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }

    static void printAll(List<?> list) {
        System.out.println("size = " + list.size() + ", contents = " + list);
    }

    public static void main(String[] args) {
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.5, 2.5, 3.0);
        System.out.println("sumAll(ints): " + sumAll(ints));
        System.out.println("sumAll(doubles): " + sumAll(doubles));

        List<Integer> intList = new ArrayList<>();
        addNumbers(intList);
        System.out.println("intList: " + intList);

        List<Number> numberList = new ArrayList<>();
        addNumbers(numberList);
        System.out.println("numberList: " + numberList);

        List<Object> objectList = new ArrayList<>();
        addNumbers(objectList);
        System.out.println("objectList: " + objectList);

        printAll(intList);
        printAll(List.of("a", "b"));
    }
}
