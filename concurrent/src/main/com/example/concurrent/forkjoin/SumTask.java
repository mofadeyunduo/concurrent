package com.example.concurrent.forkjoin;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Integer> {

    private int[] array;
    private int start, end;

    public SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start == 1) {
            return array[start] + array[end];
        } else if (end - start == 0) {
            return array[start];
        } else {
            int middle = (end + start) / 2;
            SumTask s1 = new SumTask(array, start, middle - 1);
            SumTask s2 = new SumTask(array, middle, end);
            invokeAll(s1, s2);
            int s1r = s1.join();
            int s2r = s2.join();
            return s1r + s2r;
        }
    }

}