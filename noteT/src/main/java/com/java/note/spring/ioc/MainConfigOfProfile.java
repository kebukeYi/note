package com.java.note.spring.ioc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/13  11:30
 * @Description Spring为我们提供的可以根据当前环境。动态的激活和切换一系列组件的功能
 * @Profile("test") 加入环境标识 只有激活这个环境标号 才能使用
 * 1. 启动命令 ：  -Dspring.profiles.active=test
 * 2. 利用代码：先用无参构造器 构造出容器   、 applicationContext. getEnvironment(). setActiveProfiles("test","dev");、再注册数据源配置类
 */
//@Configuration
public class MainConfigOfProfile {

//    @Profile("test")
//    @Bean("dataSourceTest")
    public DataSource dataSourceTest() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("123456");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/baidumap");
        comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        return comboPooledDataSource;
    }

//    @Profile("dev")
//    @Bean("dataSourceDev")
    public DataSource dataSourceDev() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("123456");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/man");
        comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        return comboPooledDataSource;
    }

//    @Profile("sim")
//    @Bean("dataSourceProd")
    public DataSource dataSourceProd() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("123456");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/mysqlstudy");
        comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        return comboPooledDataSource;
    }


}
