// Q: Build a parallel word-frequency counter. Given a List<String> of
// "documents," submit one Callable<Void> per document to a fixed thread
// pool, each merging its words into a shared ConcurrentHashMap<String,
// Integer> via merge(word, 1, Integer::sum). Wait for all tasks to finish
// (invokeAll), then print the aggregated counts. Also compute the same
// result sequentially and assert the two maps are equal, to verify the
// parallel version is correct.

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelWordCounter {
    static List<String> documents = List.of(
            "the quick brown fox jumps over the lazy dog",
            "the dog barks at the fox in the yard",
            "quick foxes and lazy dogs rarely meet",
            "the yard is quiet when the fox leaves",
            "dogs and foxes are both quick animals"
    );

    static Map<String, Integer> countSequential() {
        Map<String, Integer> counts = new java.util.HashMap<>();
        for (String doc : documents) {
            for (String word : doc.split(" ")) {
                counts.merge(word, 1, Integer::sum);
            }
        }
        return counts;
    }

    static Map<String, Integer> countParallel() throws InterruptedException {
        Map<String, Integer> counts = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Void>> tasks = documents.stream()
                .<Callable<Void>>map(doc -> () -> {
                    for (String word : doc.split(" ")) {
                        counts.merge(word, 1, Integer::sum);
                    }
                    return null;
                })
                .toList();

        executor.invokeAll(tasks);
        executor.shutdown();
        return counts;
    }

    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> sequential = countSequential();
        Map<String, Integer> parallel = countParallel();

        System.out.println("Sequential result: " + sequential);
        System.out.println("Parallel result:   " + parallel);
        System.out.println("Results match: " + sequential.equals(parallel));
    }
}
