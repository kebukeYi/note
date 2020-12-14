package com.java.note.mybatis;

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


}
