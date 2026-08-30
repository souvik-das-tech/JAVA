// Q: Race 10 threads each calling map.merge(key, 1, Integer::sum) 1000
// times total (100 each) on a ConcurrentHashMap<String, Integer>, then print
// the final count to confirm it's exactly correct. Also create a
// CopyOnWriteArrayList, start a thread that adds elements to it while the
// main thread iterates over it concurrently, and confirm no
// ConcurrentModificationException is thrown.

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentCollectionsDemo {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    map.merge("count", 1, Integer::sum);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("ConcurrentHashMap count (expected 1000): " + map.get("count"));

        List<Integer> cowList = new CopyOnWriteArrayList<>(List.of(1, 2, 3));
        Thread writer = new Thread(() -> {
            for (int i = 4; i <= 8; i++) {
                cowList.add(i);
            }
        });
        writer.start();

        int seen = 0;
        for (Integer ignored : cowList) {
            seen++;
        }
        writer.join();
        System.out.println("Iterated snapshot size: " + seen + " (no ConcurrentModificationException)");
        System.out.println("Final list: " + cowList);
    }
}
