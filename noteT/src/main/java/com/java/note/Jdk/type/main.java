package com.java.note.Jdk.type;

import com.java.note.spring.iocc.A;
import com.java.note.spring.iocc.B;

import java.util.ArrayList;
import java.util.List;

public class main {

    //可以使用无限通配符 因为限定了上界
    public static void getCountPeople(List<? extends People> people) {
        int scores = 0;
        for (People person : people) {
            scores += person.getScore();
        }
    }

    public static void getCountPeople2(List<People> people) {
        int scores = 0;
        for (People person : people) {
            scores += person.getScore();
        }
    }

    public <K extends A, E extends B> E getCountPeople3(K k, E e) {
        return e;
    }

    //下界通配符
    private static <T> void test(List<? super T> dst, List<T> src) {
        for (T t : src) {
            //父类list加父类
            dst.add(t);
        }
    }


    public static void main(String[] args) {
        List<Man> manArrayList = new ArrayList<>();
        List<Woman> womanList = new ArrayList<>();
        List<People> peopleList = new ArrayList<>();

        getCountPeople(manArrayList);

        //为何这个会会报错？
        //getCountPeople2(womanArrayList);

        test(peopleList, womanList);

    }
}
