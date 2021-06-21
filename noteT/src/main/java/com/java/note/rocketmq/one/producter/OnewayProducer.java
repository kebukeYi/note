package com.java.note.rocketmq.one.producter;

import com.java.note.rocketmq.setting.Remote;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  14:45
 * @Description 这种方式主要用在不特别关心发送结果的场景，例如日志发送。
 */
public class OnewayProducer {

    public static void main(Strings[] args) throws Exception {

        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("onewayProducer_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr(Remote.IP + ":" + Remote.PORT);
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest", "TagA", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);

        }
        System.out.println("发送完毕");
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
