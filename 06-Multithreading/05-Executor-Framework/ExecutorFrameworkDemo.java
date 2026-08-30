// Q: Create a fixed thread pool of 3 and submit 10 Runnable tasks that print
// their task number and thread name. Call shutdown(), then
// awaitTermination() with a timeout, printing whether all tasks completed.
// Then try submitting a task after shutdown() and catch the
// RejectedExecutionException.

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class ExecutorFrameworkDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("All tasks completed within timeout? " + finished);

        try {
            executor.submit(() -> System.out.println("late task"));
        } catch (RejectedExecutionException e) {
            System.out.println("Caught: task rejected after shutdown");
        }
    }
}
