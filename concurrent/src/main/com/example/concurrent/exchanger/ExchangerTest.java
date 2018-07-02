package com.example.concurrent.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {

    private static final int SLEEP_SECOND = 2;
    private static final Exchanger<String> bank = new Exchanger<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Trade());
        try {
            TimeUnit.SECONDS.sleep(SLEEP_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.execute(new Trade());
        executorService.shutdown();
    }

    public static class Trade implements Runnable {

        @Override
        public void run() {
            try {
                String bankName = bank.exchange(Thread.currentThread().getName());
                System.out.println("bank " + Thread.currentThread().getName() + " communicate with:" + bankName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}