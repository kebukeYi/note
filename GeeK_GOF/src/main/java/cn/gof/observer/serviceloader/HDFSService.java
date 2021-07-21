package cn.gof.observer.serviceloader;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 12:15
 * @description:
 * @question:
 * @link:
 **/
public class HDFSService implements IService {
    @Override
    public String sayHello() {
        return "Hello HDFSService";
    }

    @Override
    public String getScheme() {
        return "hdfs";
    }
}
 
