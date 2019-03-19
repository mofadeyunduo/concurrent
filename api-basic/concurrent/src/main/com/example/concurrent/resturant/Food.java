package com.example.concurrent.resturant;

import java.util.Random;

public class Food {

    private static final String[] foods = {"TUCKY", "ICE_CREAM", "HAMBURGER", "CHIP"};
    private static final Random random = new Random();

    private String name;

    public Food(String name) {
        this.name = name;
    }

    public static Food randomFood() {
        int i = random.nextInt(foods.length);
        return new Food(foods[i]);
    }

    @Override
    public String toString() {
        return name;
    }

}