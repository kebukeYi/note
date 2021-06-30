package com.java.note.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-06-29 21:32
 * @description : 查询结果的转换器
 * @question :
 * @usinglink :
 **/
public interface ResultSetConverter<R> {

    /**
     * 转换结果函数
     */
    R convert(ResultSet resultSet) throws SQLException;

    /**
     * 转换前会调用该函数，传递查询结果的列信息 如果需要的话，可以重写改函数
     */
    default void setResultSetMetaData(ResultSetMetaData rsmd) throws SQLException {
    }

    /**
     * 提供默认的转换器，将查询结果的记录转换成 HashMap 对象
     */
    class Default implements ResultSetConverter<Map<String, Object>> {

        private HashSet<String> columNames = new HashSet<>();

        @Override
        public void setResultSetMetaData(ResultSetMetaData rsmd) throws SQLException {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columNames.add(rsmd.getColumnName(i));
            }

        }

        @Override
        public Map<String, Object> convert(ResultSet resultSet) throws SQLException {
            HashMap<String, Object> record = new HashMap<>();
            for (String name : columNames) {
                record.put(name, resultSet.getObject(name));
            }
            return record;
        }
    }


}
 
