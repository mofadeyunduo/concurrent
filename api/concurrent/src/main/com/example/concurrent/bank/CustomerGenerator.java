package com.example.concurrent.bank;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CustomerGenerator implements Runnable {

    private static final int SEED = 47;
    private static final int SLEEP_THRESHOLD_MILLIS = 10;
    private static final int SERVICE_TIME_THRESHOLD_MILLIS = 1000;

    private CustomerLine customerLine;
    private Random random = new Random(SEED);


    public CustomerGenerator(CustomerLine customerLine) {
        this.customerLine = customerLine;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(SLEEP_THRESHOLD_MILLIS));
                customerLine.put(new Customer(random.nextInt(SERVICE_TIME_THRESHOLD_MILLIS)));
            }
        } catch (InterruptedException e) {
            System.out.println("CustomerGenerator interrupted");
        }
        System.out.println("CustomerGenerator stopped");
    }

}