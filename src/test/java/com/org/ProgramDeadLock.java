package com.org;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramDeadLock {
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    void main() {
        // Thread 1 tries to acquire lock1 then lock2
        Thread thread1 = new Thread(() -> {
            try {
                if (lock1.tryLock(2, TimeUnit.SECONDS)) {
                    System.out.println("Thread 1: Holding lock1...");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    System.out.println("Thread 1: Waiting for lock2...");
                    if (lock2.tryLock(2, TimeUnit.SECONDS)) {
                        System.out.println("Thread 1: Holding lock1 and lock2...");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Thread 2 tries to acquire lock2 then lock1
        Thread thread2 = new Thread(() -> {
            try {
                if (lock2.tryLock(2, TimeUnit.SECONDS)) {
                    System.out.println("Thread 2: Holding lock2...");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    System.out.println("Thread 2: Waiting for lock1...");
                    if (lock1.tryLock(2, TimeUnit.SECONDS)) {
                        System.out.println("Thread 2: Holding lock1 and lock2...");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // start the threads
        thread1.start();
        thread2.start();
    }
}