package com.java.note.spring.service;

import com.java.note.mybatis.vo.PeopleDto;
import com.java.note.spring.mapper.PeopleMapper;
import com.java.note.spring.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/8  22:47
 * @Description
 */
@Service
public class PeopleService {
    final static Random random = new Random();
    private PeopleMapper peopleMapper;

    //如果只有一个 有参构造器 那么 构造器上不加@Autowried 也是可以的，一般都得加这个@Autowried注解。
    @Autowired
    public PeopleService(PeopleMapper peopleMapper) {
        this.peopleMapper = peopleMapper;
        System.out.println("PeopleService 构造器 peopleMapper 注入" + peopleMapper);
    }


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

//    @Autowired
//    public void setUserMapper(UserMapper userMapper) {
//        this.userMapper = userMapper;
//        System.out.println("setter 方法注入 userMapper 注入" + userMapper);
//    }

    @PostConstruct
    public void test() {
        System.out.println("PeopleService PostConstruct");
    }

    //  @Autowired(required = true)
//    LogService logService;
//    @Autowired(required = true)
//    UserDao userDao;
//    @Transactional(propagation = Propagation.REQUIRED)//支持当前事务，如果当前没有事务，新建一个事务；
//    @Transactional(propagation = Propagation.SUPPORTS)//有事务则支持，没有则非事务运行；
//    @Transactional(propagation = Propagation.MANDATORY)// 支持当前事务，没有则抛出异常；
//    @Transactional(propagation = Propagation.NEVER)// 以非事务方式运行操作，如果当前存在事务，则抛出异常；
//    @Transactional(propagation = Propagation.NESTED)// 如果当前存在事务，则在嵌套事务内运行；如果当前没有事务，则新建一个事务；
//    @Transactional(propagation = Propagation.REQUIRES_NEW)//新建事务，如果当前存在事务，就把事务挂起；执行当前新建事务完成以后，上下文事务恢复再执行。
    @Transactional(propagation = Propagation.NOT_SUPPORTED)//以非事务方式运行操作，如果当前存在事务，就把当前事务挂起；执行当前新建事务完成以后，上下文事务恢复再执行。
    public void selectPeopleById(Integer id) {
        try {
            System.out.println(peopleMapper.selectById(id));
            insertPeople();
            int i = 10 / 0;
            userService.insertUserTry();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printPeopleMapper();
            printPeopleService();
        }
    }

    @Transactional
    public Integer insertPeople() {
        List<PeopleDto> peopleDtos = new ArrayList<>();
        peopleDtos.add(new PeopleDto("蜘蛛侠-" + random.nextInt(1000), 23));
        peopleDtos.add(new PeopleDto("金刚狼-" + random.nextInt(1000), 23));
        Integer integer = peopleMapper.insertBatch(peopleDtos);
        return integer;
    }

    public void printPeopleMapper() {
        //org.apache.ibatis.binding.MapperProxy@4efc180e
        System.out.println(peopleMapper);
        System.out.println(userMapper);
        //这一步 是谁完成的 ？
        System.out.println(peopleMapper.selectById(1));
    }


    public void printPeopleService() {
        //org.apache.ibatis.binding.MapperProxy@4efc180e
        System.out.println(this);
        System.out.println(userService);
    }


}
