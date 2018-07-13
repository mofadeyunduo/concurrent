package com.example.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;

public class SumMain {

    private final static int END = 2;
    private final static int THREAD_SIZE = 5;

    public static void main(String[] args) {
        int[] array = new int[END + 1];
        for (int i = 0; i <= END; i++) {
            array[i] = i;
        }
        SumTask sumTask = new SumTask(array, 0, array.length - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_SIZE);
        Integer sum = forkJoinPool.invoke(sumTask);
        System.out.println(sum);
    }

}