package com.example.concurrent.resturant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Restaurant implements Runnable {

    private static final int SLEEP_MILLIS = 100;
    private static final int SEED = 47;
    private static Random random = new Random(SEED);
    public BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
    private List<WaitPerson> waitPersons = new ArrayList<>();
    private List<Chef> chefs = new ArrayList<>();
    private ExecutorService exec;

    public Restaurant(ExecutorService exec, int nWaitPersons, int nChiefs) {
        this.exec = exec;
        for (int i = 0; i < nWaitPersons; i++) {
            WaitPerson waitPerson = new WaitPerson(this);
            waitPersons.add(waitPerson);
            exec.execute(waitPerson);
        }
        for (int i = 0; i < nChiefs; i++) {
            Chef chef = new Chef(this);
            chefs.add(chef);
            exec.execute(chef);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                WaitPerson waitPerson = waitPersons.get(random.nextInt(waitPersons.size()));
                Customer customer = new Customer(waitPerson);
                exec.execute(customer);
                TimeUnit.MILLISECONDS.sleep(SLEEP_MILLIS);
            }
        } catch (InterruptedException e) {
            System.out.println("Restaurant interrupted");
        }
        System.out.println("Restaurant closing");
    }

}