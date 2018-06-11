package main.com.example.concurrent.number;

public class Counter {

    private Long start;
    private Long end;

    public Counter() {
    }

    public Counter(long begin, long over) {
        this.start = begin;
        this.end = over;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public long time() {
        return end - start;
    }
}