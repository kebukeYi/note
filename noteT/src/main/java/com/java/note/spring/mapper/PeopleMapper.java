package com.java.note.spring.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/24  11:20
 * @Description
 */
public interface PeopleMapper {



    @Select("select * from user  where age=19")
    String selectById(Integer id);

}
