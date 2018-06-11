package main.com.example.concurrent.number;

import java.util.concurrent.Callable;

public class NumberPrinter implements Callable<Counter> {

    private final int start;
    private final int end;

    public NumberPrinter(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Counter call() {
        long begin = System.currentTimeMillis();
        for (int i = start; i < end; i++) {
            System.out.println(i);
        }
        long over = System.currentTimeMillis();
        return new Counter(begin, over);
    }

}
