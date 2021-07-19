package cn.gof.composite.oa;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:48
 * @description: 员工自己
 * @question:
 * @link:
 **/
public class Employee extends HumanResource {

    public Employee(long id, double salary) {
        super(id);
        this.salary = salary;
    }

    @Override
    public double calculateSalary() {
        return salary;
    }
}
 
