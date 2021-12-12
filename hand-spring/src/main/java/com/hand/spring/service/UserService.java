package com.hand.spring.service;

import com.hand.spring.annoation.Autowried;
import com.hand.spring.annoation.Component;
import com.hand.spring.init.InitializingBean;

/**
 * @author : kebukeYi
 * @date :  2021-12-12 00:50
 * @description: 默认是单例 userService
 * @question:
 * @link:
 **/
@Component
public class UserService implements InitializingBean, IUserService {

    @Autowried
    OrderService orderService;

    private String beanPostProcessorName;

    public void setBeanPostProcessorName(String beanPostProcessorName) {
        beanPostProcessorName = beanPostProcessorName;
    }

    public void getOrderService() {
        System.out.println(orderService);
    }

    public UserService() {
        System.out.println("UserService be created");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("UserService.afterPropertiesSet()~");
    }

    @Override
    public String proxyBean() {
        return "UserService.proxyBean";
    }
}
 
