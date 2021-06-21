package com.java.note.java8.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 16:37
 * @description :
 **/
public class Demo01Stream {

    public static void main(Strings[] args) {
        List<Strings> list = new ArrayList<>();
        Collections.addAll(list, "Java", "C", "Python", "Hadoop", "Spark");
        //当需要对多个元素进行操作(特别是多步操作)的时候，考虑到性能及便利性，我们应该首先拼好一个“模型”步骤 方案，然后再按照方案去执行它。
        list.stream().filter((s) -> s.length() >= 4).filter((s) -> s.length() >= 5).forEach(System.out::print);
    }
}
 
