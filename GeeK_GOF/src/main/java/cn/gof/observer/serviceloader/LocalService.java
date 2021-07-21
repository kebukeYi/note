package cn.gof.observer.serviceloader;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 12:16
 * @description:
 * @question:
 * @link:
 **/
public class LocalService implements IService {
    @Override
    public String sayHello() {
        return "Hello LocalService";
    }

    @Override
    public String getScheme() {
        return "local";
    }
}
 
