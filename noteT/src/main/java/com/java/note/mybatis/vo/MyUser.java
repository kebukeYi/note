package com.java.note.mybatis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUser {

    private Integer id;
    private String name;
    private String address;
    private Integer age;
    private Double money;

    public MyUser(String name, String address, Integer age, Double money) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.money = money;
    }
}
