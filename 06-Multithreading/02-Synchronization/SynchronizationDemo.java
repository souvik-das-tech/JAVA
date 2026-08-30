// Q: Write a Counter with a non-synchronized increment(), run 1000
// increments across 10 threads (100 each), join them all, and show the
// final count is unpredictably less than 1000. Then fix it by making
// increment() synchronized, rerun, and confirm the count is always exactly
// 1000. Also show a ReentrantLock-based version with lock()/unlock() in a
// try/finally.

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class UnsafeCounter {
    int count = 0;
    void increment() {
        count++;
    }
}

class SafeCounter {
    int count = 0;
    synchronized void increment() {
        count++;
    }
}

class LockCounter {
    int count = 0;
    private final Lock lock = new ReentrantLock();
    void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}

public class SynchronizationDemo {
    static void runThreads(Runnable task) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) task.run();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
    }

    public static void main(String[] args) throws InterruptedException {
        UnsafeCounter unsafe = new UnsafeCounter();
        runThreads(unsafe::increment);
        System.out.println("Unsafe counter (expected 1000, likely less): " + unsafe.count);

        SafeCounter safe = new SafeCounter();
        runThreads(safe::increment);
        System.out.println("Synchronized counter: " + safe.count);

        LockCounter locked = new LockCounter();
        runThreads(locked::increment);
        System.out.println("ReentrantLock counter: " + locked.count);
    }
}
