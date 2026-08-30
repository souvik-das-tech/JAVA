// Q: Create an ArrayList<String> and a LinkedList<String>. Add a few
// elements to both. Use ListIterator on the ArrayList to insert a new
// element in the middle while iterating. Use the LinkedList as a stack
// (push/pop) and then as a queue (offer/poll), printing its state after
// each operation.

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ListDemo {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");

        ListIterator<String> lit = arrayList.listIterator();
        while (lit.hasNext()) {
            String s = lit.next();
            if (s.equals("b")) {
                lit.add("inserted");
            }
        }
        System.out.println("ArrayList after insert: " + arrayList);

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.push("x");
        linkedList.push("y");
        linkedList.push("z");
        System.out.println("LinkedList as stack: " + linkedList);
        System.out.println("pop() -> " + linkedList.pop());
        System.out.println("LinkedList after pop: " + linkedList);

        linkedList.clear();
        linkedList.offer("x");
        linkedList.offer("y");
        linkedList.offer("z");
        System.out.println("LinkedList as queue: " + linkedList);
        System.out.println("poll() -> " + linkedList.poll());
        System.out.println("LinkedList after poll: " + linkedList);
    }
}
