package com.java.note.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-06-29 21:36
 * @description : 数据库操作的封装类，每个函数都可以完成一次数据库的访问
 * @question :
 * @usinglink :
 **/
public class JdbcAction {

    private final Connection conn;

    public JdbcAction(Connection conn) {
        this.conn = conn;
    }

    /**
     * 更新操作
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        //有了连接 就执行sql
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            if (params != null || params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i, params[i]);
                }
            }
            return preparedStatement.executeUpdate();
        }
    }

    /**
     * 查询操作
     */
    public <R> List<R> executeQuery(ResultSetConverter<R> converter, String sql, Object... params) throws SQLException {
        //有了连接 就执行sql
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            if (params != null || params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i, params[i]);
                }
            }
            try (final ResultSet resultSet = preparedStatement.executeQuery();) {
                List<R> result = new ArrayList<>();
                System.out.println("resultSet.getMetaData() : " + resultSet.getMetaData());
                converter.setResultSetMetaData(resultSet.getMetaData());
                while (resultSet.next()) {
                    final R convert = converter.convert(resultSet);
                    result.add(convert);
                }
                return result;
            }
        }
    }

    /**
     * 查询操作
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        return executeQuery(new ResultSetConverter.Default(), sql, params);
    }

    /**
     * 查询操作，只有一条记录返回
     */
    public <R> R executeQueryUnique(ResultSetConverter<R> converter, String sql, Object... params) throws SQLException {
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            if (params != null && params.length > 0) {
                for (int i = 1; i <= params.length; i++) {
                    pstm.setObject(i, params[i - 1]);
                }
            }
            try (ResultSet rs = pstm.executeQuery()) {
                converter.setResultSetMetaData(rs.getMetaData());
                if (rs.next()) {
                    R record = converter.convert(rs);
                    return record;
                }
                return null;
            }
        }
    }

    /**
     * 查询操作，只有一条记录返回
     */
    public Map<String, Object> executeQueryUnique(String sql, Object... params) throws SQLException {
        return executeQueryUnique(new ResultSetConverter.Default(), sql, params);
    }


}
 
