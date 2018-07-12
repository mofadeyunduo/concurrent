package com.example.concurrent.print;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrinterTest {

    private static final int COUNT = 10;

    public static void main(String[] args) {
        PrinterA a = new PrinterA(COUNT);
        PrinterB b = new PrinterB(COUNT);
        PrinterC c = new PrinterC(COUNT);

        a.setNext(b);
        b.setNext(c);
        c.setNext(a);

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(a);
        exec.execute(b);
        exec.execute(c);

        synchronized (a) {
            a.notify();
        }
        exec.shutdown();
    }

}