package com.yjs.dao;

import com.yjs.mapper.HostUnitMapper;
import com.yjs.mapper.TestRangeMapper;
import com.yjs.mapper.UnitCollegeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author : kebukeYi
 * @date :  2021-10-05 16:47
 * @description:
 * @question:
 * @link:
 **/
public class DBDao {

    public static SqlSession session;

    static {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            //当我们单独使用Mybatis时，需要创建一个SqlSessionFactory，然而当 MyBatis 和 Spring整合时，却需要一个SqlSessionFactoryBean, 细节 之前想过吗???
            //怎么实现的mapper 代理对象的  ??
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            session.getConnection().setAutoCommit(true);
            System.out.println(session);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public HostUnitMapper getHostUnitMapper() {
        return session.getMapper(HostUnitMapper.class);
    }

    public TestRangeMapper getTestRangeMapper() {
        return session.getMapper(TestRangeMapper.class);
    }

    public UnitCollegeMapper getUnitCollegeMapper() {
        return session.getMapper(UnitCollegeMapper.class);
    }


}
 
