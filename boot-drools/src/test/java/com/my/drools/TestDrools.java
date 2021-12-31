package com.my.drools;

import com.my.drools.entity.Order;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author : kebukeYi
 * @date :  2021-12-30 00:06
 * @description:
 * @question:
 * @link:
 **/
public class TestDrools {

    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        //会话对象,⽤于和规则引擎交互
        KieSession kieSession = kieContainer.newKieSession();
        //构造订单对象，设置订单⾦额，由规则引擎计算获得的积分
        Order order = new Order();
        order.setAmout(200);
        //将数据交给规则引擎，规则引擎会根据提供的数据进⾏规则匹配
        kieSession.insert(order);
        //激活规则引擎，如果匹配成功则执⾏规则
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
        //打印结果;
        System.out.println("订单提交之后积分：" + order.getScore());
    }

}
 
