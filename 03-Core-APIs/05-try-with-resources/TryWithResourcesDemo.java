// Q: Write a class MyResource implementing AutoCloseable, whose close()
// method prints "Closed". Use two MyResource instances in one
// try-with-resources block, printing something inside the try body too, to
// show both are closed automatically in reverse declaration order. Then
// throw an exception inside the try body and show close() still runs before
// the exception propagates (catch it in main and print its message).

class MyResource implements AutoCloseable {
    String name;

    MyResource(String name) {
        this.name = name;
    }

    @Override
    public void close() {
        System.out.println("Closed " + name);
    }
}

public class TryWithResourcesDemo {
    public static void main(String[] args) {
        try (MyResource r1 = new MyResource("R1");
             MyResource r2 = new MyResource("R2")) {
            System.out.println("Using resources");
        }

        try (MyResource r3 = new MyResource("R3")) {
            System.out.println("About to throw");
            throw new RuntimeException("boom");
        } catch (RuntimeException e) {
            System.out.println("Caught after close: " + e.getMessage());
        }
    }
}
