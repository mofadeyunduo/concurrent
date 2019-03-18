package com.example.io.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {

    public static void main(String[] args) throws IOException {
        File f = new File("test.txt");
        if (!f.exists()) {
            boolean ok = f.createNewFile();
            if (!ok) {
                System.out.println("file created failed");
            }
        }

        FileChannel fc = new FileOutputStream(f).getChannel();
        fc.write(ByteBuffer.wrap("Hello World\n".getBytes()));
        fc.close();

        fc = new RandomAccessFile("test.txt", "rw").getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap("Hello Java\n".getBytes()));
        fc.close();

        fc = new FileInputStream(f).getChannel();
        int size = 1024;
        ByteBuffer buff = ByteBuffer.allocate(size);
        fc.read(buff);
        buff.flip();
        while (buff.hasRemaining()) {
            System.out.print((char) buff.get());
        }
    }

}