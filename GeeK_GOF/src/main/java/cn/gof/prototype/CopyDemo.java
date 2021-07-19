package cn.gof.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 14:49
 * @description:
 * @question:
 * @link:
 **/
public class CopyDemo {

    public static void assignmentCopy() throws CloneNotSupportedException {
        final User user = new User(1, "user");
        final Person user_person = new Person(0, "user_person");
        user.setPerson(user_person);
        user_person.setValue(22);
        user.setAge(2);
        //直接赋值
        final User clone = user;
        clone.setName("clone");
        clone.getPerson().setPerson("clone_person");
        System.out.println("clone " + clone);
        System.out.println("user " + user);
    }

    public static void shallowCopy() throws CloneNotSupportedException {
        final User user = new User(1, "user");
        final Person user_person = new Person(0, "user_person");
        user.setAge(2);
        user.setPerson(user_person);
        user_person.setValue(33);
        //浅拷贝
        //int string 不被拷贝去
        final User clone = (User) user.clone();
        user.setName("ok???");
        //clone.setName("clone");
        clone.setAge(3);
        clone.getPerson().setPerson("clone_person");
        System.out.println("clone " + clone);
        System.out.println("user " + user);
    }

    public static void deepCopy() throws CloneNotSupportedException {
        final User user = new User(1, "user");
        final Person user_person = new Person(0, "user_person");
        user.setAge(2);
        user.setPerson(user_person);
        //深拷贝 引用对象也拷贝
        final User clone = (User) user.clone();
        clone.setName("clone");
        System.out.println("clone " + clone);
        System.out.println("user " + user);
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        assignmentCopy();
        System.out.println("============");
        shallowCopy();
        System.out.println("============");
        deepCopy();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class User implements Cloneable {
    int age;
    String name;
    Person person;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        final User user = (User) super.clone();
        user.person = (Person) person.clone();
        return user;
    }
}

@Data
@AllArgsConstructor
class Person implements Cloneable {
    private int value;
    private String person;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}