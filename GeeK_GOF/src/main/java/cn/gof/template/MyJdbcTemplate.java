package cn.gof.template;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static cn.hutool.db.ds.pooled.PooledDataSource.getDataSource;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 18:30
 * @description: JdbcTemplate 通过回调的机制，将不变的执行流程抽离出来，放到模板方法 execute() 中，
 * 将可变的部分设计成回调 StatementCallback，由用户来定制
 * @question:
 * @link:
 **/
public class MyJdbcTemplate {

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, new RowMapperResultSetExtractor<T>(rowMapper));
    }

    public <T> T query(final String sql, final ResultSetExtractor<T> rse) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        Assert.notNull(rse, "ResultSetExtractor must not be null");

        class QueryStatementCallback implements StatementCallback<T>, SqlProvider {
            @Override
            public T doInStatement(Statement stmt) throws SQLException {
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery(sql);
                    ResultSet rsToUse = rs;
                    return rse.extractData(rsToUse);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }

            @Override
            public String getSql() {
                return sql;
            }
        }

        return execute(new QueryStatementCallback());
    }

    //模板方法
    public <T> T execute(StatementCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");
        Connection con = DataSourceUtils.getConnection(getDataSource());
        Statement stmt = null;
        try {
            Connection conToUse = con;

            stmt = conToUse.createStatement();
            applyStatementSettings(stmt);
            Statement stmtToUse = stmt;

            //
            T result = action.doInStatement(stmtToUse);
            handleWarnings(stmt);
            return result;
        } catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            JdbcUtils.closeStatement(stmt);
            stmt = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
        return null;
    }

    private <T> Object getSql(StatementCallback<T> action) {
        return null;
    }

    private Event getExceptionTranslator() {
        return null;
    }

    private void handleWarnings(Statement stmt) {
    }

    private void applyStatementSettings(Statement stmt) {
    }

}
 
