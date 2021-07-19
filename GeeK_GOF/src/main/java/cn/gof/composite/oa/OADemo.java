package cn.gof.composite.oa;

import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:55
 * @description:
 * @question:
 * @link: https://time.geekbang.org/column/article/207456
 **/
public class OADemo {

    //将一组对象（员工和部门）组织成树形结构，以表示一种‘部分 - 整体’的层次结构（部门与子部门的嵌套结构）。
    // 组合模式让客户端可以统一单个对象（员工）和组合对象（部门）的处理逻辑（递归遍历）

    private static final long ORGANIZATION_ROOT_ID = 1001;
    private DepartmentRepo departmentRepo = new DepartmentRepo(); // 依赖注入
    private EmployeeRepo employeeRepo = new EmployeeRepo(); // 依赖注入

    public double buildOrganization() {
        Department rootDepartment = new Department(ORGANIZATION_ROOT_ID);
        buildOrganization(rootDepartment);
        final double salary = rootDepartment.calculateSalary();
        return salary;
    }

    private void buildOrganization(Department department) {
        List<Long> subDepartmentIds = departmentRepo.getSubDepartmentIds(department.getId());
        for (Long subDepartmentId : subDepartmentIds) {
            Department subDepartment = new Department(subDepartmentId);
            department.addSubNode(subDepartment);
            buildOrganization(subDepartment);
        }
        List<Long> employeeIds = employeeRepo.getDepartmentEmployeeIds(department.getId());
        for (Long employeeId : employeeIds) {
            double salary = employeeRepo.getEmployeeSalary(employeeId);
            department.addSubNode(new Employee(employeeId, salary));
        }
    }

    public static void main(String[] args) {
        final OADemo oaDemo = new OADemo();
        System.out.println(oaDemo.buildOrganization());
    }
}
 
