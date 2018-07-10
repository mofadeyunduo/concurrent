package com.example.concurrent.resturant;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Chef implements Runnable {

    private static final int SLEEP_MILLIS = 500;
    private static int count = 0;
    private static Random random = new Random(47);
    private final int id = count++;
    private final Restaurant restaurant;

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Order order = restaurant.orders.take();
                Food requestItem = order.item();
                TimeUnit.MILLISECONDS.sleep(random.nextInt(SLEEP_MILLIS));
                Plate plate = new Plate(order, requestItem);
                order.getWaitPerson().filledOrders.put(plate);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " interrupted");
        }
        System.out.println(this + " off duty");
    }

}