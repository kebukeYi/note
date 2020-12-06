package com.java.note.jvm;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/12  下午 8:25
 * @Description
 */
public class MyStack {

    private Object[] elements;
    private int size = 0;
    private static final int INIT_CAPACITY = 16;

    public MyStack() {
        elements = new Object[INIT_CAPACITY];
    }

    public void push(Object elem) {
        ensureCapacity();
        elements[size++] = elem;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
