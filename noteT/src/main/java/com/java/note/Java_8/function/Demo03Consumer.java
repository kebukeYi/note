package com.java.note.Java_8.function;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 15:23
 * @description :
 **/
public class Demo03Consumer {

    private static Set<String> setName = new LinkedHashSet<String>();

    static {
        setName.add("nalke");
        setName.add("mmt");
        setName.add("mme");
    }

    public static void main(String[] args) {
        String beanName = "nalke";
        //有点话就执行删除操作
        updateManualSingletonNames(
                //执行删除操作
                set -> set.remove(beanName),
                //是否含有
                set -> set.contains(beanName)
        );

        System.out.println(setName);
    }

    public static void updateManualSingletonNames(Consumer<Set<String>> action, Predicate<Set<String>> condition) {
        if (condition.test(setName)) {
            action.accept(setName);
            Set<String> updatedSingletons = new LinkedHashSet<>(setName);
            setName = updatedSingletons;
        }
    }


}
 
