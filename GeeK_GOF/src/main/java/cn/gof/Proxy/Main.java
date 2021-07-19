package cn.gof.Proxy;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 15:53
 * @description:
 * @question:
 * @link:
 **/
public class Main {

    public static void main(String[] args) {
        final CacheServicelmpl cacheServicelmpl = new CacheServicelmpl();
        final CacheService proxyObject = (CacheService) CacheProxy.getProxyObject(cacheServicelmpl);
        System.out.println(proxyObject.getUser(1, 2));
    }
}
 
