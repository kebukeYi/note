package com.java.note.Jdk.generic.two;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-07 12:22
 * @Description :
 * @Version :  0.0.1
 */
public class Pair<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
