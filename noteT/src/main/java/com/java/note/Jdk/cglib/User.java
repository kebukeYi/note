package com.java.note.Jdk.cglib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @ClassName User
 * @Author kebukeyi
 * @Date 2022/12/5 0:23
 * @Description
 * @Version 1.0.0
 */
public class User {

    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User user = new User();
        user.setName("aiQing");
        user.setId(22);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("age", 23);
        Object object = DynamicBeanUtils.getObject(user, hashMap);
        for (Field field : object.getClass().getDeclaredFields()) {
            System.out.println(field.getName());
        }
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                Object o = method.invoke(object);
                System.out.println("属性值 " + method.getName() + " get方法->" + o);
            }
        }
    }
}
