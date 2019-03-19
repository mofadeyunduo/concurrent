package com.example.concurrent.optimistic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedArrayListTest extends ListTester {

    public SynchronizedArrayListTest(int readers, int writers) {
        super("SynchronizedArrayList", readers, writers);
    }

    @Override
    protected List<Integer> containerInit() {
        return new CopyOnWriteArrayList<>(new CountingIntegerList(containerSize));
    }

}