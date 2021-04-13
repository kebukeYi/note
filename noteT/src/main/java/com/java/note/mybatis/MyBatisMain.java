package com.java.note.mybatis;

import com.java.note.mybatis.vo.MyUser;
import com.java.note.spring.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/23  15:59
 * @Description
 */
public class MyBatisMain {

    public static void main(String[] args) throws IOException {

        String resource = "static/mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        System.out.println(session);
        try {
            //第一种方法
            //   User user = session.selectOne("com.java.note.mapper.UserMapper.getUserByAge", 19);
            //  System.out.println(user);
            //第二种
            // 获取mapper接口的代理对象
            UserMapper userMapper = session.getMapper(UserMapper.class);

            List<MyUser> userList = userMapper.getUserByAge(24);
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(userList.get(i));
            }

            List<MyUser> myUserList = new ArrayList<>();
            myUserList.add(new MyUser(1, "张思", "西门胡同23号", 24, 456.123d));
            myUserList.add(new MyUser(2, "范县", "儋州23号", 23, 456.123d));
            myUserList.add(new MyUser(3, "三台子", "陈唐关23号", 26, 456.123d));
//            Integer rows = userMapper.batchUpdateUser(myUserList);
            Integer rows = userMapper.updateBatch(myUserList);
//            System.out.println(rows);
            System.out.println("===========================================");
            List<MyUser> myUserList1 = userMapper.selectAll();
            System.out.println(myUserList1);
        } finally {
            //session.close();
        }
    }


}
