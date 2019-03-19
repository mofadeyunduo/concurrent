package com.example.concurrent.car;

public class EngineRoBot extends Robot {

    public EngineRoBot(RobotPool robotPool) {
        super(robotPool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing engine");
        assembler.car().addEngine();
    }

}