package com.example.concurrent.print;

public abstract class Printer implements Runnable {

    private volatile int count;
    private Printer next;

    public Printer(int count) {
        this.count = count;
    }

    protected abstract void print();


    public void setNext(Printer next) {
        this.next = next;
    }

    @Override
    public synchronized void run() {
        try {
            while (count > 0) {
                this.wait();
                print();
                synchronized (next) {
                    next.notify();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}