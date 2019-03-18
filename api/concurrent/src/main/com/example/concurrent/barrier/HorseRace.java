package com.example.concurrent.barrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class HorseRace {

    private final static int HORSE_NUMBER = 5;
    private final static int DISTANCE = 20;
    private final static int REST_SECOND = 2;
    private final static List<Horse> horses = new ArrayList<>();
    private final static Random r = new Random();
    private static volatile boolean end = false;
    private final static CyclicBarrier barrier = new CyclicBarrier(HORSE_NUMBER, () -> {
        horses.forEach(horse -> {
            horse.printTrace();
            if (!end && horse.getSteps() > DISTANCE) {
                end = true;
            }
        });
        try {
            TimeUnit.SECONDS.sleep(REST_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });

    public static void main(String[] args) {
        for (int i = 0; i < HORSE_NUMBER; i++) {
            horses.add(new Horse());
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        horses.forEach(executorService::execute);
        executorService.shutdown();
    }

    public static class Horse implements Runnable {

        private int steps = 0;

        @Override
        public void run() {
            while (!end) {
                steps += r.nextInt(3);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }

        public void printTrace() {
            for (int i = 0; i < steps; i++) {
                System.out.print("*");
            }
            System.out.println();
        }

        public int getSteps() {
            return steps;
        }

    }

}