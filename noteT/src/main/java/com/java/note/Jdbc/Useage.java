package com.java.note.Jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-06-30 13:10
 * @description :
 * @question :
 * @usinglink :
 **/
public class Useage {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String root = "root";
        String password = "123456";
        final ConnectionFactory connectionFactory = new ConnectionFactory.Default(driverClass, url, root, password);
        final JdbcFacade jdbcFacade = new JdbcFacade(connectionFactory);
        final List<Map<String, Object>> maps = jdbcFacade.executeQuery("select * from my_user");
        for (Map<String, Object> map : maps) {
            System.out.println(map);
        }
        System.out.println();
        final Integer unique = jdbcFacade.executeQueryUnique(rs -> rs.getInt(1), "select count(*) from my_user");
        System.out.println(unique);
        System.out.println();
        final JdbcAction jdbcAction = new JdbcAction(connectionFactory.create());
        final List<Map<String, Object>> mapList = jdbcAction.executeQuery("select * from my_user");
        System.out.println(mapList);
    }
}
 
