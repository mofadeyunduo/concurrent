package com.example.collection.reference;

import java.lang.ref.*;
import java.util.concurrent.TimeUnit;

public class ReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        SoftReference<Object> softReference = new SoftReference<>(new Object());
        WeakReference<Object> weakReference = new WeakReference<>(new Object());
        Object obj = new Object();
        PhantomReference<Object> phantomReference = new PhantomReference<>(obj, referenceQueue);
        System.out.printf("sf: %s, wk: %s, pt: %s\n", softReference.get(), weakReference.get(), referenceQueue.poll());
        obj = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        Reference<?> pollPhantomReference = referenceQueue.poll();
        System.out.printf("sf: %s, wk: %s, pt: %s\n", softReference.get(), weakReference.get(), pollPhantomReference);
        pollPhantomReference.clear();
        System.out.printf("pt: %s\n", pollPhantomReference);
    }

}