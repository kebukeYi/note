package cn.gof.composite.oa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:48
 * @description: 部门下 可能有员工 也能有部门
 * @question:
 * @link:
 **/
public class Department extends HumanResource {

    List<HumanResource> humanResources = new ArrayList<>();

    public Department(long id) {
        super(id);
    }

    @Override
    public double calculateSalary() {
        double salary = 0;
        for (HumanResource humanResource : humanResources) {
            salary += humanResource.calculateSalary();
        }
        this.salary = salary;
        return salary;
    }

    public void addSubNode(HumanResource hr) {
        humanResources.add(hr);
    }
}
 
