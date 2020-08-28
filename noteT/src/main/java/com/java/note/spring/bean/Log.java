package com.java.note.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/10  22:31
 * @Description
 */
@Data
@Entity
@AllArgsConstructor
public class Log {

    @Id
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
