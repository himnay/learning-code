package com.org.learning.core.stream;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramDeadLock {
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main() {
        // Thread 1 tries to acquire lock1 then lock2
        Thread thread1 = new Thread(() -> {
            try {
                if (lock1.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread 1: Holding lock1...");
                        Thread.sleep(100);
                        System.out.println("Thread 1: Waiting for lock2...");
                        if (lock2.tryLock(2, TimeUnit.SECONDS)) {
                            try {
                                System.out.println("Thread 1: Holding lock1 and lock2...");
                            } finally {
                                lock2.unlock();
                            }
                        } else {
                            System.out.println("Thread 1: Gave up on lock2, backing off...");
                        }
                    } finally {
                        lock1.unlock(); // always release, or the other thread can never proceed
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 2 tries to acquire lock2 then lock1
        Thread thread2 = new Thread(() -> {
            try {
                if (lock2.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread 2: Holding lock2...");
                        Thread.sleep(100);
                        System.out.println("Thread 2: Waiting for lock1...");
                        if (lock1.tryLock(2, TimeUnit.SECONDS)) {
                            try {
                                System.out.println("Thread 2: Holding lock1 and lock2...");
                            } finally {
                                lock1.unlock();
                            }
                        } else {
                            System.out.println("Thread 2: Gave up on lock1, backing off...");
                        }
                    } finally {
                        lock2.unlock(); // always release, or the other thread can never proceed
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // start the threads
        thread1.start();
        thread2.start();
    }
}