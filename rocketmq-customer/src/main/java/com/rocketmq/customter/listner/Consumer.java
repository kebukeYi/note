package com.rocketmq.customter.listner;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
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
        consumer.subscribe("order", "*");
        consumer.registerMessageListener(orderListener);
        consumer.start();
    }
}
