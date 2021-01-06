package com.java.note.Jdk.reflaft.one;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-06 12:45
 * @Description : 用于记录修改后发生变化的字段属性信息
 * @Version :  0.0.1
 */
@Data
public class ModifiedPropertyInfo implements Serializable {

    /**
     * 发生变化的属性名称
     */
    private String propertyName;
    /**
     * 发生变化的属性注释
     */
    private String propertyComment;
    /**
     * 修改前的值
     */
    private Object oldValue;
    /**
     * 修改后的值
     */
    private Object newValue;

}
