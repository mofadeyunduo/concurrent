package com.example.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnsafeTest {

    private Integer[] array = {1, 2, 3};

    public Optional<Unsafe> getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return Optional.of((Unsafe) f.get(null));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void testArray(Unsafe unsafe) {
        int third = unsafe.arrayBaseOffset(array.getClass()) + 2 * unsafe.arrayIndexScale(array.getClass());
        if (unsafe.compareAndSwapObject(array, third, array[2], 4)) {
            System.out.println("cas success " +Arrays.stream(array).map(Objects::toString).collect(Collectors.joining(",")));
        } else {
            System.out.println("cas failed at {1, 2, 3} -> {1, 2, 4}");
        }
    }

    public static void main(String[] args) {
        UnsafeTest unsafeTest = new UnsafeTest();
        Unsafe unsafe = unsafeTest.getUnsafe().orElseThrow(NullPointerException::new);
        unsafeTest.testArray(unsafe);
    }

}