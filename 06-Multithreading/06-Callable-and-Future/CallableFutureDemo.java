// Q: Write a Callable<Integer> that sleeps briefly then returns a computed
// value; submit it and call future.get() to retrieve the result. Write a
// second Callable that throws a checked exception, submit it, and catch the
// ExecutionException from future.get(), printing getCause(). Submit a batch
// of Callables via invokeAll() and print all results.

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableFutureDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<Integer> computeTask = () -> {
            Thread.sleep(50);
            return 21 * 2;
        };
        Future<Integer> future = executor.submit(computeTask);
        try {
            System.out.println("computeTask result: " + future.get());
        } catch (ExecutionException e) {
            System.out.println("unexpected: " + e.getMessage());
        }

        Callable<Integer> failingTask = () -> {
            throw new IllegalStateException("something went wrong");
        };
        Future<Integer> failingFuture = executor.submit(failingTask);
        try {
            failingFuture.get();
        } catch (ExecutionException e) {
            System.out.println("Caught ExecutionException, cause: " + e.getCause().getMessage());
        }

        List<Callable<Integer>> batch = List.of(
                () -> 1 + 1,
                () -> 2 + 2,
                () -> 3 + 3
        );
        List<Future<Integer>> results = executor.invokeAll(batch);
        for (Future<Integer> f : results) {
            try {
                System.out.println("batch result: " + f.get());
            } catch (ExecutionException e) {
                System.out.println("unexpected: " + e.getMessage());
            }
        }

        executor.shutdown();
    }
}
