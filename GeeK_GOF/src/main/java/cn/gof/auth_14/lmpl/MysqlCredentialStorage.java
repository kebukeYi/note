package cn.gof.auth_14.lmpl;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 14:50
 * @description:
 * @question:
 * @link:
 **/
public class MysqlCredentialStorage implements CredentialStorage {

    @Override
    public String getPasswordByAppId(String appId) {
        if (appId.equals("12222")) {
            return "12222";
        }
        return "123456";
    }
}
 
