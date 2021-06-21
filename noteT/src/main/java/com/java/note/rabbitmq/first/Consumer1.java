package com.java.note.rabbitmq.first;

import com.rabbitmq.client.*;

/**
 * @author fang.com
 */
public class Consumer1 {

    //与交换机进行了绑定
    private final static Strings QUEUE_NAME = "test_queue_topic_1";

    private final static Strings EXCHANGE_NAME = "test_exchange_topic";

    public static void main(Strings[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        // 获取到连接以及mq通道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机   设置路由键 order.#  模糊对应
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "order.#");

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel);
        // 监听队列，手动返回完成 or  消费者不声明队列，直接从队列中消费
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer);

        // 获取消息
        while (true) {
            Thread.sleep(500);
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            Strings message = new Strings(delivery.getBody());
            System.out.println(" [财务系统] Received '" + message + "'");
            Thread.sleep(10);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

}
