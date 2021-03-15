package com.rocketmq.customter.listner;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 14:30
 * @Description :
 * @Version :  0.0.1
 */
@Component
public class Consumer {

    String consumerGroup = "consumer-group";

    DefaultMQPushConsumer consumer;

    @Autowired
    OrderListener orderListener;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr("39.96.63.187:9876");
        //接着我们可以在消费的时候根据tag和属性进行过滤，比如我们可以通过下面的代码去指定，我们只要tag=TableA和tag=TableB的数据
        // consumer.subscribe("order", "TagA || TagB");
        //或者指定语法
        //consumer.subscribe("order", MessageSelector.bySql("a>5 And b='abc' "));
        consumer.subscribe("order", "*");
        consumer.registerMessageListener(orderListener);
        consumer.start();
    }
}
