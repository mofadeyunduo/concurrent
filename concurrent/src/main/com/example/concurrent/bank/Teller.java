package com.example.concurrent.bank;

import java.util.concurrent.TimeUnit;

public class Teller implements Runnable, Comparable<Teller> {

    private static int count = 0;
    private final int id = count++;

    private CustomerLine customerLine;
    private int customersServed = 0;
    private boolean servingCustomerLine = true;

    public Teller(CustomerLine customerLine) {
        this.customerLine = customerLine;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Customer customer = customerLine.take();
                TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
                synchronized (this) {
                    customersServed++;
                    while (!servingCustomerLine) {
                        this.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(String.format("%s interrupted", this));
        }
        System.out.println(String.format("%s stopped", this));
    }

    @Override
    public synchronized int compareTo(Teller other) {
        return Integer.compare(customersServed, other.customersServed);
    }

    @Override
    public String toString() {
        return String.format("Teller %d ", id);
    }

    public synchronized void doSomethingElse() {
        customersServed = 0;
        servingCustomerLine = false;
    }

    public synchronized void serveCustomLine() {
        customersServed = 0;
        servingCustomerLine = true;
        notifyAll();
    }

    public String shortString() {
        return String.format("T%d", id);
    }

}