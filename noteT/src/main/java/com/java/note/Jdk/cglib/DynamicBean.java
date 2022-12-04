package com.java.note.Jdk.cglib;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @ClassName DynamicBean
 * @Author kebukeyi
 * @Date 2022/12/5 0:24
 * @Description
 * @Version 1.0.0
 */
public class DynamicBean {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 属性集合
     */
    private BeanMap beanMap;

    public DynamicBean(Class<?> superclass, Map<String, Class<?>> property) {
        this.target = generateBean(superclass, property);
        this.beanMap = BeanMap.create(target);
    }

    public void setValue(String k, Object v) {
        this.beanMap.put(k, v);
    }

    public Object getValue(String k) {
        return this.beanMap.get(k);
    }

    public Object getTarget() {
        return target;
    }

    private Object generateBean(Class<?> supClass, Map<String, Class<?>> property) {
        BeanGenerator beanGenerator = new BeanGenerator();
        if (supClass != null) {
            beanGenerator.setSuperclass(supClass);
        }
        BeanGenerator.addProperties(beanGenerator, property);
        return beanGenerator.create();
    }

}
