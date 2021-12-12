package com.hand.spring.service;

import com.hand.spring.annoation.Autowried;
import com.hand.spring.annoation.Component;
import com.hand.spring.aware.BeanNameAware;

/**
 * @author : kebukeYi
 * @date :  2021-12-12 00:14
 * @description: 设置成多例 @Scope("prototype")
 * @question:
 * @link:
 **/
@Component("orderService")
// @Scope("prototype")
public class OrderService implements BeanNameAware {

    //Can not set com.hand.spring.service.UserService field com.hand.spring.service.OrderService.userService to com.sun.proxy.$Proxy5
    @Autowried
    private IUserService userService;

    private String beanName;

    public OrderService() {
        System.out.println("OrderService be created");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void getUserService() {
        System.out.println(userService);
        System.out.println(beanName);
    }
}
 
