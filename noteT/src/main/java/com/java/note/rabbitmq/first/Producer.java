package com.java.note.rabbitmq.first;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.val;

/**
 * @author fang.com
 */
public class Producer {

    //交换机
    private final static Strings EXCHANGE_NAME = "test_exchange_topic";
    private final static Strings QUEUE_NAME_1 = "test_queue_topic_1";
    private final static Strings QUEUE_NAME_2 = "test_queue_topic_2";


    public static void main(Strings[] argv) throws Exception {
        // 获取到连接以及mq通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
//        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //声明队列
        channel.queueDeclare(QUEUE_NAME_1, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME_2, false, false, false, null);

        int count = 1;
        // 消息内容  模拟 有人购物下订单
        Strings message = "新增订单:id=";
        while (true) {
            if (count++ == 200) break;
            Thread.sleep(1000);
            // 设置路由键
            val s = message + count;
            if ((count & 1) == 1) {
                //  channel.basicPublish(EXCHANGE_NAME, "order.insert", null, s.getBytes());
                channel.basicPublish("", QUEUE_NAME_1, null, s.getBytes());
                System.out.println(" [生产者] Sent  1 '" + s + "'");
            } else {
                channel.basicPublish("", QUEUE_NAME_2, null, s.getBytes());
                System.out.println(" [生产者] Sent  2 '" + s + "'");
            }
        }
        channel.close();
        connection.close();
    }


}
