package com.hand.log.rmi;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 13:02
 * @description:
 * @question:
 * @link:
 **/
public class Evil {

    private int id;
    private String name;
    private Integer age;

    public Evil(int id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String
    toString() {
        return "Evil{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    static {
        System.out.println("Evil 魔鬼侵入服务器~");
    }

}
 
