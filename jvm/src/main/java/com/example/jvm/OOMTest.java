package com.example.jvm;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OOMTest {

    private final static Integer ONE_MB = 1024 * 1024;

    static class OOMObject {
    }

    /**
     * VMArgs: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemory -XX:HeapDumpPath=e:/
     * Result: jhat -port 7401 -J-Xmx4G java_pid14912.hprof
     */
    public static void heapOOM() {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }

    /**
     * VMArgs： -Xss128k
     */
    public static void stackOOM() {
        stackOOM();
    }

    /**
     * WARNING： windows will get fake death
     * VMArgs: -Xss2m
     */
    public static void stackOOMMultiThread() {
        while (true) {
            new Thread(() -> {
                while (true) {

                }
            }).start();
        }
    }

    /**
     * Will produce OOM before jdk7
     * VMArgs: -XX:PermSize=10M -XX:MaxPermSize=10m
     */
    public static void methodAreaOOM() {
        List<String> strings = new ArrayList<>();
        int i = 0;
        while (true) {
            strings.add(String.valueOf(i++).intern());
        }
    }

    /**
     * Will produce OOM in jdk7
     * VMArgs: -XX:PermSize=10m -XX:MaxPermSize=10m
     */
    public static void methodAreaOOMJDK7() {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invokeSuper(objects, objects));
            enhancer.create();
        }
    }

    /**
     * Perm Gen removed in jdk8, replaced by Metaspace. Will produce OOM in jdk8.
     * VMArgs: -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
     */
    public static void metaSpaceOOMJDK8() {
        methodAreaOOMJDK7();
    }

    /**
     * VMArgs: -XX:MaxDirectMemorySize=10m
     */
    public static void directMemoryODOM() throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(ONE_MB);
        }
    }

    /**
     * Test for String Pool changed in jdk7.
     * Print false and true.
     */
    public static void internTest() {
        String str1 = new StringBuilder().append("ja").append("va").toString();
        System.out.println(str1 == str1.intern());

        String str2 = new StringBuilder().append("hello").append("world").toString();
        System.out.println(str2 == str2.intern());
    }

    public static void main(String[] args) throws IllegalAccessException {
//        heapOOM();
//        stackOOM();
//        stackOOMMultiThread();
//        methodAreaOOM();
//        internTest();
//        methodAreaOOMJDK7();
//        metaSpaceOOMJDK8();
        directMemoryODOM();
    }

}
