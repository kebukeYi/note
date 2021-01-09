package com.java.note.Jdk.reflect.one;

import com.alibaba.fastjson.JSONObject;
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
    public static <T> List<ModifiedPropertyInfo> getDifferentProperty(T oldObj, T newObj, String... ignoreProperties) {
        if (oldObj != null && newObj != null) {
            // 比较全部字段
            if (ignoreProperties == null || ignoreProperties.length > 0) {
                if (oldObj.equals(newObj)) {
                    return Collections.emptyList();
                }
            }
            //获取old实体类的键值对
            List<PropertyModelInfo> oldObjectPropertyValue = getObjectPropertyValue(oldObj, ignoreProperties);
            //如果不为空
            if (!CollectionUtils.isEmpty(oldObjectPropertyValue)) {
                //记录两个实体发生变化属性的list
                List<ModifiedPropertyInfo> modifiedPropertyInfos = new ArrayList<>(oldObjectPropertyValue.size());
                //获取new实体类的键值对
                List<PropertyModelInfo> newObjectPropertyValue = getObjectPropertyValue(newObj, ignoreProperties);
                Map<String, Object> objectMap = new HashMap<>(newObjectPropertyValue.size());

                // 获取新对象所有属性值
                for (PropertyModelInfo propertyModelInfo : newObjectPropertyValue) {
                    String propertyName = propertyModelInfo.getPropertyName();
                    Object value = propertyModelInfo.getValue();
                    objectMap.put(propertyName, value);
                }

                //遍历旧属性键值对
                for (PropertyModelInfo propertyModelInfo : oldObjectPropertyValue) {
                    String propertyName = propertyModelInfo.getPropertyName();
                    String propertyComment = propertyModelInfo.getPropertyComment();
                    Object oldValue = propertyModelInfo.getValue();

                    //newMap是否存在
                    if (objectMap.containsKey(propertyName)) {
                        Object newValue = objectMap.get(propertyName);
                        ModifiedPropertyInfo modifiedPropertyInfo = new ModifiedPropertyInfo();
                        //两值都存在变化的话
//                        if (oldValue != null && newValue != null) {
                        //两值是否一样
                        if (!oldValue.equals(newValue)) {
                            //记录对应的属性
                            modifiedPropertyInfo.setPropertyName(propertyName);
                            modifiedPropertyInfo.setPropertyComment(propertyComment);
                            modifiedPropertyInfo.setOldValue(oldValue);
                            modifiedPropertyInfo.setNewValue(newValue);
                            modifiedPropertyInfos.add(modifiedPropertyInfo);
                        }
//                        }
                        //当其中一个从有值变为了null 或者反之
//                        else if ((newValue == null && oldValue != null && !StringUtils.isBlank(oldValue.toString())) || (oldValue == null && newValue != null && !StringUtils.isBlank(newValue.toString()))) {
//                            modifiedPropertyInfo.setPropertyName(propertyName);
//                            modifiedPropertyInfo.setPropertyComment(propertyComment);
//                            modifiedPropertyInfo.setOldValue(oldValue);
//                            modifiedPropertyInfo.setNewValue(newValue);
//                            modifiedPropertyInfos.add(modifiedPropertyInfo);
//                        }
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
     * @param obj              对象
     * @param ignoreProperties 可忽略比对的属性
     * @return java.util.List<com.sdyy.staff.utils.PropertyModelInfo>
     */
    public static <T> List<PropertyModelInfo> getObjectPropertyValue(T obj, String... ignoreProperties) {
        if (obj != null) {
            Class<?> objClass = obj.getClass();
            //获得此类的各个属性的多个描述
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(objClass);
            //自定义各个属性值的实体类
            List<PropertyModelInfo> modelInfos = new ArrayList<>(propertyDescriptors.length);
            //各个属性数组
            Field[] fields = objClass.getDeclaredFields();
            //忽视属性的集合
            List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
            //开始遍历
            for (Field field : fields) {
                //设置可编辑
                field.setAccessible(true);
                String fieldName = field.getName();
                //如果此属性没有被忽视
                if (ignoreList == null || !ignoreList.contains(fieldName)) {
                    //新建自己的属性实体类
                    PropertyModelInfo propertyModelInfo = new PropertyModelInfo();
                    propertyModelInfo.setPropertyName(fieldName);
                    propertyModelInfo.setReturnType(field.getType());
                    //得到属性值
                    Object fieldValue = getFieldValueByName(fieldName, obj);
                    // 通过自定义注解拿到属性注释
                    if (field.isAnnotationPresent(PropertyName.class)) {
                        PropertyName annotation = field.getAnnotation(PropertyName.class);
                        //额外的形容词
                        propertyModelInfo.setPropertyComment(annotation.name());
                        // 如果是字典项，则将code转换成name
                        // 注意：获取字典项这部分代码需要结合自己的业务实现，我这里是从静态文件加载进来的
                        if (annotation.isDict()) {
                            List<DictDTO> dictList = JSONObject.parseArray(JSONObject.toJSONString(DictionaryConstant.dictionaryMap.get(fieldName)), DictDTO.class);
                            Object finalFieldValue = fieldValue;
                            if (finalFieldValue != null && !"".equals(finalFieldValue)) {
                                Optional<DictDTO> first = dictList.stream().filter(dictDTO -> dictDTO.getValue().equals(String.valueOf(finalFieldValue))).findFirst();
                                if (first.isPresent()) {
                                    fieldValue = first.get().getText();
                                }
                            }
                        }
                    }
                    //开始保存字典值
                    propertyModelInfo.setValue(fieldValue == null ? "" : fieldValue);
                    //一一保存实体类的数值
                    modelInfos.add(propertyModelInfo);
                }
            }
            return modelInfos;
        }
        return Collections.emptyList();
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            //id中的 i 大写 Id
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            //getId();
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
        StaffBaseInfo oldStaff = new StaffBaseInfo("");
        oldStaff.setName("张三");
        oldStaff.setBirthday("1987-01-02");
        oldStaff.setBirthPlace("北京市");
        oldStaff.setHighestDegree("本科");
        // 最高学位为字典项
        oldStaff.setHighestDegree("408");
        // 修改后数据
        StaffBaseInfo newStaff = new StaffBaseInfo("");
        newStaff.setName("张三");
        newStaff.setId("22");
        newStaff.setBirthday("1987-01-02");
        newStaff.setBirthPlace("山东济南");
        newStaff.setHighestDegree("博士");
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
