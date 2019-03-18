package com.example.concurrent.optimistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CopyOnWriteArrayListTest extends ListTester {

    public CopyOnWriteArrayListTest(int readers, int writers) {
        super("CopyOnWriteArrayList", readers, writers);
    }

    @Override
    protected List<Integer> containerInit() {
        return Collections.synchronizedList(new ArrayList<>(new CountingIntegerList(containerSize)));
    }

}