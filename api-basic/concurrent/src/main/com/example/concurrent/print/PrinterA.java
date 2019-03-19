package com.example.concurrent.print;

public class PrinterA extends Printer {

    public PrinterA(int count) {
        super(count);
    }

    @Override
    protected void print() {
        System.out.println("A");
    }

}