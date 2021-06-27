package com.java.note.Jdk.model.agent.dong;

/**
 * @Author : fang.com
 * @CreatTime : 2020-11-23 10:19
 * @Description :
 * @Version :  0.0.1
 */
public class UserDao implements IUserDao {

    @Override
    public String save(String name) {
        //int i =1/0;用于测试异常通知
        System.out.println("模拟： 保存用户 ! " + name);
        return "success-1";
    }

    @Override
    public String find(int id) {
        System.out.println("查询用户 " + id);
        return "success-1";
    }
}
