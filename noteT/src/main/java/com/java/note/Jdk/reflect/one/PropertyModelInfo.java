package com.java.note.Jdk.reflect.one;

import lombok.Data;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-06 12:43
 * @Description : 利用反射机制拿到需要比较的字段属性信息
 * @Version :  0.0.1
 */
@Data
public class PropertyModelInfo {

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性注释
     */
    private String propertyComment;

    /**
     * 属性值
     */
    private Object value;

    /**
     * 返回值类型
     */
    private Class<?> returnType;


}
