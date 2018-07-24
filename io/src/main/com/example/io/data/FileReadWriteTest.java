package com.example.io.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileReadWriteTest {

    public static void main(String[] args) throws IOException {
        File r = new File("r.txt");
        File w = new File("w.txt");
        if (!r.exists()) {
            boolean ok = r.createNewFile();
            if (!ok) {
                System.out.println("file created failed");
            }
        }
        if (!w.exists()) {
            boolean ok = w.createNewFile();
            if (!ok) {
                System.out.println("file created failed");
            }
        }

        FileChannel in = new RandomAccessFile(r, "rw").getChannel(),
                out = new RandomAccessFile(w, "rw").getChannel();

        in.write(ByteBuffer.wrap("Hello World\n".getBytes()));
        in.write(ByteBuffer.wrap("This is Piers\n".getBytes()));
        in.close();

        int size = 10;
        in = new FileInputStream(r).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        while ((in.read(buffer) != -1)) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
        in.close();
        out.close();

        buffer.clear();
        out = new RandomAccessFile(w, "rw").getChannel();
        while ((out.read(buffer) != -1)) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
        in.close();

        // read write once
        in = new RandomAccessFile(r, "r").getChannel();
        out = new RandomAccessFile(w, "rw").getChannel();
        in.transferTo(0, in.size(), out);
        in.close();
        out.close();
    }

}