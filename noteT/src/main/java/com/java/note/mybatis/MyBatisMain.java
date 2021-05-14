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
 * @Description: https://juejin.cn/post/6844903954615107597
 */
public class MyBatisMain {

    public static void main(String[] args) throws IOException {

        String resource = "static/mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //当我们单独使用Mybatis时，需要创建一个SqlSessionFactory，然而当MyBatis和Spring整合时，却需要一个SqlSessionFactoryBean, 细节 之前想过吗???
        //怎么实现的mapper 代理对象的???
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        System.out.println(session);
        try {
            //第一种方法
            //User user = session.selectOne("com.java.note.mapper.UserMapper.getUserByAge", 19);
            //System.out.println(user);
            //第二种
            //获取mapper接口的代理对象
            //这样我们写的每一个Mapper接口都会对应一个MapperFactoryBean，每一个MapperFactoryBean的getObject()方法最终会采用JDK动态代理创建一个对象，
            // 所以每一个Mapper接口最后都对应一个代理对象，这样就实现了Spring和MyBatis的整合
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
