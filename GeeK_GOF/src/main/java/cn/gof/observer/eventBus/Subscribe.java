package cn.gof.observer.eventBus;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : kebukeyi
 * @date :  2021-07-20 15:53
 * @description :
 * @question :
 * @usinglink :
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Beta
public @interface Subscribe {


}
