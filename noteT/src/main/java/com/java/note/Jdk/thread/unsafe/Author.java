package com.java.note.Jdk.thread.unsafe;

import lombok.Data;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:07
 * @Description
 */
@Data
public class Author {
    String name;
    int seq;

    public Author() {
    }

    public Author(String name, int seq) {
        this.name = name;
        this.seq = seq;
    }
}
