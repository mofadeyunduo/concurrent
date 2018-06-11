package main.com.example.concurrent.number;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 单线程、多线程 System.out.println 由于 synchronized 产生的性能问题测试
 */
public class NumberMain {

    public static void main(String[] args) throws Exception {
        int start = 0, end = 1000000, threadSize = 100;

        // single
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        long singleTime = executorService.submit(new NumberPrinter(start, end)).get().time();

        // multi
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadSize);
        int part = (end - start) / threadSize;
        List<Future<Counter>> multiTimeFutures = new ArrayList<>(threadSize);
        for (int i = 0; i < threadSize; i++) {
            multiTimeFutures.add(executor.submit(new NumberPrinter(i * part, (i + 1) * part - 1)));
        }
        // select min、 max
        Counter multiCounter = new Counter(Long.MAX_VALUE, Long.MIN_VALUE);
        for (Future<Counter> counterFuture : multiTimeFutures) {
            Counter c = counterFuture.get();
            if (c.getStart() < multiCounter.getStart()) {
                multiCounter.setStart(c.getStart());
            }
            if (c.getEnd() > multiCounter.getEnd()) {
                multiCounter.setEnd(c.getEnd());
            }
        }

        System.out.println("single thread time: " + singleTime);
        System.out.println("multi thread time: " + multiCounter.time());
        // fixme: something block here
    }

}