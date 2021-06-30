package com.java.note.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author : kebukeyi
 * @date :  2021-06-29 21:28
 * @description : 数据库连接的工厂类，创建数据库连接
 * @question :
 * @usinglink :
 **/
public interface ConnectionFactory {

    Connection create() throws SQLException;

    class Default implements ConnectionFactory {
        private final String driverClass;
        private final String url;
        private final String user;
        private final String password;

        public Default(String driverClass, String url, String user, String password) {
            this.driverClass = driverClass;
            this.url = url;
            this.password = password;
            this.user = user;
            try {
                //尽管注释了 但是 DriverManager.getConnection() 依然 会从默认的加载驱动中 选择一个 进行加载
                //Class.forName(driverClass);
            } catch (Exception e) {
                throw new IllegalStateException("create jdbc connection failed.", e);
            }
        }

        @Override
        public Connection create() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }
    }
}
