package com.java.note.java8.stream;

import com.java.note.java8.bean.Person;

import java.util.stream.Stream;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  11:32
 * @Description
 */
public class Program {

    public static void creatPerson() {
        //实例对象  一 一赋值
        Person person = new Person("ming", 20, 98);

        //流式举例
        Person people = new Person();
        people.setAge(12).setName("ll").setScore(98);//拿到对象实例一直可以操作

    }


    /*
    中间操作
     */
    public static void creatProcess() {
        Stream<Person> stream = Data.getPersonList().stream();

        //中间操作 1  Fifler 过滤
//        stream.filter(ele -> ele.getScore() > 80).forEach(System.out::print);

        //中间操作 2 distinct 去重
        //去重规则   1. 先判断hashCode     2.再判断equals()
//        stream.distinct().forEach(System.out::println);


        //中间操作 3  sorted 得有排序规则 需要排序的类实验Comparable
//        stream.sorted().forEach(System.out::println);
//        stream.sorted((ele1, ele2) -> ele1.getAge() - ele2.getAge()).forEach(System.out::println);


        //中间操作 4  limit   只取流中前指定的元素量
//        stream.limit(3).forEach(System.out::println);


        //中间操作 5 skip 跳过 前几个元素
//        stream.skip(2).forEach(System.out::println);

        //举例 从7个元素中 获取第2个到地5个元素
//        stream.limit(5).skip(1).forEach(System.out::println);


        //中间操作  6  map  元素映射 ，用指定的元素替换流中的元素
        //操作 将流中的对象替换成他们的姓名
//        stream.map(ele->ele.getName()).forEach(System.out::println);
        stream.map(ele -> ele.getScore() >= 80 ? ele : ele.getName()).forEach(System.out::println);

    }

    /*
    最终操作
     */
    public static void creatCollections() {
        //集合 = 数据源
        //流式方法返回的都是在这个数据源 进行过滤等操作
        //更像一个迭代器 有序一层一层的操作

        //1.获取数据源 (stream流 只能用一次 ) : stream has already been operated upon or closed
        //  Stream.of(Data.getPersonList());
        Stream<Person> stream = Data.getPersonList().stream();

        //2.中间操作处理：过滤  排序

        //最终操作 1 Collect
        //3.对流中的数据整合 转list集合、set、Map
//        List<Person> personList = stream.collect(Collectors.toList());
//        System.out.println(personList);

//        Set<Person> personSet = stream.collect(Collectors.toSet());
//        System.out.println(personSet);

//        Map<String, Integer> map = stream.collect(Collectors.toMap(Person::getName, ele -> ele.getScore()));
//        System.out.println(map);

        //最终操作 2   reduce
//        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 11);
//        Optional optional = stream1.reduce((n1, n2) -> n1 + n2);
//        System.out.println(optional.get());
//两两个元素进行操作
//        Person temp = new Person();
//        Optional<Person> person = stream.reduce((n1, n2) -> temp.setScore(n1.getScore() + n2.getScore()));
//        System.out.println(person.get().getScore());


        //最终操作 3 Max Min
//       Person max= stream.max((ele1,ele2)->ele1.getScore()-ele2.getScore()).get();
//        Person min = stream.min((ele1, ele2) -> ele1.getAge() - ele2.getAge()).get();
//        System.out.println(min);

        //最终操作 4  筛选条件 anyMatch allMatch noneMatch
        //根据任意一个成绩是否符合条件的
//        boolean r = stream.anyMatch(ele -> ele.getScore() > 100);
//        System.out.println(r);

        //判断所有成员是否都符合这个条件
//        boolean all = stream.anyMatch(ele -> ele.getScore() >= 60);
//        System.out.println(all);

        //是否 不存在小于60的
//        boolean res = stream.noneMatch(ele -> ele.getScore() <= 60);
//        System.out.println(res);


        //最终操作  count  查询几个数量
//        long count = stream.count();
//        System.out.println(count);


        //最终操作  forEach 跟集合中的forEach 一样
//        stream.forEach(ele -> System.out.println(ele));
//        stream.forEach(System.out::println);

    }

    /*
    综合案例
    */
    public static void Exercise() {
        /*
        所有及格的学生信息
        所有及格的学生姓名
        班级的前3名(按照成绩)
        班级的3-10名(按照成绩)
        所有不及格的学生平均成绩
        将及格的学生，按照成绩升序输出所有信息
        班级学生的总分
         */
        Stream<Person> stream = Data.getPersonList().stream();

//        List<Person> personList = stream.filter(ele -> ele.getScore() > 94).collect(Collectors.toList());
//        System.out.println(personList);

//        List<String> stringList = stream.filter(ele -> ele.getScore() > 94).map(ele -> ele.getName()).collect(Collectors.toList());
//        System.out.println(stringList);

//        List<Person> list = stream.sorted().limit(3).collect(Collectors.toList());
//        System.out.println(list);

//        stream.sorted().limit(10).skip(2).collect(Collectors.toList()).forEach(System.out::println);

//        Person temp = new Person();
//        stream.filter(ele -> ele.getScore() < 90).reduce((ele1, ele2) -> temp.setScore(ele1.getScore() + ele2.getScore()));
//        long count = stream.filter(ele -> ele.getScore() < 90).count();
//        float ave = temp.getScore() / (float) count;
//        System.out.println(ave);

//        stream.filter(ele -> ele.getScore() > 90).sorted((ele1, ele2) -> ele1.getScore() - ele2.getScore()).forEach(System.out::println);

        Person person = new Person();
        stream.reduce((ele1, ele2) -> person.setScore(ele1.getScore() + ele2.getScore()));
        System.out.println(person.getScore());

    }

/*
并行流
 */
    public static void RowStream() {
        Stream<Person> stream = Data.getPersonList().stream();
        /*
        并行流 引入线程 并并行解决问题
        */
//两种方式
//        Data.getPersonList().stream().parallel();
//        Data.getPersonList().parallelStream();

        /*
        咱么变化尼？
         */
//        long strat = System.currentTimeMillis();
//        LongStream.rangeClosed(0L, 50000000000L).reduce(Long::sum);
//        LongStream.rangeClosed(0L, 50000000000L).parallel().reduce(Long::sum);
//        long end = System.currentTimeMillis();
//        System.out.println(end - strat);

        // find first
//        System.out.println(stream.findFirst());//串行流
//        System.out.println(stream.parallel().findFirst());//并行流

        // find any
//        System.out.println(stream.findAny());//和串行流一样
//        System.out.println(Data.getPersonList().parallelStream().findAny());//最先抢到时间片的线程对待的对象


        /*
        映射  map
         */
//        String[] array = {"ming", "yang", "hao", "hei"};
        //获取所有的字符
//        System.out.println(Arrays.stream(array).map(ele -> ele.split("")).collect(Collectors.toList()));//4个数组 每个数组中都是[m,i,n,g]
//        Arrays.stream(array).map(ele -> ele.split("")).forEach(ele -> System.out.println(ele.length));
        //对 流中的数据 进行扁平化处理
//        System.out.println(Arrays.stream(array).map(ele -> ele.split("")).flatMap(Arrays::stream).collect(Collectors.toList()));//就是把[m,i,n,g]中每一个元素都当作是一个流 然后再合并成一个流

    }


    public static void main(String[] args) {
//        creatCollections();
//        creatProcess();
//        Exercise();
        RowStream();
    }

}
