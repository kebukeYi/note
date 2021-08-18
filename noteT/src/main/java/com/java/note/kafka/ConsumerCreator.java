package com.java.note.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  18:28
 * @Description
 */
public class ConsumerCreator {

    public static Consumer<String, String> createConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID_CONFIG);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(properties);
    }

    public static void main(String[] args) throws InterruptedException {
        String TOPIC = "test-topic";
        Consumer<String, String> consumer = ConsumerCreator.createConsumer();
        try {
            // 循环消费消息
            while (true) {
                //subscribe topic and consume message
                consumer.subscribe(Collections.singletonList(TOPIC));
                Thread.sleep(2000);
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println("Consumer consume message:" + consumerRecord.value());
                    //异步提交位移量
                    consumer.commitAsync();
                }
            }
        } catch (Exception e) {
            // 最后一次提交使用同步阻塞式提交
            consumer.commitSync();
        } finally {
            consumer.close();
        }
    }
}
