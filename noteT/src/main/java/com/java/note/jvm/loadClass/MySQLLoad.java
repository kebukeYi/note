package com.java.note.jvm.loadClass;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * @author : kebukeyi
 * @date :  2021-06-21 13:07
 * @description :
 * @question :
 * @usinglink :
 **/
public class MySQLLoad {


    public static void main(Strings[] args) throws SQLException {
        Strings url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
//        String url = "jdbc:mysql://39.96.63.187:3306/test?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
        Connection connection = DriverManager.getConnection(url, "root", "123456");
        // null -> BootstrapClassLoader 根加载器
        System.out.println("DriverManager.class.getClassLoader() : " + DriverManager.class.getClassLoader());
        //DriverManager 内部的 getDrivers 是由 下层 ApplicationClassLoader 加载 破坏了自下而上的双亲委派模式
        //俗称  jdbc spi
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        //数据库厂商把驱动类注册在Java的接口上, 当Java调用这些接口时,
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println("driver :  " + driver);
            //sun.misc.Launcher$AppClassLoader  下层应用加载器
            System.out.println("driver.getClass().getClassLoader() : " + driver.getClass().getClassLoader());
        }
        System.out.println("connection : " + connection);

    }
}
 
