package com.example.concurrent.car;

public class WheelRoBot extends Robot {

    public WheelRoBot(RobotPool robotPool) {
        super(robotPool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing Wheels");
        this.assembler.car().addWheels();
    }

}