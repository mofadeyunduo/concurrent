package com.example.concurrent.optimistic;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Tester<C> {

    public static ExecutorService exec = Executors.newCachedThreadPool();
    protected String testId;
    protected int reps = 10, cycles = 1000, containerSize = 1000, readers = 0, writers = 0;
    protected long readTime, writeTime, readResult;
    protected CountDownLatch endLatch;
    protected C container;
    protected int[] writeData;

    public Tester(String testId, int readers, int writers) {
        this.testId = testId;
        this.readers = readers;
        this.writers = writers;
        writeData = new int[containerSize];
        Arrays.fill(writeData, new Random().nextInt());
        for (int i = 0; i < reps; i++) {
            runTest();
            readTime = 0;
            writeTime = 0;
        }
    }

    public static void initMain() {
        System.out.printf("%-27s %-14s %-14s\n", "Type", "Read Time", "Write Time");
    }

    protected abstract void startReaderAndWriter();

    protected abstract C containerInit();

    public void runTest() {
        endLatch = new CountDownLatch(readers + writers);
        container = containerInit();
        startReaderAndWriter();

        try {
            endLatch.await();
        } catch (InterruptedException e) {
            System.out.println("endLatch interrupted");
        }

        System.out.printf("%-27s %-14s %-14s\n", testId, readTime, writeTime);
        if (readTime != 0 && writeTime != 0) {
            System.out.printf("%-27s %-14d\n", "readTime + writeTime =", readTime + writeTime);
        }
    }

    public abstract class TestTask implements Runnable {

        protected long duration;

        protected abstract void test();

        protected abstract void putResults();

        @Override
        public void run() {
            long start = System.nanoTime();
            test();
            duration = System.nanoTime() - start;
            synchronized (Tester.this) {
                putResults();
            }
            endLatch.countDown();
        }

    }

}