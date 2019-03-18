package com.example.concurrent.optimistic;

import java.util.ArrayList;

public class CountingIntegerList extends ArrayList<Integer> {

    private static int count = 0;

    public CountingIntegerList(int max) {
        for (int i = 0; i < max; i++) {
            this.add(count++);
        }
    }

}