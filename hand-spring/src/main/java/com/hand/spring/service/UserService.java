package com.hand.spring.service;

import com.hand.spring.annoation.Autowried;
import com.hand.spring.annoation.Component;
import com.hand.spring.aware.BeanNameAware;
import com.hand.spring.init.InitializingBean;

/**
 * @author : kebukeYi
 * @date :  2021-12-12 00:50
 * @description: 默认是单例
 * @question:
 * @link:
 **/
@Component("userService")
public class UserService implements InitializingBean {

    @Autowried
    OrderService orderService;

    public void getOrderService() {
        System.out.println(orderService);
    }

    public UserService() {
        System.out.println("UserService be created");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
 
