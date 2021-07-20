package cn.gof.observer.publisher;

import java.io.File;

/**
 * @author : kebukeyi
 * @date :  2021-07-20 16:33
 * @description :  通用观察者接口
 * @question :
 * @usinglink :
 **/
public interface EventListener {
    void update(String eventType, File file);
}
