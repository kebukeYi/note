package com.java.note.rocketmq.five;

import com.java.note.rocketmq.setting.Remote;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  14:45
 * @Description  消息广播
 */
public class BroadCastingConsumerA {

    public static void main(String[] args) throws InterruptedException, MQClientException {

        // 实例化消费者 天然是一个集群 消费模块
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("BroadCastingConsumer_group_name");

        // 设置NameServer的地址
        consumer.setNamesrvAddr(Remote.IP + ":" + Remote.PORT);

        //默认是集群模式，改成消息广播接受模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        // 订阅一个或者多个Topic，或者 多个 Tags   来过滤需要消费的消息
        consumer.subscribe("TopicTest", "*");

        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                try {
                    for (MessageExt msg : msgs) {
                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String result = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("BroadCastingConsumerA消费消息-----topic : " + topic + " , tags : " + tags + " , result ： " + result);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    //消息重试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 启动消费者实例
        consumer.start();
        System.out.println("Consumer Started...");
    }
}
