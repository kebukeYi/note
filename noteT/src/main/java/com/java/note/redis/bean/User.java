package com.java.note.redis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/16  12:53
 * @Description
 */
@Data
@AllArgsConstructor
@Entity
public class User {

    @Id
    String id;
    String name;
    int age;


    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
