package com.java.note.spring.mapper;

import com.java.note.mybatis.MyUser;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/23  16:09
 * @Description
 */
public interface UserMapper {



    List<MyUser> getUserByAge(int age);

    List<MyUser> selectAll();

    Integer batchUpdateUser(@Param("list") List<MyUser> myUserList);

    Integer updateBatch(@Param("list") List<MyUser> myUserList);


}
