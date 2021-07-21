package cn.gof.observer.serviceloader;

import java.util.ServiceLoader;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 12:24
 * @description:
 * @question:
 * @link:
 **/
public class ServiceLoaderTest {

    public static void main(String[] args) {
        ServiceLoader<IService> serviceLoader = ServiceLoader.load(IService.class);
        for (IService service : serviceLoader) {
            System.out.println(service.getScheme() + "=" + service.sayHello());
        }
    }

}
 
