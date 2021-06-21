package com.java.note.mybatis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : fang.com
 * @CreatTime : 2021-04-06 14:33
 * @Description :
 * @Version :  0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PeopleDto {

    private Integer id;
    private Strings name;
    private Integer age;

    public PeopleDto(Strings name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
