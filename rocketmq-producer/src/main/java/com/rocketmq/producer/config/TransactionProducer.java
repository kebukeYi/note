package com.rocketmq.producer.config;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 12:57
 * @Description :
 * @Version :  0.0.1
 */
@Component
public class TransactionProducer {

    private String producerGroup = "ordertransgroup";

    private TransactionMQProducer producer;
    //用于执行本地事务和事务状态回查的监听器
    @Autowired

    OrderTransactionListener orderTransactionListener;

    //执行任务的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @PostConstruct
    public void init() {
        producer = new TransactionMQProducer(producerGroup);
        //创建支持消息轨迹的消息
        //DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup, true);
        producer.setNamesrvAddr("39.96.63.187:9876");
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        producer.setExecutorService(executor);
        producer.setTransactionListener(orderTransactionListener);
        this.start();
    }

    private void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    //事务消息发送
    public TransactionSendResult send(String data, String topic) throws MQClientException {
        Message message = new Message(topic, data.getBytes());
        return this.producer.sendMessageInTransaction(message, null);
    }

    //生产者顺序消息发送
    public TransactionSendResult sendOrderlyMessage(String data, String topic, Long orderId) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        Message message = new Message(topic, data.getBytes());
        //这个消息到broker上，会基于key构建hash索引，这个hash索引就存放在IndexFile索引文件里
        //可以通过MQ提供的命令去根据key查询这个消息，类似下面这样：mqadmin queryMsgByKey -n 127.0.0.1:9876 -t SCANRECORD -k orderId
        message.setKeys(String.valueOf(orderId));
        //顺序发送消息
        SendResult sendResult = this.producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                //根据订单取模
                long id = orderId % list.size();
                //返回同一个 MessageQueue
                return list.get((int) id);
            }
        }, orderId);
        return (TransactionSendResult) sendResult;
    }

    //标签自定义属性消息发送
    public TransactionSendResult sendTagMessage(String data, String topic, String tag) throws MQClientException {
        Message message = new Message(topic, tag, data.getBytes());
        message.putUserProperty("name", "kebi");
        message.putUserProperty("age", "22");
        return this.producer.sendMessageInTransaction(message, null);
    }

    //延迟消息发送
    public TransactionSendResult sendDelay(String data, String topic) throws MQClientException {
        Message message = new Message(topic, data.getBytes());
        //延迟级别为3
        //1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(3);
        return this.producer.sendMessageInTransaction(message, null);
    }


    //发送消息轨迹
    public TransactionSendResult sendTranceMessage(String data, String topic) throws MQClientException {
        Message message = new Message(topic, data.getBytes());
        return this.producer.sendMessageInTransaction(message, null);
    }


}
