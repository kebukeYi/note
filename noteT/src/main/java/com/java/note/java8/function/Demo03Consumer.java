package com.java.note.java8.function;

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

    private static Set<Strings> setName = new LinkedHashSet<Strings>();

    static {
        setName.add("nalke");
        setName.add("mmt");
        setName.add("mme");
    }

    public static void main(Strings[] args) {
        Strings beanName = "nalke";
        //有点话就执行删除操作
        updateManualSingletonNames(
                //执行删除操作
                set -> set.remove(beanName),
                //是否含有
                set -> set.contains(beanName)
        );

        System.out.println(setName);
    }

    public static void updateManualSingletonNames(Consumer<Set<Strings>> action, Predicate<Set<Strings>> condition) {
        if (condition.test(setName)) {
            action.accept(setName);
            Set<Strings> updatedSingletons = new LinkedHashSet<>(setName);
            setName = updatedSingletons;
        }
    }


}
 
