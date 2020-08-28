package com.java.note.spring.mapper;

import com.java.note.redis.bean.User;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/23  16:09
 * @Description
 */
public interface UserMapper {

    User getUserByAge(int age);

}
