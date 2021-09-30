package com.java.note.Jdk.type.one;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-09-27 00:05
 * @description:
 * @question:
 * @link:
 **/
public class Main {

    public static void main(String[] args) {
        final ArrayList<Integer> integers = new ArrayList<>();
        final ArrayList<String> strings = new ArrayList<>();
        final ArrayList<Person> people = new ArrayList<>();
        final ArrayList<Man> men = new ArrayList<>();

        final ArrayList<Integer> integers1 = test(integers);
        final ArrayList<String> strings1 = test(strings);
        final ArrayList<Person> people1 = test(people);
        people1.add(new Person("父类名字", 23));

        testList(integers);

        testListPerson(people1);
        testListPerson(men);
        //报错了
        // testListPerson(strings1);
    }

    public static <T> T test(T t) {
        return t;
    }

    //这样虽然能传入不同类型的list 但是倘若想要操作其中的元素的话 就不好办了只能强转对象
    public static void testList(List list) {
        final Object o = list.get(0);
        final Person person = (Person) o;
    }

    //这样就直接能获取其中的元素进行操作了
    public static void testListPerson(List<? extends Person> list) {
        final Person person = list.get(0);
    }
}
 
