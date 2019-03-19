package com.example.concurrent.car;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CarSimulator {

    public static int SLEEP_SECOND = 7;

    public static void main(String[] args) throws InterruptedException {
        CarQueue cq = new CarQueue(), fq = new CarQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        RobotPool robotPool = new RobotPool();
        exec.execute(new EngineRoBot(robotPool));
        exec.execute(new DriveTrainRoBot(robotPool));
        exec.execute(new WheelRoBot(robotPool));
        exec.execute(new Assembler(cq, fq, robotPool));
        exec.execute(new Reporter(fq));
        exec.execute(new ChassisBuilder(cq));
        TimeUnit.SECONDS.sleep(SLEEP_SECOND);
        exec.shutdown();
    }

}