package cn.gof.bridge;

import java.sql.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 16:10
 * @description:
 * @question:
 * @link:
 **/
public class JDBCDemo {


    public static void getConnection() throws ClassNotFoundException, SQLException {
        //如果我们想要把 MySQL 数据库换成 Oracle 数据库，只需要把第一行代码中的
        // com.mysql.jdbc.Driver 换成 oracle.jdbc.driver.OracleDriver 就可以了
        //执行Class.forName("全类名")的时候不会执行构造方法，但是会执行静态方法
        Class.forName("com.mysql.jdbc.Driver");//加载及注册JDBC驱动程序
        String url = "jdbc:mysql://localhost:3306/test?user=root&password=123456";
        Connection con = DriverManager.getConnection(url);
        Statement stmt = con.createStatement();
        String query = "select * from test";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            rs.getString(1);
            rs.getInt(2);
        }
    }
}
 
