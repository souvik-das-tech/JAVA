// Q: Write a Flag class with a volatile boolean running = true. Start a
// thread that busy-loops while running is true, counting iterations. From
// main, sleep briefly, then set running = false and join the thread,
// printing that it stopped. Also show that a volatile int counter is still
// not safe for concurrent increments by racing 10 threads doing 100
// increments each and printing the (likely incorrect) final count.

class Flag {
    volatile boolean running = true;
}

class VolatileCounter {
    volatile int count = 0;
    void increment() {
        count++;
    }
}

public class VolatileDemo {
    public static void main(String[] args) throws InterruptedException {
        Flag flag = new Flag();
        Thread worker = new Thread(() -> {
            long iterations = 0;
            while (flag.running) {
                iterations++;
            }
            System.out.println("Worker stopped after observing running = false, iterations = " + iterations);
        });
        worker.start();
        Thread.sleep(50);
        flag.running = false;
        worker.join();

        VolatileCounter counter = new VolatileCounter();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) counter.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("volatile counter (expected 1000, may be less): " + counter.count);
    }
}
