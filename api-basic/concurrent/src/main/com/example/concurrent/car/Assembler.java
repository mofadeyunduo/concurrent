package com.example.concurrent.car;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Assembler implements Runnable {

    private static final int PARTIES = 4;

    private CarQueue chassisQueue, finishingQueue;
    private Car car;
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(PARTIES);
    private RobotPool robotPool;


    public Assembler(CarQueue cq, CarQueue fq, RobotPool robotPool) {
        this.chassisQueue = cq;
        this.finishingQueue = fq;
        this.robotPool = robotPool;
    }

    public Car car() {
        return car;
    }

    public CyclicBarrier cyclicBarrier() {
        return cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                car = chassisQueue.take();

                robotPool.hire(EngineRoBot.class, this);
                robotPool.hire(DriveTrainRoBot.class, this);
                robotPool.hire(WheelRoBot.class, this);
                cyclicBarrier.await();

                finishingQueue.put(car);
            }
        } catch (InterruptedException e) {
            System.out.println("Assembler interrupted");
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Assembler off");
    }

}