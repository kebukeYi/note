package com.java.note.rocketmq.five;

import com.java.note.rocketmq.setting.Remote;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  14:40
 * @Description 批量发送
 */
public class BroadCastingProducer {

    public static void main(String[] args) throws Exception {

        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("BroadCastingProducer_group_name");

        // 设置NameServer的地址
        producer.setNamesrvAddr(Remote.IP + ":" + Remote.PORT);

        // 启动Producer实例
        producer.start();

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < Remote.COUNT; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest", "TagA", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            messages.add(msg);
        }

        // 发送消息到一个Broker 会阻塞吗？
        SendResult sendResult = producer.send(messages);
        // 通过sendResult返回消息是否成功送达
        System.out.printf("%s%n", sendResult);

        Thread.sleep(5000);
        System.out.println("发送完毕");
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
