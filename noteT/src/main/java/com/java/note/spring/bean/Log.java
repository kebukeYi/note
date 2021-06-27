package com.java.note.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author : mmy
 * @Creat Time : 2020/6/10  22:31
 * @Description
 */
@Data

@AllArgsConstructor
public class Log {


    int id;
    String time;
    String data;

    public Log(String time, String data) {
        this.data = data;
        this.time = time;
    }

    public Log() {
    }


}
