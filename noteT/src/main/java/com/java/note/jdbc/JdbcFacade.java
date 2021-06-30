package com.java.note.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-06-30 12:50
 * @description : 数据库事务的封装类，通过接口回调完成事务封装。
 * @question :
 * @usinglink : https://blog.csdn.net/preferme/article/details/115528877
 **/
public class JdbcFacade {

    /**
     * 数据库操作的封装接口，实现类中添加一个事务中具体的数据库操作。
     * 留给子类去实现
     **/
    public interface TransactionAction<R> {
        //调用子类的执行 方法
        R execute(JdbcAction action) throws SQLException;
    }

    private final ConnectionFactory factory;

    public JdbcFacade(ConnectionFactory factory) {
        this.factory = factory;
    }

    public <R> R executeTransaction(TransactionAction<R> transaction) throws SQLException {
        final Connection connection = factory.create();
        final boolean autoCommit = connection.getAutoCommit();
        if (autoCommit) {
            connection.setAutoCommit(false);
        }
        final JdbcAction jdbcAction = new JdbcAction(connection);
        try {
            final R execute = transaction.execute(jdbcAction);
            connection.commit();
            return execute;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection.getAutoCommit() != autoCommit) {
                connection.setAutoCommit(autoCommit);
            }
        }
    }

    /**
     * 特化操作，一个事务只进行一次更新操作
     **/
    public int executeUpdate(String sql, Object... params) throws SQLException {
        //匿名类实现
        return executeTransaction(action -> action.executeUpdate(sql, params));
    }


    /**
     * 特化操作，一个事务只进行一次查询操作
     **/
    public <R> List<R> executeQuery(ResultSetConverter<R> converter, String sql, Object... params) throws SQLException {
        return executeTransaction(action -> action.executeQuery(converter, sql, params));
    }

    /**
     * 特化操作，一个事务只进行一次查询操作
     **/
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        return executeTransaction(action -> action.executeQuery(sql, params));
    }

    /**
     * 特化操作，一个事务只进行一次查询操作
     **/
    public <R> R executeQueryUnique(ResultSetConverter<R> converter, String sql, Object... params) throws SQLException {
        return executeTransaction(action -> action.executeQueryUnique(converter, sql, params));
    }

    /**
     * 特化操作，一个事务只进行一次查询操作
     **/
    public Map<String, Object> executeQueryUnique(String sql, Object... params) throws SQLException {
        return executeTransaction(action -> action.executeQueryUnique(sql, params));
    }
}
 
