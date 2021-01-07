package com.java.note.Jdk.reflaft.one;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-06 12:46
 * @Description : 使用反射比较两个实体对象的属性是否发生变化
 * @Version :  0.0.1
 */
public class CompareObjectPropertyUtil {

    private static final Logger log = LoggerFactory.getLogger(CompareObjectPropertyUtil.class);

    /**
     * 比较两个对象不同的属性并记录返回
     *
     * @param oldObj 旧对象
     * @param newObj 新对象
     * @return java.util.List<com.sdyy.staff.utils.ModifiedPropertyInfo>
     */
    public static <T> List<ModifiedPropertyInfo> getDifferentProperty(T oldObj, T newObj) {
        if (oldObj != null && newObj != null) {
            List<PropertyModelInfo> oldObjectPropertyValue = getObjectPropertyValue(oldObj);
            if (!CollectionUtils.isEmpty(oldObjectPropertyValue)) {
                List<ModifiedPropertyInfo> modifiedPropertyInfos = new ArrayList<>(oldObjectPropertyValue.size());

                List<PropertyModelInfo> newObjectPropertyValue = getObjectPropertyValue(newObj);
                Map<String, Object> objectMap = new HashMap<>(newObjectPropertyValue.size());

                // 获取新对象所有属性值
                for (PropertyModelInfo propertyModelInfo : newObjectPropertyValue) {
                    String propertyName = propertyModelInfo.getPropertyName();
                    Object value = propertyModelInfo.getValue();
                    objectMap.put(propertyName, value);
                }

                for (PropertyModelInfo propertyModelInfo : oldObjectPropertyValue) {
                    String propertyName = propertyModelInfo.getPropertyName();
                    String propertyComment = propertyModelInfo.getPropertyComment();
                    Object value = propertyModelInfo.getValue();
                    if (objectMap.containsKey(propertyName)) {
                        Object newValue = objectMap.get(propertyName);
                        ModifiedPropertyInfo modifiedPropertyInfo = new ModifiedPropertyInfo();
                        if (value != null && newValue != null) {
                            if (!value.equals(newValue)) {
                                modifiedPropertyInfo.setPropertyName(propertyName);
                                modifiedPropertyInfo.setPropertyComment(propertyComment);
                                modifiedPropertyInfo.setOldValue(value);
                                modifiedPropertyInfo.setNewValue(newValue);
                                modifiedPropertyInfos.add(modifiedPropertyInfo);
                            }
                        } else if ((newValue == null && value != null && !StringUtils.isBlank(value.toString())) || (value == null && newValue != null && !StringUtils.isBlank(newValue.toString()))) {
                            modifiedPropertyInfo.setPropertyName(propertyName);
                            modifiedPropertyInfo.setPropertyComment(propertyComment);
                            modifiedPropertyInfo.setOldValue(value);
                            modifiedPropertyInfo.setNewValue(newValue);
                            modifiedPropertyInfos.add(modifiedPropertyInfo);
                        }
                    }
                }
                return modifiedPropertyInfos;
            }
        }
        return Collections.emptyList();
    }


    /**
     * 通过反射获取对象的属性名称、getter返回值类型、属性值等信息
     *
     * @param obj 对象
     * @return java.util.List<com.sdyy.staff.utils.PropertyModelInfo>
     */
    public static <T> List<PropertyModelInfo> getObjectPropertyValue(T obj) {
        if (obj != null) {
            Class<?> objClass = obj.getClass();
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(objClass);
            List<PropertyModelInfo> modelInfos = new ArrayList<>(propertyDescriptors.length);
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
//                if (ignoreProperties == null || !ignoreProperties.contains(fieldName)) {
                if (field.isAnnotationPresent(PropertyName.class)) {
                    PropertyModelInfo propertyModelInfo = new PropertyModelInfo();
                    propertyModelInfo.setPropertyName(fieldName);
                    propertyModelInfo.setReturnType(field.getType());
                    Object fieldValue = getFieldValueByName(fieldName, obj);
                    propertyModelInfo.setValue(fieldValue == null ? "" : fieldValue);
                    modelInfos.add(propertyModelInfo);
                }
            }
            return modelInfos;
        }
        return Collections.emptyList();
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) {
        // 修改前数据
        StaffBaseInfo oldStaff = new StaffBaseInfo("北京市");
        oldStaff.setName("张三");
        oldStaff.setId("11");
        oldStaff.setBirthday("1987-01-02");
        oldStaff.setHighestEducation("本科");
        // 最高学位为字典项
        oldStaff.setHighestDegree("408");
        // 修改后数据
        StaffBaseInfo newStaff = new StaffBaseInfo("山东济南");
        newStaff.setName("张三三");
        newStaff.setId("12");
        newStaff.setBirthday("1988-01-02");
        newStaff.setHighestEducation("博士");
        newStaff.setHighestDegree("308");

        long start = System.currentTimeMillis();
        List<ModifiedPropertyInfo> differentProperty = CompareObjectPropertyUtil.getDifferentProperty(oldStaff, newStaff);
        long end = System.currentTimeMillis();
        System.out.println("本次修改共计发生" + differentProperty.size() + "处变化，共花费 ：" + (end - start) + " ，具体如下所示：");
        differentProperty.forEach(diff -> {
            System.out.println(diff.getPropertyName() + "：" + "修改前为【" + diff.getOldValue() + "】，修改后变为【" + diff.getNewValue() + "】");
        });

    }
}
