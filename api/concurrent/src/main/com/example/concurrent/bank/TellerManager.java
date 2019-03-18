package com.example.concurrent.bank;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TellerManager implements Runnable {

    private final int adjustmentPeriod;
    private static final int RANDOM_SEED = 47;

    private ExecutorService executorService;
    private CustomerLine customerLine;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<>();
    private static Random random = new Random(RANDOM_SEED);

    public TellerManager(ExecutorService executorService, CustomerLine customerLine, int adjustmentPeriod) {
        this.executorService = executorService;
        this.customerLine = customerLine;
        this.adjustmentPeriod = adjustmentPeriod;
        this.workingTellers = new PriorityQueue<>();

        Teller teller = new Teller(customerLine);
        executorService.execute(teller);
        workingTellers.add(teller);
    }

    public void adjustTellerNumber() {
        if (customerLine.size() / workingTellers.size() > 2) {
            if (tellersDoingOtherThings.size() > 0) {
                Teller teller = tellersDoingOtherThings.remove();
                teller.serveCustomLine();
                workingTellers.offer(teller);
                return;
            }
            Teller teller = new Teller(customerLine);
            executorService.execute(teller);
            workingTellers.add(teller);
            return;
        }
        if (workingTellers.size() > 1 && customerLine.size() / workingTellers.size() < 2) {
            reassignOneTeller();
        }
        if (customerLine.size() == 0) {
            while (workingTellers.size() > 1) {
                reassignOneTeller();
            }
        }
    }

    private void reassignOneTeller() {
        Teller teller = workingTellers.poll();
        if (teller == null) {
            return;
        }
        teller.doSomethingElse();
        tellersDoingOtherThings.offer(teller);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber();
                System.out.print(customerLine + "{");
                for (Teller teller : workingTellers) {
                    System.out.print(teller.shortString() + " ");
                }
                System.out.println("}");
            }
        } catch (InterruptedException e) {
            System.out.println(String.format("%s interrupted", this));
        }
        System.out.println(String.format("%s stopped", this));
    }

}