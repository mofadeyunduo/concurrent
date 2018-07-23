package com.example.io.data;

import java.io.*;
import java.util.Scanner;

public class DataStreamTest {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int left = sc.nextInt(),
                right = sc.nextInt(),
                sum = left + right;

        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis);

        DataOutputStream dos = new DataOutputStream(pos);
        dos.writeInt(sum);
        DataInputStream dis = new DataInputStream(pis);
        int readSum = dis.readInt();

        System.out.println(readSum);

        dos.writeUTF("Hello World");
        String hello = dis.readUTF();
        System.out.println(hello);

        dis.close();
        dos.close();
    }

}