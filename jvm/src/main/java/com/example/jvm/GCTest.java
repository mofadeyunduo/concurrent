package com.example.jvm;

import java.util.concurrent.TimeUnit;

public class GCTest {

    private static final Integer ONE_MB = 1024 * 1024;

    private Object instance = null;
    private byte[] bigSize = new byte[2 * ONE_MB];

    /**
     * VMargs: -XX:+PrintGCDetails
     */
    private static void gc() {
        GCTest gcTest = new GCTest();
        GCTest gcTest1 = new GCTest();
        gcTest.instance = gcTest1;
        gcTest1.instance = gcTest;

        gcTest = null;
        gcTest1 = null;

        System.gc();
    }

    private static void save() throws InterruptedException {
        FinalizeClass finalizeClass = new FinalizeClass();
        finalizeClass = null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);

        if (FinalizeClass.saveHook != null) {
            System.out.println(FinalizeClass.class.getName() + " is alive.");
        } else {
            System.out.println(FinalizeClass.class.getName() + " is dead.");
        }

        FinalizeClass.saveHook = null;
        finalizeClass = null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        if (FinalizeClass.saveHook != null) {
            System.out.println(FinalizeClass.class.getName() + " is alive.");
        } else {
            System.out.println(FinalizeClass.class.getName() + " is dead.");
        }
    }

    /**
     * VMArgs: -XX:+UseSerialGC -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * VMArgs: -XX:+UseSerialGC -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=314528
     */
    public static void testAllocation() {
        byte[] allocation1,  allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * ONE_MB];
        allocation2 = new byte[2 * ONE_MB];
        allocation3 = new byte[2 * ONE_MB];
        allocation4 = new byte[4 * ONE_MB];
    }

    public static void main(String[] args) throws InterruptedException {
//        gc();
//        save();
        testAllocation();
    }

    private static class FinalizeClass {

        private static FinalizeClass saveHook;

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            FinalizeClass.saveHook = this;
            System.out.println(this.getClass() + " saved itself.");
        }

    }

}