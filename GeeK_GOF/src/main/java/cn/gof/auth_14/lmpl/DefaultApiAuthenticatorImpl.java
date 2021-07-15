package cn.gof.auth_14.lmpl;

import cn.gof.auth_14.ApiRequest;
import cn.gof.auth_14.AuthToken;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 14:50
 * @description: 只是向外部暴露 api 接口
 * @question:
 * @link:
 **/
public class DefaultApiAuthenticatorImpl implements ApiAuthencator {

    //持久层组合
    private CredentialStorage storage;


    public DefaultApiAuthenticatorImpl() {
        storage = new MysqlCredentialStorage();
    }

    //依赖注入 万一以后更换数据源即可
    public DefaultApiAuthenticatorImpl(CredentialStorage storage) {
        this.storage = storage;
    }


    @Override
    public boolean auth(String url) {
        //从前台传过来的 url 拆分为 实体类
        final ApiRequest apiRequest = ApiRequest.buildFromUrl(url);
        return auth(apiRequest);
    }

    @Override
    public boolean auth(ApiRequest apiRequest) {
        String appId = apiRequest.getAppId();
        String token = apiRequest.getToken();
        long timestamp = apiRequest.getTimestamp();
        String originalUrl = apiRequest.getBaseUrl();
        //只服务于 token 种种操作 的类
        AuthToken clientAuthToken = new AuthToken(token, timestamp);
        //token 是否过期了
        if (clientAuthToken.isExpiredTimeInterval()) {
            throw new RuntimeException("Token is expired!!!");
        }
        String password = storage.getPasswordByAppId(appId);
        //没过期的情况下 重新生成 token 进行对比
        AuthToken serverAuthToken = AuthToken.create(originalUrl, appId, password, timestamp);
        //是否一样
        if (serverAuthToken.match(clientAuthToken)) {
            throw new RuntimeException("Token verfication failed!!!");
        }
        return false;
    }

}
 
