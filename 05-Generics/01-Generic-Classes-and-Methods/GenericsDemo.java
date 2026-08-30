// Q: Write a generic Box<T> class with set(T)/get() methods, and create
// instances holding a String and an Integer. Write a generic Pair<K, V>
// class with a toString() printing both key and value. Write a generic
// static method <T> T firstElement(List<T> list) and call it with a
// List<String> and a List<Integer>.

import java.util.List;

class Box<T> {
    private T content;

    void set(T content) {
        this.content = content;
    }

    T get() {
        return content;
    }
}

class Pair<K, V> {
    K key;
    V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}

public class GenericsDemo {
    static <T> T firstElement(List<T> list) {
        return list.get(0);
    }

    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.set("hello");
        System.out.println("stringBox: " + stringBox.get());

        Box<Integer> intBox = new Box<>();
        intBox.set(42);
        System.out.println("intBox: " + intBox.get());

        Pair<String, Integer> pair = new Pair<>("age", 30);
        System.out.println("pair: " + pair);

        System.out.println("firstElement(List<String>): " + firstElement(List.of("a", "b", "c")));
        System.out.println("firstElement(List<Integer>): " + firstElement(List.of(1, 2, 3)));
    }
}
