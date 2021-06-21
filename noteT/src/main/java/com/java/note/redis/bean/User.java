package com.java.note.redis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author : mmy
 * @Creat Time : 2020/5/16  12:53
 * @Description
 */
@Data
@AllArgsConstructor
public class User {


    Strings id;
    Strings name;
    int age;


    public User() {
    }

    public User(Strings id, Strings name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public Strings toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
