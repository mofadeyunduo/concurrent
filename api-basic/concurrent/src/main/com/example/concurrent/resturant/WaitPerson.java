package com.example.concurrent.resturant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WaitPerson implements Runnable {

    private static int count = 0;
    private final Restaurant restaurant;
    public BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<Plate>();
    private int id = count++;

    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void placeOrder(Customer customer, Food food) throws InterruptedException {
        restaurant.orders.put(new Order(customer, this, food));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Plate plate = filledOrders.take();
                System.out.println(this + " received " + plate + " delivering to " + plate.getOrder().getCustomer());
                plate.getOrder().getCustomer().deliver(plate);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " interrupted");
        }
        System.out.println(this + " off duty");
    }

    @Override
    public String toString() {
        return "WaitPerson " + id;
    }

}