package com.example.io.data;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest {

    public static void main(String[] args) throws IOException {
        String file = "LineNumberFile.txt";
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.writeChars("Hello World");
        raf.writeChars("P");
        raf.close();

        raf = new RandomAccessFile(file, "rw");
        raf.seek(22);
        char str = raf.readChar();
        System.out.println(str);
        raf.close();
    }

}