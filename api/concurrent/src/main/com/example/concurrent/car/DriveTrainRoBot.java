package com.example.concurrent.car;

public class DriveTrainRoBot extends Robot {

    public DriveTrainRoBot(RobotPool robotPool) {
        super(robotPool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing driveTrain");
        assembler.car().addDriveTrain();
    }

}