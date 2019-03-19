package com.example.concurrent.resturant;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestaurantSimulator {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int nWaitPersons = 5;
        int nChefs = 2;
        Restaurant restaurant = new Restaurant(executorService, nWaitPersons, nChefs);
        executorService.execute(restaurant);
        System.in.read();
        executorService.shutdown();
    }
}