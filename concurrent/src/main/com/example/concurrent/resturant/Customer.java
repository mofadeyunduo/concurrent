package com.example.concurrent.resturant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

public class Customer implements Runnable {

    private static final int FOOD_SIZE = 2;
    private static int count = 0;
    private final int id = count++;
    private final WaitPerson waitPerson;
    private SynchronousQueue<Plate> plateSetting = new SynchronousQueue<>();

    public Customer(WaitPerson waitPerson) {
        this.waitPerson = waitPerson;
    }

    public void deliver(Plate p) throws InterruptedException {
        plateSetting.put(p);
    }

    @Override
    public void run() {
        List<Food> foods = new ArrayList<>();
        for (int i = 1; i < FOOD_SIZE; i++) {
            foods.add(Food.randomFood());
        }

        for (Food food : foods) {
            try {
                waitPerson.placeOrder(this, food);
                System.out.println(this + " eating " + plateSetting.take());
            } catch (InterruptedException e) {
                System.out.println(this + " waiting for " + foods.stream().map(Food::toString).collect(Collectors.joining(" ")) + " interrupted");
            }
        }
        System.out.println(this + " finished meal, leaving");
    }

    @Override
    public String toString() {
        return "Customer " + id;
    }

}