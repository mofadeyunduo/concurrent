package com.example.concurrent.optimistic;

import java.util.List;

public abstract class ListTester extends Tester<List<Integer>> {

    public ListTester(String testId, int readers, int writers) {
        super(testId, readers, writers);
    }

    @Override
    protected void startReaderAndWriter() {
        for (int i = 0; i < readers; i++) {
            exec.execute(new Reader());
        }
        for (int i = 0; i < writers; i++) {
            exec.execute(new Writer());
        }
    }

    public class Reader extends TestTask {

        private long result;

        @Override
        protected void test() {
            for (long i = 0; i < cycles; i++)
                for (int index = 0; index < containerSize; index++) {
                    result += container.get(index);
                }
        }

        @Override
        protected void putResults() {
            readTime += duration;
            readResult += result;
        }
    }

    public class Writer extends TestTask {

        @Override
        protected void test() {
            for (int i = 0; i < cycles; i++) {
                for (int j = 0; j < containerSize; j++) {
                    container.set(i, writeData[j]);
                }
            }
        }

        @Override
        protected void putResults() {
            writeTime += duration;
        }
    }

}