package com.java.note.spring.service;

import com.java.note.mybatis.MyUser;
import com.java.note.spring.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author : fang.com
 * @CreatTime : 2021-04-06 14:20
 * @Description :
 * @Version :  0.0.1
 */
@Service
public class UserService {
    final static Random random = new Random();

    @Autowired
    private UserMapper userMapper;

    public Integer insertUserTry() {
        Integer integer = 0;
        try {
            List<MyUser> myUserList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                myUserList.add(new MyUser("超人-" + random.nextInt(100), "成华大道", 23, (double) 23.232));
            }
            integer = userMapper.batchUpdateUser(myUserList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return integer;
    }
}
