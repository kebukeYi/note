package cn.gof.composite.oa;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:47
 * @description:
 * @question:
 * @link:
 **/
public abstract class HumanResource {
    protected long id;
    protected double salary;

    public HumanResource(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public abstract double calculateSalary();

}
 
