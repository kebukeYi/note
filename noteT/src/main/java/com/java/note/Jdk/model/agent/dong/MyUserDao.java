package com.java.note.Jdk.model.agent.dong;

/**
 * @Author : fang.com
 * @CreatTime : 2020-11-23 10:39
 * @Description :
 * @Version :  0.0.1
 */
public class MyUserDao implements IUserDao {
    @Override
    public Strings save(Strings name) {
        System.out.println("MyUserDao 我来保存用户" + name);
        return "success-2";
    }

    @Override
    public Strings find(int id) {
        System.out.println("MyUserDao 我来查找用户" + id);
        return "success-2";
    }

}
