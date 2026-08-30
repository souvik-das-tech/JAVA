// Q: Create a thread by extending Thread, and another by implementing
// Runnable, start both, and print Thread.currentThread().getName() from
// each. Call .run() directly (not .start()) on a third Runnable and confirm
// via getName() it ran on the main thread. Print a Thread's state right
// after creation and right after start(). Try calling start() twice on the
// same Thread and catch the IllegalThreadStateException.

class MyThread extends Thread {
    public void run() {
        System.out.println("MyThread running in " + Thread.currentThread().getName());
    }
}

class MyTask implements Runnable {
    public void run() {
        System.out.println("MyTask running in " + Thread.currentThread().getName());
    }
}

public class ThreadLifecycleDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new MyThread();
        t1.start();

        Thread t2 = new Thread(new MyTask());
        t2.start();

        t1.join();
        t2.join();

        MyTask directTask = new MyTask();
        directTask.run(); // runs on main thread, not a new one

        Thread t3 = new Thread(() -> {});
        System.out.println("State right after creation: " + t3.getState());
        t3.start();
        System.out.println("State right after start: " + t3.getState());
        t3.join();

        try {
            t3.start();
        } catch (IllegalThreadStateException e) {
            System.out.println("Caught: cannot start a thread twice");
        }
    }
}
