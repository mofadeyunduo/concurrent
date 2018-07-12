package com.example.concurrent.print;

public class PrinterC extends Printer {

    public PrinterC(int count) {
        super(count);
    }

    @Override
    protected void print() {
        System.out.println("C");
    }

}