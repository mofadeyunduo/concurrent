package com.example.concurrent.print;

public class PrinterB extends Printer {

    public PrinterB(int count) {
        super(count);
    }

    @Override
    protected void print() {
        System.out.println("B");
    }

}