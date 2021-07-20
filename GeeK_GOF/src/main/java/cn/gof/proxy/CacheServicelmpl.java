package cn.gof.proxy;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 15:44
 * @description:
 * @question:
 * @link:
 **/
public class CacheServicelmpl implements CacheService {

    @Override
    public String getUser(int id, int age) {
        return getUserFromDB();
    }


    public String getUserFromDB() {
        return "mysql";
    }
}
 
