package cn.gof.auth_14.lmpl;

import cn.gof.auth_14.ApiRequest;

/**
 * @author : kebukeyi
 * @date :  2021-07-15 14:39
 * @description : 暴露一组给外部调用者使用的 API 接口，作为触发执行鉴权逻辑的入口
 * @question :
 * @usinglink :
 **/
public interface ApiAuthencator {

    boolean auth(String url);

    boolean auth(ApiRequest apiRequest);
}
