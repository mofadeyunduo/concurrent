package com.example.concurrent.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {

    private static final int POOL_SIZE = 3;
    private static final int SLEEP_SECONDS = 3;
    private static final Pool<Task> pool = new Pool<>(Task.class, POOL_SIZE);

    public static class Pool<T> {

        private boolean[] checkout;
        private List<T> items;
        private Semaphore available;

        public Pool(Class<T> tClass, Integer size) {
            checkout = new boolean[size];
            available = new Semaphore(size, true);
            items = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                try {
                    items.add(tClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public void checkin(T item) {
            releaseItem(item);

        }

        public T checkout() {
            try {
                available.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getItem();
        }

        private synchronized T getItem() {
            for (int i = 0; i < checkout.length; i++) {
                if (!checkout[i]) {
                    checkout[i] = true;
                    return items.get(i);
                }
            }
            return null;
        }

        private synchronized boolean releaseItem(T item) {
            int idx = items.indexOf(item);
            if (idx == -1) {
                return false;
            }
            if (!checkout[idx]) {
                return false;
            }
            checkout[idx] = false;
            available.release();
            return true;
        }

    }

    public static class Task implements Runnable {

        public Task() {
            System.out.println("Task init");
        }

        @Override
        public void run() {
            try {
                System.out.println("I am running, id is " + Thread.currentThread().getId());
                TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                System.out.println("I am finished, id is " + Thread.currentThread().getId());
                pool.checkin(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < POOL_SIZE + 2; i++) {
            executorService.execute(pool.checkout());
        }
        executorService.shutdown();
    }

}