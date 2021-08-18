package com.java.note.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  18:28
 * @Description
 */
public class ProducerCreator {

    public static Producer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER_LIST);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 开启GZIP压缩
        properties.put("compression.type", "gzip");
        //生产端提供消息发送幂等性 仅保证单分区、单producer会话
        properties.put("enable.idempotence", true);
        //TCP 连接是在创建 KafkaProducer 实例时建立的
        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) {
        String TOPIC = "test-topic";
        Producer<String, String> producer = ProducerCreator.createProducer();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka!");
        try {
            //事务型
            producer.initTransactions();
            try {
                //能够保证 Record1 和 Record2 被当作一个事务统一提交到 Kafka，要么它们全部提交成功，要么全部写入失败
                producer.beginTransaction();
                producer.send(record);
                producer.send(record);
                producer.commitTransaction();
            } catch (KafkaException e) {
                producer.abortTransaction();
            }
            //send message
            RecordMetadata metadata = producer.send(record).get();
            //带有回调通知的api
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                }
            });
            System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        producer.close();
    }
}
