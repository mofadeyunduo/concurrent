package com.example.concurrent.car;

import java.util.HashSet;
import java.util.Set;

public class RobotPool {

    private Set<Robot> robots = new HashSet<>();

    public synchronized void add(Robot r) {
        robots.add(r);
        notifyAll();
    }

    public synchronized void hire(Class<? extends Robot> robotType, Assembler d) throws InterruptedException {
        for (Robot r : robots) {
            if (r.getClass().equals(robotType)) {
                robots.remove(r);
                r.assignAssembler(d);
                r.engage();
                return;
            }
        }
        wait();
        hire(robotType, d);
    }

    public synchronized void release(Robot r) {
        add(r);
    }

}