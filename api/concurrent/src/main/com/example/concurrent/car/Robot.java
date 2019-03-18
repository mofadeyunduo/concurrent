package com.example.concurrent.car;

import java.util.concurrent.BrokenBarrierException;

public abstract class Robot implements Runnable {

    protected Assembler assembler;
    private boolean engage;
    private RobotPool robotPool;

    public Robot(RobotPool robotPool) {
        this.robotPool = robotPool;
    }

    protected abstract void performService();

    public Robot assignAssembler(Assembler assembler) {
        this.assembler = assembler;
        return this;
    }

    public synchronized void engage() {
        this.engage = true;
        notifyAll();
    }

    protected synchronized void powerDown() throws InterruptedException {
        engage = false;
        assembler = null;
        robotPool.release(this);
        while (!engage) {
            wait();
        }
    }

    public void setAssembler(Assembler assembler) {
        this.assembler = assembler;
    }

    @Override
    public void run() {
        try {
            powerDown();
            while (!Thread.interrupted()) {
                performService();
                assembler.cyclicBarrier().await();
                powerDown();
            }
        } catch (InterruptedException e) {
            System.out.println("Robot interrupted");
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Robot off");
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }
}