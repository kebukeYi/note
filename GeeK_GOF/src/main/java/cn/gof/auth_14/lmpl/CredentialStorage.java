package cn.gof.auth_14.lmpl;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 14:50
 * @description : 从存储中取出 AppID 和对应的密码
 * @question :
 * @usinglink :
 **/
public interface CredentialStorage {

    String getPasswordByAppId(String appId);

}
