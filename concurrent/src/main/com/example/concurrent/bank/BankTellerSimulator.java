package com.example.concurrent.bank;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankTellerSimulator  {

    private static int MAX_LINE_SIZE = 50;
    private static int ADJUST_PERIOD = 1000;

    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        CustomerLine customerLine = new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customerLine));
        exec.execute(new TellerManager(exec, customerLine, ADJUST_PERIOD));
        System.out.println("Press 'Enter' to quit");
        System.in.read();
        exec.shutdown();
    }

}