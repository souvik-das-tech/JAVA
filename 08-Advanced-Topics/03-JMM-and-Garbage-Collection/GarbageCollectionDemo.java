// Q: Write a method that creates a large number of short-lived objects in a
// loop (e.g. 1,000,000 small objects) and discards them immediately, then
// print Runtime.getRuntime().totalMemory()/freeMemory() before and after
// calling System.gc(), to observe (loosely — GC timing isn't guaranteed)
// that memory is reclaimed. Also demonstrate a reference-cycle: two objects
// referencing each other, both set to null, still eligible for collection
// (just print a message noting this, since eligibility isn't directly
// observable from code).

public class GarbageCollectionDemo {
    static class Node {
        Node other;
        int[] payload = new int[1000];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1_000_000; i++) {
            Object temp = new Object(); // eligible for GC immediately after each iteration
        }

        Runtime runtime = Runtime.getRuntime();
        System.out.println("Total memory: " + runtime.totalMemory());
        System.out.println("Free memory before gc(): " + runtime.freeMemory());
        System.gc();
        System.out.println("Free memory after gc() request: " + runtime.freeMemory());

        Node a = new Node();
        Node b = new Node();
        a.other = b;
        b.other = a; // reference cycle between a and b
        a = null;
        b = null; // no GC root reaches either node anymore -> both eligible for collection despite the cycle
        System.out.println("a and b now unreachable (cycle doesn't prevent collection)");
    }
}
