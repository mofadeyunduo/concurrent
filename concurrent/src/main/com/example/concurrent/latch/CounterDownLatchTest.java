package com.example.concurrent.latch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CounterDownLatchTest {

    private static final int WORK_SECOND_THRESHOLD = 20;
    private static final int WORKER_SIZE = 10;
    private static final CountDownLatch latch = new CountDownLatch(WORKER_SIZE);

    public static class Worker implements Runnable {

        private final Random r = new Random();

        @Override
        public void run() {
            try {
                int workTime = r.nextInt(WORK_SECOND_THRESHOLD);
                System.out.println("Worker starts working, need time = " + workTime + " s");
                TimeUnit.SECONDS.sleep(workTime);
                latch.countDown();
                latch.await();
                System.out.println("Worker finishes working");
            } catch (Exception e) {
                System.out.println("worker has benn stopped");
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < WORKER_SIZE; i++) {
            executorService.execute(new Worker());
        }
        executorService.shutdown();
    }

}