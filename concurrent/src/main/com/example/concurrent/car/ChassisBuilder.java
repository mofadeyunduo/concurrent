package com.example.concurrent.car;

import java.util.concurrent.TimeUnit;

public class ChassisBuilder implements Runnable {

    private static final int SLEEP_TIME = 500;
    private CarQueue carQueue;
    private int counter = 0;

    public ChassisBuilder(CarQueue cq) {
        this.carQueue = cq;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);

                Car car = new Car(counter++);
                System.out.println("ChassisBuilder build " + car);
                carQueue.put(car);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted: ChassisBuilder");
        }
        System.out.println("Interrupted off");
    }

}