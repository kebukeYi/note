package com.java.note.mybatis;

import com.java.note.mybatis.dto.StatisticalBrandDto;
import com.java.note.mybatis.vo.StatisticalBrandVo;
import com.java.note.spring.mapper.UserMapper;
import com.java.note.redis.bean.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
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
//            User user = session.selectOne("com.java.note.mapper.UserMapper.getUserByAge", 19);
//            System.out.println(user);

            //第二种
            // 获取mapper接口的代理对象
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User user1 = userMapper.getUserByAge(19);
            System.out.println(user1);

            StatisticalBrandDto statisticalBrandDto = StatisticalBrandDto.builder().cityName("北京").brandNameChar("A").pagesize(100).offset(0).build();
//            UserMapper userMapper = session.getMapper(UserMapper.class);
            List<StatisticalBrandVo> statisticalBrandList = userMapper.getStatisticalBrandList(statisticalBrandDto);
            System.out.println(statisticalBrandList);
        } finally {
            session.close();
        }
    }


}
