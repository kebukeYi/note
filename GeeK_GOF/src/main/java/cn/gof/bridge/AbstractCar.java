package cn.gof.bridge;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 16:32
 * @description:
 * @question:
 * @link:
 **/
public abstract class AbstractCar {

    protected Transmission transmission;

    public abstract void run();

    public void setTransmission(Transmission gear) {
        this.transmission = gear;
    }
}
 
