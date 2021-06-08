package com.java.note.Jdk.Ato;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-06-05 14:13
 * @description : 案例初始方案是先修改数据库，再修改内存对象。改进后的方案是先修改数据库，然后再基于数据库查询的数据构建新的内存对象替换当前的内存对象。
 * final 关键字只是为了防止外部可以直接修改内存对象中的数据;
 * @question :
 * @usinglink :
 **/
public class SmsRouter {

    private static volatile SmsRouter smsRouter = new SmsRouter();

    private Map<Integer, SmsInfo> smsInfoMap;

    public SmsRouter() {
        //把短信服务商信息从数据库中加载到内存中(这里使用模拟的方式)，用字段smsInfoRouteMap保存。其中smsInfoRouteMap的Key为服务商排名
        this.smsInfoMap = this.loadSmsInfoRouteMapFromDb();
    }

    public static SmsRouter getSmsRouter() {
        return smsRouter;
    }

    public static void setSmsRouter(SmsRouter smsRouter) {
        SmsRouter.smsRouter = smsRouter;
    }

    //从数据库获取数据
    private Map<Integer, SmsInfo> loadSmsInfoRouteMapFromDb() {
        HashMap<Integer, SmsInfo> hashMap = new HashMap<>();
        hashMap.put(1, new SmsInfo("www.baidu.com", 180L));
        hashMap.put(2, new SmsInfo("www.aliyun.com", 180L));
        hashMap.put(3, new SmsInfo("www.honor.com", 180L));
        return hashMap;
    }

    //版本一
    private void changeSmsInfoMap() {
        Map<Integer, SmsInfo> smsInfoMap = smsRouter.loadSmsInfoRouteMapFromDb();
        //不是原子操作 会存在中间状态
        SmsInfo smsInfo = smsInfoMap.get(3);
        smsInfo.setUrl("www.jiguang.com");
        smsInfo.setMaxSize(190L);
    }

    //版本二
    public Map<Integer, SmsInfo> getSmsInfoRouteMap() {
        //做了防御性复制
        //即便外部改变获取到对象的状态，也不会影响SmsRouter本身的smsInfoRouteMap数据
        return Collections.unmodifiableMap(deepCopy(smsInfoMap));
    }

    //版本二
    private void changeSmsRouteMap() {
        //更新数据库

        //当短信服务商列表发生变化的时候，我们通过调用changeRouteInfo方法，更新数据库中的服务商信息，接着替换整个SmsRouter实例
        SmsRouter.setSmsRouter(new SmsRouter());
    }


    //版本二
    private Map<Integer, SmsInfo> deepCopy(Map<Integer, SmsInfo> smsInfoMap) {
        HashMap<Integer, SmsInfo> objectHashMap = new HashMap<>(smsInfoMap.size());
        for (Map.Entry<Integer, SmsInfo> smsInfoEntry : smsInfoMap.entrySet()) {
            objectHashMap.put(smsInfoEntry.getKey(), smsInfoEntry.getValue());
        }
        return objectHashMap;
    }

}
 
