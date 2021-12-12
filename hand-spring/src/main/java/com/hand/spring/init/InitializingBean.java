package com.hand.spring.init;

/**
 * @author : kebukeyi
 * @date :  2021-12-12 19:54
 * @description :
 * @question :
 * @usinglink :
 **/
public interface InitializingBean {

    /**
     * Bean 处理了属性填充后调用
     *
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
