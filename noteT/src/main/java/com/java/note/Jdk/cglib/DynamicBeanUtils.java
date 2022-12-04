package com.java.note.Jdk.cglib;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.testng.collections.Maps;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DynamicBeanUtils
 * @Author kebukeyi
 * @Date 2022/12/5 0:35
 * @Description
 * @Version 1.0.0
 */
public class DynamicBeanUtils {

    public static Object getObject(Object dest, Map<String, Object> properties) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] propertyDescriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class<?>> propertyMap = Maps.newHashMap();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            if (!"class".equals(descriptor.getName())) {
                propertyMap.put(descriptor.getName(), descriptor.getPropertyType());
            }
        }
        properties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
        DynamicBean dynamicBean = new DynamicBean(dest.getClass(), propertyMap);
        Set<Map.Entry<String, Class<?>>> entries = propertyMap.entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            if (!properties.containsKey(entry.getKey())) {
                dynamicBean.setValue(entry.getKey(), propertyUtilsBean.getNestedProperty(dest, entry.getKey()));
            } else {
                dynamicBean.setValue(entry.getKey(), properties.get(entry.getKey()));
            }
        }
        return dynamicBean.getTarget();
    }

}
