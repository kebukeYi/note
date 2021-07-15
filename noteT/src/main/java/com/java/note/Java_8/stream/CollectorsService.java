package com.java.note.Java_8.stream;

import com.java.note.Java_8.bean.Person;

import java.util.stream.Collectors;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  15:47
 * @Description
 */
public class CollectorsService {

    public static void main(String[] args) {

        // Collectors: 是一个工具类，提供了若干个方法，返回一个Collec tor接口的实现类对象
        // toList, toSet， toMap
        // maxBy，minBy:
        // maxBy: 通过指定的规则，获取流中最大的元素
        // minBy:通过指定的规则，获取流中最小的元素
//        System.out.println(Data.getPersonList().stream().collect(Collectors.maxBy((ele1, ele2) -> ele1.getScore() - ele2.getScore())));
//        System.out.println(Data.getPersonList().stream().collect(Collectors.minBy((ele1, ele2) -> ele1.getScore() - ele2.getScore())));


        //joining : 合并 将流中的元素，以字符串的形式拼接起来
        //将集合中的person姓名拼接成一个字符串
//        String name = Data.getPersonList().stream().map(Person::getName).collect(Collectors.joining());
//        System.out.println(name);//小明小兰小红小崩小绿小d小g小r小r

//        String name = Data.getPersonList().stream().map(Person::getName).collect(Collectors.joining(","));//分隔符
//        System.out.println(name);//小明,小兰,小红,小崩,小绿,小d,小g,小r,小r

//        String name = Data.getPersonList().stream().map(Person::getName).collect(Collectors.joining(",","[","]"));//分隔符
//        System.out.println(name);//[小明,小兰,小红,小崩,小绿,小d,小g,小r,小r]


        //summingInt : 将流中的每一个元素映射成int类型的元素，最后进行求和

        //将集合中的所有人的成绩求和
//        System.out.println(Data.getPersonList().stream().collect(Collectors.summingInt(Person::getScore)));//759

        //平均值
//        System.out.println(Data.getPersonList().stream().collect(Collectors.averagingInt(Person::getScore)));//IntSummaryStatistics{count=9, sum=759, min=65, average=84.333333, max=98}

        //获取数据的描述信息
        System.out.println(Data.getPersonList().stream().collect(Collectors.summarizingInt(Person::getScore)));//IntSummaryStatistics{count=9, sum=759, min=65, average=84.333333, max=98}


    }


}
